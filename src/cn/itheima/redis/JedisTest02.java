package cn.itheima.redis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;

public class JedisTest02 {

	//单独连接1台redis服务器
	private static Jedis jedis;

	//2.主从，哨兵，使用share
	private static ShardedJedis shard;
	private static ShardedJedisPool pool;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		//单个节点
		jedis = new Jedis("192.168.1.191" , 6379);

		//分片
		List<JedisShardInfo> shards = Arrays.asList(new JedisShardInfo("192.168.1.191",6379));
		shard = new ShardedJedis(shards);

		GenericObjectPoolConfig goConfig = new GenericObjectPoolConfig();
		goConfig.setMaxTotal(100);
		goConfig.setMaxIdle(20);
		goConfig.setMaxWaitMillis(-1);
		goConfig.setTestOnBorrow(true);
		pool = new ShardedJedisPool(goConfig, shards);
	}

	@AfterClass
	public static void tearDownAfterClass()throws Exception{
		jedis.disconnect();
		shard.disconnect();
		pool.destroy();
	}
	
	/**
	 * String
	 */
	@Test
	public void testString(){
		//1.添加数据
		jedis.set("name", "张三");
		System.out.println(jedis.get("name"));

		jedis.append("name", "is a good man"); //拼接
		System.out.println(jedis.get("name"));

		jedis.del("name");//根据 key 删除
		System.out.println(jedis.get("name"));

		//设置多个键值对
		jedis.mset("name","zhangsan","age","27","qq","5455353534");
		jedis.incr("age");//age 进行加1操作
		System.out.println(jedis.get("name") + " - " + jedis.get("age") + " - " + jedis.get("qq"));
	}
	
	/**
	 * jedis 操作map
	 */
	@Test
	public void testMap(){
		//-----添加数据------
		Map<String,String> map = new HashMap<String,String>();
		map.put("name", "xinxin");
		map.put("age", "22");
		map.put("qq", "123456789");
		jedis.hmset("user", map);
		
		//取出user中的name
		
		//第一个参数是存入redis中的map对象的key，后面的是放入map中的对象的key，后面的key可以跟多个，是可变参数
		List<String> rsmap = jedis.hmget("user", "name","age","qq");
		System.out.println(rsmap);
		
		//删除map中的某个键值
		//jedis.del("user","age");
		System.out.println(jedis.hmget("user", "age")); //因为删除了，所以返回的为null
		System.out.println(jedis.hlen("user"));  //返回key为user的键中存放的值的个数  2
		System.out.println(jedis.exists("user")); //是否存在key为user的记录    true
		System.out.println(jedis.hkeys("user"));  //返回map对象中的所有key
		System.out.println(jedis.hvals("user"));  //返回map对象中的所有value
		
		Iterator<String> iterator = jedis.hkeys("user").iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			System.out.println(key + ":" + jedis.hmget("user", key));
		}
	}
	
	/**
	 * jedis 操作list
	 */
	@Test
	public void testList(){
		//开始前，先移除所有的内容
		jedis.del("java framework");
		System.out.println(jedis.lrange("java framework", 0, -1));
		
		//先向java framework中存放三条
		jedis.lpush("java framework", "spring");
		jedis.lpush("java framework", "springmvc");
		jedis.lpush("java framework", "mybatis");
		
		//再取出所有数据jedis.lrange是按范围取出
		//第一个是key 第二个是其实位置  第三个是结束位置  jedis.llen获取长度  -1 获取获取所有
		System.out.println(jedis.lrange("java framework", 0, -1));
		
		//jedis.del("java framework");
		/*jedis.rpush("java framework", "spring");
		jedis.rpush("java framework", "springmvc");
		jedis.rpush("java framework", "mybatis");
		
		System.out.println(jedis.lrange("java framework", 0, -1));*/
	}
	
	/**
	 * jedis操作set
	 */
	@Test
	public void testSet(){
		//添加
		jedis.sadd("user", "liuling");
		jedis.sadd("user", "xinxin");
		jedis.sadd("user", "ling");
		jedis.sadd("user", "zhangxinxin");
		jedis.sadd("user", "who");
		
		//移除
		jedis.srem("user", "who");
		System.out.println(jedis.smembers("user"));  //获取所有加入的value
		System.out.println(jedis.sismember("user", "who"));  //判断 who 是否是user集合的元素
		System.out.println(jedis.srandmember("user"));  //随机取出一个key的值
		System.out.println(jedis.scard("user")); //返回集合的元素个数
	}
	
	/**
	 * 测试RLPush
	 */
	@Test
	public void testRLpush(){
		//jedis 排序
		//注意，此处的rpush和lpush是list的操作，是个双向链表
		jedis.del("a");
		jedis.rpush("a", "1");
		jedis.lpush("a", "6");
		jedis.lpush("a", "3");
		jedis.lpush("a", "9");
		System.out.println(jedis.lrange("a", 0, -1));
		System.out.println(jedis.sort("a"));
		System.out.println(jedis.lrange("a", 0, -1));
	}
	
	@Test
	public void testTrans(){
		long start = System.currentTimeMillis();
        Transaction tx = jedis.multi();
        for (int i = 0; i < 1000; i++) {
            tx.set("t" + i, "t" + i);
        }
        //System.out.println(tx.get("t1000").get());
 
        List<Object> results = tx.exec();
        long end = System.currentTimeMillis();
        System.out.println("Transaction SET: " + ((end - start)/1000.0) + " seconds");
	}
	
	@Test
	public void testPipelineTrans(){
		long start = System.currentTimeMillis();
		Pipeline pipeline = jedis.pipelined();
		pipeline.multi();
		for (int i = 0; i <100000; i++) {
			pipeline.set(""+i, "" + i);
		}
		pipeline.exec();
		List<Object> results = pipeline.syncAndReturnAll();
		long end = System.currentTimeMillis();
		System.out.println("Pipelined transaction SET:" + ((end - start)/1000.0) + "seconds");
	}
	
	@Test
	public void testShard(){
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			shard.set("shard" + i, "n" + i);
		}
		long end = System.currentTimeMillis();
		System.out.println("Shard SET: " + ((end - start)/1000.0) + "seconds");
	}
	
	@Test
	public void testShardpipelined(){
		ShardedJedisPipeline pipeline = shard.pipelined();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			pipeline.set("sp" + i, "p" + i);
		}
		List<Object> results = pipeline.syncAndReturnAll();
		long end = System.currentTimeMillis();
		System.out.println("shardPipelined SET ： " +((end - start)/1000.0) + "seconds");
	}
	
	@Test
	public void testShardPool(){
		ShardedJedis sj = pool.getResource();
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			String results = sj.set("span", "n" + i);
		}
		long end = System.currentTimeMillis();
		pool.returnResource(sj);
		System.out.println("shardPool SET:" + ((end - start)/1000.0)+"seconds" );
	}
	
	@Test
	public void delKey(){
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			jedis.del("t"+i);
		}
		long end= System.currentTimeMillis();
		System.out.println((end-start)/1000.0);
	}
	
	
}
