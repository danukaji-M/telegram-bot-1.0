package com.ruufilms.Beans;

import java.util.HashMap;

public class Sticker {
    private HashMap<String, String> utilSticker;
    private HashMap<String, String> qualitySticker;
    private HashMap<Integer, String> seasonSticker;

    public HashMap<String, String> getUtilSticker() {
        return utilSticker;
    }

    public void setUtilSticker(HashMap<String, String> utilSticker) {
        this.utilSticker = utilSticker;
    }

    public HashMap<String, String> getQualitySticker() {
        return qualitySticker;
    }

    public void setQualitySticker(HashMap<String, String> qualitySticker) {
        this.qualitySticker = qualitySticker;
    }

    public HashMap<Integer, String> getSeasonSticker() {
        return seasonSticker;
    }

    public void setSeasonSticker(HashMap<Integer, String> seasonSticker) {
        this.seasonSticker = seasonSticker;
    }
}
