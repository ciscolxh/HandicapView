package com.loveluo.arcview.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 *
 * @author Luokes
 * @date 2018/3/13
 */

public class JsonUtils {
    private static Gson mGson = new Gson();

    /**
     * @param object object
     * @param <T> T
     * @return 将对象准换为json字符串
     */
    public static <T> String serialize(T object) {
        return mGson.toJson(object);
    }

    /**
     * @param json json
     * @param clz clz
     * @param <T> T
     * @return 将json字符串转换为对象
     */
    public static <T> T deserialize(String json, Class<T> clz) throws JsonSyntaxException {
        return mGson.fromJson(json, clz);
    }

    /**
     * @param json json
     * @param clz clz
     * @param <T> T
     * @return 将json对象转换为实体对象
     * @throws JsonSyntaxException e
     */
    public static <T> T deserialize(JsonObject json, Class<T> clz) throws JsonSyntaxException {
        return mGson.fromJson(json, clz);
    }

    /**
     * @param json json
     * @param type type
     * @param <T> T
     * @return 将json字符串转换为对象
     */
    public static <T> T deserialize(String json, Type type) throws JsonSyntaxException {
        return  mGson.fromJson(json,type);
    }



}
