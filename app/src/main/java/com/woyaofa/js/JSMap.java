package com.woyaofa.js;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.lib_common.dialog.TipDialog;

/**
 *
 * Created by LoaR on 15/11/10.
 */
public class JSMap {
    private Context context;

    public JSMap(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void showDialog(String title) {
        TipDialog tipDialog = new TipDialog(context);
        tipDialog.setTitle(title);
        tipDialog.setContent(title, null, null);
        tipDialog.show();
    }
}
