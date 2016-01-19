package com.lib_common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class GsonUtil {

    private static GsonUtil gsonUtil;
    private static Gson gson;

    public static GsonUtil getInstance() {
        if (gsonUtil == null) {
            synchronized (GsonUtil.class) {
                if (gsonUtil == null) {
                    gsonUtil = new GsonUtil();
                }
            }
        }
        getGsonInstance();
        return gsonUtil;
    }

    private static Gson getGsonInstance() {
        if (gson == null) {
            synchronized (GsonUtil.class) {
                if (gson == null) {
                    gson = new GsonBuilder().setDateFormat(DateUtil.DEFAULT_PATTERN).create();
                }
            }
        }
        return gson;
    }

    public <T> T toJsonObj(JsonObject obj, Class<T> t) {
        return gson.fromJson(obj.toString(), t);
    }

    public <T> T toJsonObj(String json, Class<T> t) {
        return toJsonObj(toJsonObj(json), t);
    }

    /**
     * 若出现泛型丢失，请使用下面的方法 toJsonArr(String arrJson, TypeToken<List<T>> typeToken)
     *
     * @param arrJson
     * @param t
     * @param <T>
     * @return
     */
//    public <T> List<T> toJsonArr(String arrJson, Class<T> t) {
//        return toJsonArr(arrJson, new TypeToken<List<T>>() {
//        });
//    }
//
//    public <T> List<T> toJsonArr(JsonArray arrJson, Class<T> t) {
//        return toJsonArr(arrJson.toString(), new TypeToken<List<T>>() {
//        });
//    }

    public <T> List<T> toJsonArr(String arrJson, TypeToken<List<T>> typeToken) {
        return gson.fromJson(arrJson, typeToken.getType());
    }

    public <T> String toJsonString(T t) {
        return gson.toJson(t);
    }

    public JsonObject toJsonObj(String json) {
        JsonElement jp = new JsonParser().parse(json);
        return jp.getAsJsonObject();
    }
}
