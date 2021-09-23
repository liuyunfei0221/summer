package com.blue.rocket.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 通用组件工厂类
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
