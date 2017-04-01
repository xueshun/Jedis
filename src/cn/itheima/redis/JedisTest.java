package cn.itheima.redis;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class JedisTest {
	
	@Test
	public void testJedis1()throws Exception{
		//创建和redis的连接
		Jedis jedis = new Jedis("192.168.1.191",6379);
		
		//存入
		jedis.set("key2", "2");
		
		//取出
		System.out.println(jedis.get("key2"));
		
		jedis.close();
	}
}
