package br.com.battista.arcadia.caller.model.enuns;

import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

/**
 * Created by rabsouza on 05/07/16.
 */
public enum TypeCardEnum {
    NONE,
    UPGRADE,
    DEATH_CURSE;

    private static final Map<String, TypeCardEnum> LOOK_UP = Maps.newHashMap();

    static {
        for (TypeCardEnum typeCard :
                TypeCardEnum.values()) {
            LOOK_UP.put(typeCard.name().toUpperCase(), typeCard);
        }
    }

    public static TypeCardEnum get(String typeCard) {
        return LOOK_UP.get(MoreObjects.firstNonNull(typeCard, NONE.name()).toUpperCase());
    }

}
