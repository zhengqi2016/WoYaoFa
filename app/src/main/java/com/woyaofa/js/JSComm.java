package com.woyaofa.js;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.google.gson.reflect.TypeToken;
import com.lib_common.bean.KVbean;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.ToastUtil;
import com.woyaofa.MApplication;
import com.woyaofa.MBaseActivity;
import com.woyaofa.bean.AccountBean;
import com.woyaofa.util.NavUtil;

import java.util.List;

/**
 * Created by LoaR on 15/11/10.
 */
public class JSComm {
    private Context context;

    public JSComm(Context context) {
        this.context = context;
    }

    /**
     * @param canonicalName
     * @param bundleJson
     */
    @JavascriptInterface
    public void goToNewAct(String canonicalName,
                           String bundleJson) {
        try {
            List<KVbean> list = GsonUtil.getInstance().toJsonArr(bundleJson, new TypeToken<List<KVbean>>() {
            });
            Bundle bundle = new Bundle();
            for (KVbean item : list) {
                bundle.putString(item.key, item.value);
            }
            NavUtil.goToNewAct(context, Class.forName(canonicalName), bundle);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void goToNewAct(String canonicalName) {
        try {
            ToastUtil.logE("JavascriptInterface", canonicalName);
            NavUtil.goToNewAct(context, Class.forName(canonicalName));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public String getAccount() {
        AccountBean account = MApplication.getApp().getAccount();
        return GsonUtil.getInstance().toJsonString(account);
    }

    @JavascriptInterface
    public String getAccountWithLogin() {
        AccountBean account = MApplication.getApp().getAccountWithLogin();
        return GsonUtil.getInstance().toJsonString(account);
    }

    @JavascriptInterface
    public void callPhone(String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(uri);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void callMap(String geo) {
        NavUtil.callMap(context, geo);
    }

    @JavascriptInterface
    public void onBack() {
        ((MBaseActivity) context).finish();
    }

    @JavascriptInterface
    public void toastAlways(String text) {
        if (text == null) {
            text = "";
        }
        ToastUtil.toastAlways(context, text);
    }

    @JavascriptInterface
    public void showLoading() {
        try {
            ((MBaseActivity) context).showLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void hideLoading() {
        try {
            ((MBaseActivity) context).hideLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
