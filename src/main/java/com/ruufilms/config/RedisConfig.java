package com.ruufilms.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class RedisConfig {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static final String REDIS_PASSWORD = "Danu2003@";

    public static Jedis getJedis(){
        Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        jedis.auth(REDIS_PASSWORD);
        return jedis;
    }

}
