package com.woyaofa.api;

import com.lib_common.activity.BaseActivity;
import com.squareup.okhttp.RequestBody;

import java.util.List;

/**
 * Created by LoaR on 15/11/23.
 */
public class ApiAddress extends Api {

    public static final int TYPE_SEND = 0;
    public static final int TYPE_RECEIVE = 1;

    public static String URL_LIST = HOST + "addressbooks/list";
    public static String URL_ADD = HOST + "addressbooks/add";
    public static String URL_MODIFY = HOST + "addressbooks/modify";
    public static String URL_SET_DEFAULT = HOST + "addressbooks/setdefault";
    public static String URL_DELETE = HOST + "addressbooks/delete";

    private static ApiAddress apiRegister;

    public static ApiAddress getInstance() {
        if (apiRegister == null) {
            apiRegister = new ApiAddress();
        }
        return apiRegister;
    }

    public void getList(BaseActivity act, List<Object> body) {
        getRequest(act, URL_LIST, body);
    }

    public void postAdd(BaseActivity act, RequestBody body) {
        postRequest(act, URL_ADD, body);
    }

    public void postModify(BaseActivity act, RequestBody body) {
        postRequest(act, URL_MODIFY, body);
    }

    public void getSetDefault(BaseActivity act, List<Object> body) {
        getRequest(act, URL_SET_DEFAULT, body);
    }

    public void getDelete(BaseActivity act, List<Object> body) {
        getRequest(act, URL_DELETE, body);
    }
}
