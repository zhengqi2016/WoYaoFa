package com.woyaofa.api;

import com.lib_common.activity.BaseActivity;
import com.lib_common.observer.ActivityObserver;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.ui.mine.MyCollectionActivity;

import java.util.List;

/**
 * Created by LoaR on 15/11/23.
 */
public class ApiCompany extends Api {

    public static final String URL_LIST = HOST + "companies/list";

    private static ApiCompany apiCompany;

    public static ApiCompany getInstance() {
        if (apiCompany == null) {
            apiCompany = new ApiCompany();
        }
        return apiCompany;
    }

    public void getList(ActivityObserver act, List<Object> body) {
        getRequest(act, URL_LIST, body);
    }
}
