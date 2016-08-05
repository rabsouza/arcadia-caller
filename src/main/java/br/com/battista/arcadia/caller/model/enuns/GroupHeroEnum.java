package br.com.battista.arcadia.caller.model.enuns;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import java.util.Map;

public enum GroupHeroEnum {

    NONE,
    BEYOND_THE_GRAVE_EXPANSION,
    PROMO_HEROES,
    CORE_BOX,
    MONSTERS_AS_HEROES,
    EXTRA;

    private static final Map<String, GroupHeroEnum> LOOK_UP = Maps.newHashMap();

    static {
        for (GroupHeroEnum groupHero :
                GroupHeroEnum.values()) {
            LOOK_UP.put(groupHero.name().toUpperCase(), groupHero);
        }
    }

    public static GroupHeroEnum get(String groupHero) {
        return LOOK_UP.get(MoreObjects.firstNonNull(groupHero, NONE.name()).toUpperCase());
    }

}
