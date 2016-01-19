package com.woyaofa.js;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.google.gson.reflect.TypeToken;
import com.lib_common.bean.KVbean;
import com.lib_common.observer.ActivityObserver;
import com.lib_common.util.GsonUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.api.Api;

import java.util.List;

/**
 * Created by LoaR on 15/11/10.
 */
public class JSRequest {
    private ActivityObserver act;
    private Context context;

    public JSRequest(Context context, ActivityObserver act) {
        this.context = context;
        this.act = act;
    }

    @JavascriptInterface
    public void requestPost(String url, String jsonParams) {
        try {
            List<KVbean> list = GsonUtil.getInstance().toJsonArr(jsonParams, new TypeToken<List<KVbean>>() {
            });
            FormEncodingBuilder feb = new FormEncodingBuilder();
            for (int i = 0; i < list.size(); i++) {
                KVbean item = list.get(i);
                feb.add(item.getKey(), item.getValue());
            }
            RequestBody body = feb.build();
            Api.postRequest(act, url, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
