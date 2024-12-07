package com.ruufilms.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisConfig {
    private static JedisPool jedisPool = new JedisPool("localhost",6379);
    //Get a Redis connection
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

    //Close a Redis connection
    public static void closeJedis(Jedis jedis){
        if(jedis != null){
            jedis.close();
        }
    }
}
