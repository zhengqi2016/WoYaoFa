package com.woyaofa.api;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.JsonObject;
import com.lib_common.activity.BaseActivity;
import com.lib_common.observer.ActivityObserver;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * 账号信息
 * Created by LoaR on 15/11/23.
 */
public class ApiAccount extends Api {
    public final static String URL_LOGIN = HOST + "accounts/signin";
    public final static String URL_CHANGE_PWD = HOST + "accounts/resetPwd";
    public final static String URL_REGISTER = HOST + "accounts/register";
    public final static String URL_PHONE = HOST + "accounts/valid";
    public final static String URL_CHANGE_PHONE = HOST + "accounts/changephone";

    //验证手机，0表示注册，1表示修改密码
    public static int TYPE_CODE_REGISTER = 0;
    public static int TYPE_CODE_CHANGE_PWD = 1;

    private static ApiAccount apiAccount;

    public static ApiAccount getInstance() {
        if (apiAccount == null) {
            apiAccount = new ApiAccount();
        }
        return apiAccount;
    }

    public void postLoginOnBack(ActivityObserver obs, RequestBody body) {
        postRequest(obs, URL_LOGIN, body);
    }

    public void postLogin(BaseActivity act, RequestBody body) {
        postRequest(act, URL_LOGIN, body);
    }

    public void postRegister(BaseActivity act, RequestBody body) {
        postRequest(act, URL_REGISTER, body);
    }

    public void postChangePwd(BaseActivity act, RequestBody body) {
        postRequest(act, URL_CHANGE_PWD, body);
    }

    public void getRegister(BaseActivity act, List<Object> params) {
        getRequest(act, URL_REGISTER, params);
    }

    public void getPhone(BaseActivity act, List<Object> params) {
        getRequest(act, URL_PHONE, params);
    }

    public void postChangePhone(BaseActivity act, RequestBody body) {
        postRequest(act, URL_CHANGE_PHONE, body);
    }
}
