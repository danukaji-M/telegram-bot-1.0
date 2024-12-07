package com.ruufilms.cache;

import com.ruufilms.config.DatabaseConfig;
import com.ruufilms.config.RedisConfig;
import redis.clients.jedis.Jedis;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CacheSync {
    public static void syncRedisToMysql(){
        try(Jedis jedis = RedisConfig.getJedis()){
            for(String key: jedis.keys("*")){
                String value = jedis.get(key);
                upsertCacheData(key, value);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void upsertCacheData(String key, String value) throws SQLException {
        String sql = "INSERT INTO cache (cache_key, cache_value) " +
                "VALUES (?, ?) ON DUPLICATE KEY UPDATE" +
                " cache_value = VALUES(cache_value), " +
                "created_at = CURRENT_TIMESTAMP ";
        DatabaseConfig.executePreparedUpdate(sql, key, value);
    }

    public static void syncMySQLToRedis() throws SQLException{
        String sql = "SELECT cache_key, cache_value FROM cache";
        try(Jedis jedis = RedisConfig.getJedis()){
            ResultSet resultSet = DatabaseConfig.executeQuery(sql);
            while(resultSet.next()){
                String key = resultSet.getString("cache_key");
                String value = resultSet.getString("cache_value");
                jedis.set(key,value);
            }
        }
    }
}
