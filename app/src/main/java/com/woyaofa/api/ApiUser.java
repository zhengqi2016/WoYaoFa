package com.woyaofa.api;

import com.lib_common.activity.BaseActivity;
import com.squareup.okhttp.RequestBody;

/**
 * 用户信息
 * Created by LoaR on 15/11/23.
 */
public class ApiUser extends Api {
    public static String URL_MODIFY = HOST + "users/modify";//修改用户信息
    public static String URL_FACE = HOST + "users/face";

    private static ApiUser apiUser;

    public static ApiUser getInstance() {
        if (apiUser == null) {
            apiUser = new ApiUser();
        }
        return apiUser;
    }

    public void postModify(BaseActivity act, RequestBody body) {
        postRequest(act, URL_MODIFY, body);
    }

    public void postFace(BaseActivity act, RequestBody body) {
        postRequest(act, URL_FACE, body);
    }
}
