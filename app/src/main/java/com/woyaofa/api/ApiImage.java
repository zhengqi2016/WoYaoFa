package com.woyaofa.api;

import com.lib_common.activity.BaseActivity;
import com.squareup.okhttp.RequestBody;

/**
 *
 * Created by LoaR on 15/11/23.
 */
public class ApiImage extends Api {
    public static String URL_PLACE_AN_ORDER = HOST + "orders/upload";
    public static String URL_COMMENT = HOST + "comments/upload";

    private static ApiImage apiImage;

    public static ApiImage getInstance() {
        if (apiImage == null) {
            apiImage = new ApiImage();
        }
        return apiImage;
    }

    public void postPlaceAnOrder(BaseActivity act, RequestBody body) {
        postRequest(act, URL_PLACE_AN_ORDER, body);
    }

    public void postFace(BaseActivity act, RequestBody body) {
        postRequest(act, URL_PLACE_AN_ORDER, body);
    }
    public void postComment(BaseActivity act, RequestBody body) {
        postRequest(act, URL_COMMENT, body);
    }
}
