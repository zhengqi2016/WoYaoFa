package com.woyaofa.js;

import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.lib_common.util.ToastUtil;
import com.woyaofa.util.NavUtil;

/**
 * 用于js参数传递的对象
 * Created by LoaR on 15/11/10.
 */
public class JSParams {
    private Context context;
    private String json;


    public JSParams(Context context, String json) {
        this.context = context;
        this.json = json;
    }


    @JavascriptInterface
    public String getJson() {
        return json;
    }
}
