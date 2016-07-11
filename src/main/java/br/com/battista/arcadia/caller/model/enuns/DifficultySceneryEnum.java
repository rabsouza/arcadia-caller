package br.com.battista.arcadia.caller.model.enuns;

import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

/**
 * Created by rabsouza on 05/07/16.
 */
public enum DifficultySceneryEnum {
    NONE,
    EASY,
    NORMAL,
    HARD;

    private static final Map<String, DifficultySceneryEnum> LOOK_UP = Maps.newHashMap();

    static {
        for (DifficultySceneryEnum groupCard :
                DifficultySceneryEnum.values()) {
            LOOK_UP.put(groupCard.name().toUpperCase(), groupCard);
        }
    }

    public static DifficultySceneryEnum get(String groupCard) {
        return LOOK_UP.get(MoreObjects.firstNonNull(groupCard, NONE.name()).toUpperCase());
    }

}
