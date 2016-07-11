package br.com.battista.arcadia.caller.model.enuns;

import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

/**
 * Created by rabsouza on 05/07/16.
 */
public enum LocationSceneryEnum {
    NONE,
    INNER_CIRCLE,
    OUT_CIRCLE,
    CENTER;

    private static final Map<String, LocationSceneryEnum> LOOK_UP = Maps.newHashMap();

    static {
        for (LocationSceneryEnum groupCard :
                LocationSceneryEnum.values()) {
            LOOK_UP.put(groupCard.name().toUpperCase(), groupCard);
        }
    }

    public static LocationSceneryEnum get(String groupCard) {
        return LOOK_UP.get(MoreObjects.firstNonNull(groupCard, NONE.name()).toUpperCase());
    }

}
