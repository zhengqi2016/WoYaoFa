package com.woyaofa.js;

import android.webkit.JavascriptInterface;

/**
 *
 * Created by LoaR on 15/11/10.
 */
public class JSIndex {

    @JavascriptInterface
    public void toastMessage(String message) {
    }

    @JavascriptInterface
    public void onSumResult(int result) {
    }
}
