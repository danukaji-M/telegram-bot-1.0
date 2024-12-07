package com.ruufilms.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruufilms.Beans.Sticker;
import com.ruufilms.config.RedisConfig;
import redis.clients.jedis.Jedis;

import java.util.HashMap;

public class StickerCache {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Save Sticker object in Redis
    public static void saveSticker(String stickerId, Sticker sticker) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            String stickerJson = objectMapper.writeValueAsString(sticker);
            jedis.set("sticker:" + stickerId, stickerJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get Sticker object from Redis by ID
    public static Sticker getSticker(String stickerId) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            String stickerJson = jedis.get("sticker:" + stickerId);
            if (stickerJson != null) {
                return objectMapper.readValue(stickerJson, Sticker.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Delete Sticker object from Redis
    public static void deleteSticker(String stickerId) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            jedis.del("sticker:" + stickerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update Sticker object in Redis
    public static void updateSticker(String stickerId, Sticker updatedSticker) {
        saveSticker(stickerId, updatedSticker);
    }

    public static void main(String[] args) {
        // Create a Sticker object
        Sticker sticker = new Sticker();
        HashMap<String, String> utilSticker = new HashMap<>();
        utilSticker.put("sticker1", "useful");
        utilSticker.put("sticker2", "handy");

        HashMap<String, String> qualitySticker = new HashMap<>();
        qualitySticker.put("high", "excellent");
        qualitySticker.put("low", "mediocre");

        HashMap<Integer, String> seasonSticker = new HashMap<>();
        seasonSticker.put(1, "winter");
        seasonSticker.put(2, "summer");

        sticker.setUtilSticker(utilSticker);
        sticker.setQualitySticker(qualitySticker);
        sticker.setSeasonSticker(seasonSticker);

        // Save Sticker in cache
        StickerCache.saveSticker("sticker123", sticker);

        // Retrieve Sticker from cache
        Sticker cachedSticker = StickerCache.getSticker("sticker123");
        System.out.println("Cached Sticker: " + cachedSticker.getSeasonSticker().get(2));

        // Update Sticker
        utilSticker.put("sticker3", "extra useful");
        sticker.setUtilSticker(utilSticker);
        StickerCache.updateSticker("sticker123", sticker);

        // Retrieve the updated Sticker
        Sticker updatedSticker = StickerCache.getSticker("sticker123");
        System.out.println("Updated Sticker: " + updatedSticker.getQualitySticker().get("high"));

        // Delete Sticker
        StickerCache.deleteSticker("sticker123");
        Sticker deletedSticker = StickerCache.getSticker("sticker123");
        System.out.println("Deleted Sticker: " + deletedSticker); // Should be null
    }
}
