package com.ruufilms.Models;

import com.ruufilms.Beans.Stickers;
import com.ruufilms.config.DatabaseConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class StickerModel {

    // Cache for stickers
    private static final HashMap<String, Stickers> stickersCache = new HashMap<>();

    public Stickers loadSeasonStickers() {
        if (stickersCache.containsKey("all")) {
            return stickersCache.get("all");
        }

        Stickers stickers = new Stickers();

        try {
            ResultSet seasonResults = DatabaseConfig.executeQuery("SELECT * FROM `season_stickers`");
            while (seasonResults.next()) {
                int season = seasonResults.getInt("id");
                String sticker = seasonResults.getString("sticker");
                stickers.addSeasonSticker(season, sticker);
            }

            ResultSet qualityResults = DatabaseConfig.executeQuery("SELECT * FROM `quality_sticker`");
            while (qualityResults.next()) {
                String quality = qualityResults.getString("quality");
                String sticker = qualityResults.getString("sticker");
                stickers.addQualitySticker(quality, sticker);
            }

            ResultSet otherResults = DatabaseConfig.executeQuery("SELECT * FROM `other_stickers`");
            while (otherResults.next()) {
                String name = otherResults.getString("name");
                String sticker = otherResults.getString("sticker");
                stickers.setOtherStickers(name, sticker);  // Add to otherStickers map
            }

            stickersCache.put("all", stickers);

            DatabaseConfig.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stickers;
    }
}
