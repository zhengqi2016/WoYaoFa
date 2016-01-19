package com.woyaofa.api;

import com.lib_common.observer.ActivityObserver;

import java.util.List;

/**
 * Created by LoaR on 15/11/23.
 */
public class ApiMessage extends Api {
    //    public static String URL_UN_READ = HOST + "users/modify";
    public static String URL_LIST = HOST + "home/messages";

    private static ApiMessage apiMessage;

    public static ApiMessage getInstance() {
        if (apiMessage == null) {
            apiMessage = new ApiMessage();
        }
        return apiMessage;
    }

    public void getUnRead(ActivityObserver act) {
//        getRequest(act, URL_UN_READ, null);
    }

    public void getList(ActivityObserver act, List<Object> body) {
        getRequest(act, URL_LIST, body);
    }

}
