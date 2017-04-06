package bhz.com.jedisTest;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
public class TestClusterRedis {

	public static void main(String[] args) {
		Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
		jedisClusterNode.add(new HostAndPort("192.168.1.191", 7001));
		jedisClusterNode.add(new HostAndPort("192.168.1.191", 7002));
		jedisClusterNode.add(new HostAndPort("192.168.1.191", 7003));
		jedisClusterNode.add(new HostAndPort("192.168.1.191", 7004));
		jedisClusterNode.add(new HostAndPort("192.168.1.191", 7005));
		jedisClusterNode.add(new HostAndPort("192.168.1.191", 7006));

		JedisPoolConfig cfg = new JedisPoolConfig();
		cfg.setMaxTotal(100);
		cfg.setMaxIdle(20);
		cfg.setMaxIdle(-1);
		cfg.setTestOnBorrow(true);
		JedisCluster jc = new JedisCluster(jedisClusterNode,6000,100,cfg);
		/*
		jc.set("name", "xue");
		jc.set("sex", "man");
		jc.set("age", "22");*/

		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("age"));
		System.out.println(jc.get("sex"));
		jc.close();
	}
}
