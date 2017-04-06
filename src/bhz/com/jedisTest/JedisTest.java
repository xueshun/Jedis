package bhz.com.jedisTest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import bhz.com.entity.User;
import bhz.com.util.FastJsonConvert;
import redis.clients.jedis.Jedis;

public class JedisTest {
	
	@Test
	public void testJedis1(){
		
		final String SYS_USERS_TABLE = "SYS_USERS_TABLE";
		final String SYS_USER_SEL_AGE_25 = "SYS_USER_SEL_AGE_25";
		final String SYS_USER_SEL_SEX_man = "SYS_USER_SEL_SEX_man";
		final String SYS_USER_SEL_SEX_woman = "SYS_USER_SEL_SEX_woman";
		
		//1.创建于Jedis的连接
		Jedis jedis = new Jedis("192.168.1.191",6379);
		
		Map<String,String> map = new HashMap<String,String>();
		
		//select * from SYS_USERS_TABLE WHERE AGE = 25
		//select * from SYS_USERS_TABLE WHERE AGE = 25 and sex = 'man'
		
		//多种集合配合使用    hash  和    set 类型同时使用
		
		//指定业务  查询业务  SYS_USER_SEL_AGE_25
		//                 SYS_USER_SEL_SEX_man
		//                 SYS_USER_SEL_SEX_woman
		
		
		//保存 user对象数据
		/*String u1id = UUID.randomUUID().toString();
		User u1 = new User(u1id,"张三",20,"man");
		map.put(u1id, FastJsonConvert.convertObjectToJSON(u1));
		jedis.sadd(SYS_USER_SEL_SEX_man, u1id);
		
		
		String u2id = UUID.randomUUID().toString();
		User u2 = new User(u2id,"李四",25,"man");
		map.put(u2id,FastJsonConvert.convertObjectToJSON(u2));
		jedis.sadd(SYS_USER_SEL_SEX_man, u2id);
		jedis.sadd(SYS_USER_SEL_AGE_25, u2id);
		
		String u3id = UUID.randomUUID().toString();
		User u3 = new User(u3id,"王丽",27,"woman");
		map.put(u3id, FastJsonConvert.convertObjectToJSON(u3));
		jedis.sadd(SYS_USER_SEL_SEX_woman, u3id);
		jedis.sadd(SYS_USER_SEL_AGE_25, u3id);
		
		String u4id = UUID.randomUUID().toString();
		User u4 = new User(u4id,"佳明",22,"man");
		map.put(u4id, FastJsonConvert.convertObjectToJSON(u4));
		jedis.sadd(SYS_USER_SEL_SEX_man, u4id);
		
		String u5id = UUID.randomUUID().toString();
		User u5 = new User(u5id,"Lina",22,"woman");
		map.put(u5id, FastJsonConvert.convertObjectToJSON(u5));	
		jedis.sadd(SYS_USER_SEL_SEX_woman, u5id);
		
		jedis.hmset(SYS_USERS_TABLE, map);*/
		
		//获取数据
		/*Iterator<String> iterator = jedis.hkeys(SYS_USERS_TABLE).iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			System.out.println(key + ":" + jedis.hmget(SYS_USERS_TABLE, key));
		}*/
		
		//获取年龄大于25的id
		/*Set<String> user_ages = jedis.smembers(SYS_USER_SEL_AGE_25);
		for (Iterator iterator = user_ages.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			String ret = jedis.hget(SYS_USERS_TABLE, string);
			System.out.println(ret);
		}
		System.out.println(user_ages);*/
		
		
		Set<String> user_age25_sexWoman = jedis.sinter(SYS_USER_SEL_AGE_25,SYS_USER_SEL_SEX_woman);
		for (Iterator iterator = user_age25_sexWoman.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println(string);
			String ret = jedis.hget(SYS_USERS_TABLE,string);
			System.out.println(ret);
			User user = FastJsonConvert.convertJSONToObject(ret, User.class);
			System.out.println(user);
		}
	}
}
