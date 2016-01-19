package com.woyaofa.api;

import com.lib_common.activity.BaseActivity;
import com.squareup.okhttp.RequestBody;

/**
 * Created by LoaR on 15/11/23.
 */
public class ApiLine extends Api {

    public static final String URL_LIST = HOST + "lines/list";
    public static final int TYPE_LIST_EVALUATE = 1;
    public static final int TYPE_LIST_VOLUME = 2;
    public static final int TYPE_LIST_EFFECIVENESS = 0;

    private static ApiLine apiLine;

    public static ApiLine getInstance() {
        if (apiLine == null) {
            apiLine = new ApiLine();
        }
        return apiLine;
    }

    public void postList(BaseActivity act, RequestBody body) {
        postRequest(act, URL_LIST, body);
    }

}
