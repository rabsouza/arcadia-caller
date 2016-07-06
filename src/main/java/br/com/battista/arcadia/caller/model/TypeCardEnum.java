package br.com.battista.arcadia.caller.model;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Created by rabsouza on 05/07/16.
 */
public enum TypeCardEnum {
    LEVEL_0,
    LEVEL_1,
    LEVEL_2,
    LEVEL_3,
    LEVEL_4,
    LEVEL_5,
    DEATH_CURSE;

    private static final Map<String, TypeCardEnum> LOOK_UP = Maps.newHashMap();

    static {
        for (TypeCardEnum typeCard :
                TypeCardEnum.values()) {
            LOOK_UP.put(typeCard.name().toLowerCase(), typeCard);
        }
    }

    public static TypeCardEnum get(String typeCard) {
        return LOOK_UP.get(typeCard);
    }

}
