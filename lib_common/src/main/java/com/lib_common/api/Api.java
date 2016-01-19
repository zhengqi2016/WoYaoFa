package com.lib_common.api;

import com.google.gson.JsonObject;
import com.lib_common.net.HttpRequest;
import com.lib_common.observer.ActivityObserver;
import com.lib_common.util.GsonUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;


public class Api {
//    public static final String HOST = "http://115.29.207.3:8080/HeMiao/";
//	 public static final String HOST = "http://192.168.199.170:8080/HeMiao/";

    // *****json key******
    public static final String KEY_STATUS_OK = "2000";
    public static final String KEY_STATUS = "code";
    public static final String KEY_DATA = "datas";
    public static final String KEY_LIST = "resultList";
    public static final String KEY_TOTAL_COUNT = "totalSize";
    public static final String KEY_PAGE_INDEX = "pageIndex";
    public static final String KEY_PAGE_SIZE = "pageSize";
    public static final String KEY_MSG = "msg";

    // ********************

    public static boolean checkStatusCode(JsonObject obj) {
        return KEY_STATUS_OK.equals(getResponseCode(obj));
    }

    public static String getResponseCode(JsonObject obj) {
        return obj.getAsJsonPrimitive(KEY_STATUS).getAsString();
    }

    public static String getResponseCode(String json) {
        return getResponseCode(GsonUtil.getInstance().toJsonObj(json));
    }

    /**
     * post请求
     *
     * @param act         实现ActivityObserver的activity
     * @param url
     * @param requestBody 请求参数
     */
    public static void postRequest(ActivityObserver act, String url, RequestBody requestBody) {
        HttpRequest.getInstance().postRequest(requestBody, getDefaultCallBack(act, url));
    }

    public static void getRequest(ActivityObserver act, String url) {
        HttpRequest.getInstance().getRequest(getDefaultCallBack(act, url));
    }

    public static void getRequest(ActivityObserver act, String url, List<Object> params) {
        HttpRequest.getInstance().getRequest(getDefaultCallBack(act, url), params);
    }


    public static HttpRequest.HttpCallBack getDefaultCallBack(final ActivityObserver act, final String url) {
        return new HttpRequest.HttpCallBack(url) {

            @Override
            public void onSuccess(String url, Response response, String data) {
                try {
                    JsonObject jo = GsonUtil.getInstance().toJsonObj(data);
                    if (checkStatusCode(jo)) {
                        act.setRequestSuc(url, getResponseCode(data), GsonUtil.getInstance().toJsonObj(data));
                    } else {
                        act.setRequestNotSuc(url, getResponseCode(data), GsonUtil.getInstance().toJsonObj(data));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String url, Response response, String data) {
                act.setRequestErr(url, response.code() + "", response);
            }

            @Override
            public void onException(String url, Request request, IOException e) {
                act.setRequestException(url, request, e);
            }

            @Override
            public void onFinish(String url) {
                act.setRequestFinish(url);
            }
        };
    }

}