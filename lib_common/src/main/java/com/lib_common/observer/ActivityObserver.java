package com.lib_common.observer;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.JsonObject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * 请求统一的方法，和请求结果的处理
 * Created by loar on 2015/7/10.
 */
public interface ActivityObserver {

    /**
     * 对传过来的数据进行处理
     *
     * @param bundle
     */
    void dealIntent(Bundle bundle);

    /**
     * 数据加载后更新界面
     */
    void updateView(Runnable runnable);

    /**
     * 数据加载,有数据请求的基本都要放在这里,以跟新界面
     */
    void loadData();


    /**
     * 网络请求成功 后重写
     *
     * @param statusCode
     * @param statusCode
     * @param jo
     */
    void setRequestSuc(String url, String statusCode,
                       JsonObject jo);

    /**
     * @param statusCode
     * @param statusCode
     * @param jo
     */
    void setRequestNotSuc(String url, String statusCode,
                          JsonObject jo);

    /**
     * 状态码非200的结果处理，服务器内部异常
     *
     * @param url
     * @param response
     */
    void setRequestErr(String url, String statusCode,Response response);

    /**
     * 网络异常 后重写
     *
     * @param url
     * @param request
     * @param e
     */
    void setRequestException(String url, Request request, IOException e);

    /**
     * 请求结束
     */
    void setRequestFinish(String url);


}
