package br.com.battista.arcadia.caller.constants;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Created by rabsouza on 04/07/16.
 */
public enum ProfileAppConstant {

    ADMIN,
    APP;

    private final static Map<String, ProfileAppConstant> LOOK_UP = Maps.newHashMap();

    static {
        for (ProfileAppConstant profile :
                ProfileAppConstant.values()) {
            LOOK_UP.put(profile.name(), profile);
        }
    }

    public static ProfileAppConstant get(String name) {
        return LOOK_UP.get(name);
    }
}
