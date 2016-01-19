package com.woyaofa.api;

import com.lib_common.activity.BaseActivity;
import com.lib_common.observer.ActivityObserver;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.ui.order.CommentListActivity;

import java.util.List;

/**
 * Created by LoaR on 15/11/23.
 */
public class ApiComment extends Api {

    public static final String URL_ADD = HOST + "comments/add";
    public static final String URL_LIST = HOST + "comments/list";

    private static ApiComment apiComment;

    public static ApiComment getInstance() {
        if (apiComment == null) {
            apiComment = new ApiComment();
        }
        return apiComment;
    }

    public void postAdd(ActivityObserver act, RequestBody body) {
        postRequest(act, URL_ADD, body);
    }

    public void getList(CommentListActivity act, List<Object> body) {
        getRequest(act, URL_LIST, body);
    }
}
