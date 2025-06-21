package com.example.norush.util;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class FavoriteUtil {

    private FavoriteUtil() {

    }

    public static String generateFavoriteId() {

        String uuidWithoutHyphens = UUID.randomUUID().toString().replace("-", "");

        String generatedId = uuidWithoutHyphens.substring(0, Math.min(uuidWithoutHyphens.length(), 20)); // 최대 20자

        log.debug("Generated Favorite ID: {}", generatedId);
        return generatedId;
    }

    public static boolean isValidFavoriteType(String type){

        if(type ==null || type.trim().isEmpty()){
            return false;
        }
        return type.equals("버스") || type.equals("지하철")||type.equals("경로")||type.equals("장소");
    }
}
