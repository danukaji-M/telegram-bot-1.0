package com.ruufilms.Beans;

import java.util.HashMap;
import java.util.Map;

public class Stickers {

    Map<Integer, String> seasonStickers;
    Map<String, String> videoQualities;
    Map<String, String> otherStickers;

    public Stickers(){
        seasonStickers = new HashMap<>();
        videoQualities = new HashMap<>();
        otherStickers = new HashMap<>();  // Initialize the otherStickers map
    }

    public void addSeasonSticker(int seasonNumber, String sticker){
        seasonStickers.put(seasonNumber,sticker);
    }

    public String getSeasonSticker(int seasonNumber){
        return seasonStickers.get(seasonNumber);
    }

    public void addQualitySticker(String quality, String sticker){
        videoQualities.put(quality,sticker);
    }

    public String getQualitySticker(String quality){
        return videoQualities.get(quality);
    }

    public void setOtherStickers(String name, String sticker){
        otherStickers.put(name, sticker);
    }

    public String getOtherSticker(String name){
        return otherStickers.get(name);
    }
}
