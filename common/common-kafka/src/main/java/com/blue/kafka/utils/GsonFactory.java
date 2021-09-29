package com.blue.kafka.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * gson factory
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class GsonFactory {

    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    public static Gson getGson() {
        return GSON;
    }

}
