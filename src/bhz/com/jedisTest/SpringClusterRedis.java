package bhz.com.jedisTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class SpringClusterRedis {
	
	private ApplicationContext applicationContext;
	
	@Before
	public void setUp(){
		String configLocation = "classpath:applicationContext-cluster.xml";
		applicationContext = new ClassPathXmlApplicationContext(configLocation);
	}
	
	@Test
	public void testJedisSpring() throws Exception{
		//获取连接池
		JedisCluster jc =(JedisCluster) applicationContext.getBean("redisCluster");
		/*JedisPool jedisPool = (JedisPool)applicationContext.getBean("jedisPool");
		//获取连接
		Jedis jedis = jedisPool.getResource();
		//存入
		jedis.set("key4", "bbb");*/
		//取出
		System.out.println(jc.get("sex"));
		
	}
}
