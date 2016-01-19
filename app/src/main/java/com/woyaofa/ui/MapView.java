package com.woyaofa.ui;

import android.os.Bundle;

import com.lib_common.util.GsonUtil;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.bean.OverlayPoint;
import com.woyaofa.js.JSMap;
import com.woyaofa.js.JSParams;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by LoaR on 15/11/13.
 */
public class MapView extends WebActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_map);
        init();
        setListener();
    }

    private void init() {
//        wvContent.addJavascriptInterface("asdddd", "wvHeight");
        List<OverlayPoint> overlayPoints = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            overlayPoints.add(new OverlayPoint(i, "name" + i, 116.397428 - i, 39.90923 - i));
        }
        wvContent.addJavascriptInterface(new JSParams(this, GsonUtil.getInstance().toJsonString(overlayPoints)), "jsparams");
        wvContent.addJavascriptInterface(new JSMap(this), "jsmap");
    }

    private void setListener() {

    }

    @Override
    public void loadData() {
        super.loadData();
        wvContent.loadUrl("file:///android_asset/www/html/map.html");
    }
}
