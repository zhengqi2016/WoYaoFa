package com.lib_common.net;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okio.BufferedSource;

public class HttpRequest {
    private static final String TAG_URL = "HttpRequestUrl";
    private static final String TAG_RESULT = "HttpRequestResult";
    private static OkHttpClient client;
    private static HttpRequest httpRequest;

    public static HttpRequest getInstance() {
        if (httpRequest == null) {
            synchronized (HttpRequest.class) {
                if (httpRequest == null) {
                    httpRequest = new HttpRequest();
                }
            }
        }
        getHttpClient();
        return httpRequest;
    }

    public static OkHttpClient getHttpClient() {
        if (client == null) {
            synchronized (OkHttpClient.class) {
                if (client == null) {
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }

    /**
     * @param value 毫秒值
     */
    public void setTimeout(long value) {
        getHttpClient().setConnectTimeout(value, TimeUnit.MILLISECONDS);
    }

    public void postRequest(RequestBody body, HttpCallBack callBack) {
        Log.d(TAG_URL, callBack.url + " == " + body.toString());
        Request request = new Request.Builder().post(body).url(callBack.url).build();
        getHttpClient().newCall(request).enqueue(callBack);
    }

    public void getRequest(HttpCallBack callBack) {
        Log.d(TAG_URL, callBack.url);
        Request request = new Request.Builder().url(callBack.url).build();
        getHttpClient().newCall(request).enqueue(callBack);
    }

    public void getRequest(HttpCallBack callBack, List<Object> params) {
        String url = callBack.url + parseRestfulParams(params);
        Log.d(TAG_URL, url);
        Request request = new Request.Builder().url(url).build();
        getHttpClient().newCall(request).enqueue(callBack);
    }

    public void postFile(File file, HttpCallBack callBack) {

    }

    public static String parseParams(HashMap<String, Object> params) {
        if (params == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        Iterator<Map.Entry<String, Object>> iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            sb.append(key + "=" + val + "&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String parseRestfulParams(List<Object> params) {
        if (params == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < params.size(); i++) {
            sb.append("/" + params.get(i).toString());
        }
        return sb.toString();
    }

    public static abstract class HttpCallBack implements Callback {
        public String url;
        public String data;

        public HttpCallBack(String url) {
            this.url = url;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            try {
                Log.d(TAG_RESULT, "onFailure === " + e.getMessage());
                onException(url, request, e);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                onFinish(url);
            }
        }

        @Override
        public void onResponse(Response response) {
            try {
                data = response.body().string();
                Log.d(TAG_RESULT, "onResponse === " + data);
                if (response.isSuccessful()) {
                    onSuccess(url, response, data);
                } else {
                    onError(url, response, data);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                onFinish(url);
            }
        }

        public abstract void onSuccess(String url, Response response, String data);

        /**
         * 请求成功，但非200状态码
         *
         * @param url
         * @param response
         * @param data
         */
        public abstract void onError(String url, Response response, String data);

        /**
         * 请求失败，比如网络异常
         *
         * @param url
         * @param request
         * @param e
         */
        public abstract void onException(String url, Request request, IOException e);

        public abstract void onFinish(String url);
    }
}


