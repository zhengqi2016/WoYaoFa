package com.woyaofa.api;

import com.lib_common.observer.ActivityObserver;
import com.squareup.okhttp.RequestBody;

import java.util.List;

/**
 * Created by LoaR on 15/11/23.
 */
public class ApiCollection extends Api {

    /**
     * (0表示公司，1表示线路)
     */
    public static final int TYPE_COMPANY = 0;
    public static final int TYPE_LINE = 1;

    public static final String URL_LIST = HOST + "collections/list";
    public static final String URL_DELETE = HOST + "collections/cancel";

    private static ApiCollection apiCollection;

    public static ApiCollection getInstance() {
        if (apiCollection == null) {
            apiCollection = new ApiCollection();
        }
        return apiCollection;
    }

    public void getList(ActivityObserver act, List<Object> body) {
        getRequest(act, URL_LIST, body);
    }

    public void postDelete(ActivityObserver act, RequestBody body) {
        postRequest(act, URL_DELETE, body);
    }

}
