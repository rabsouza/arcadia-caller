package br.com.battista.arcadia.caller.constants;

import java.util.Map;

import com.google.common.collect.Maps;

public enum ProfileAppConstant {

    ADMIN,
    APP;

    private static final Map<String, ProfileAppConstant> LOOK_UP = Maps.newHashMap();

    static {
        for (ProfileAppConstant profile :
                ProfileAppConstant.values()) {
            LOOK_UP.put(profile.name().toUpperCase(), profile);
        }
    }

    public static ProfileAppConstant get(String name) {
        return LOOK_UP.get(name);
    }
}
