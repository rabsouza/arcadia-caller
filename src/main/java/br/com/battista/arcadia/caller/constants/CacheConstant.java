package br.com.battista.arcadia.caller.constants;

public final class CacheConstant {

    public static final int MAXIMUM_SIZE_CACHE = 10000;
    public static final int DURATION_CACHE = 1 * 60;
    public static final String HEADER_CACHE_CONTROL_MAX_AGE_VALUE = "public, max-age=9600, max-stale=3600";
    public static final String HEADER_NO_CACHE_CONTROL = "no-cache, no-store, max-age=0, must-revalidate";

    private CacheConstant() {
    }

}
