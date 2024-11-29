package com.ruufilms.models;

import com.ruufilms.config.DatabaseConfig;

import java.sql.SQLException;
import java.sql.Timestamp;

public class RedisCache {
    private String cacheKey;
    private String cacheValue;
    private String relatedTable;
    private String relatedId;
    private Timestamp createdAt;
    private String status;
    private Timestamp expiredAt;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Timestamp expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public String getRelatedTable() {
        return relatedTable;
    }

    public void setRelatedTable(String relatedTable) {
        this.relatedTable = relatedTable;
    }

    public String getCacheValue() {
        return cacheValue;
    }

    public void setCacheValue(String cacheValue) {
        this.cacheValue = cacheValue;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public void createRedisCache(String cacheKey, String cacheValue, String relatedTable, String relatedId, Timestamp expiredAt, String status){
        String sql = "INSERT INTO(`cache_key`,`cache_value`,`related_table`.`related_id`,`expires_at`,`status`) VALUES (?,?,?,?,?,?)";
        try{
            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql,cacheKey,cacheValue,relatedTable,relatedId,expiredAt,status);
            if(rowsAffected>0){
                System.out.println("Successfully Created");
            }else{
                System.out.println("Something went wrong");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
