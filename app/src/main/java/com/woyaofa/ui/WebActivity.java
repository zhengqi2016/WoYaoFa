package com.woyaofa.ui;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.JsonObject;
import com.lib_common.listener.OnClickListener;
import com.lib_common.util.ToastUtil;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.config.Config;
import com.woyaofa.js.JSComm;
import com.woyaofa.js.JSParams;
import com.woyaofa.js.JSRequest;

import butterknife.Bind;

/**
 * 标题控件加WebView的通用
 */
public class WebActivity extends MBaseActivity {

    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_URL = "url";
    public static final String BUNDLE_KEY_JSON = "jsonBean";
    public static final String BUNDLE_KEY_RIGHT = "right";
    public static final String BUNDLE_KEY_RIGHT_LISTENER = "rightClick";

    private String title;
    private String url;
    private String jsonBean;
    private String right;
    private OnClickListener rightListener;

    @Nullable
    @Bind(R.id.wv_content)
    public WebView wvContent;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_web);
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        title = bundle.getString(BUNDLE_KEY_TITLE);
        url = bundle.getString(BUNDLE_KEY_URL);
        jsonBean = bundle.getString(BUNDLE_KEY_JSON);
        right = bundle.getString(BUNDLE_KEY_RIGHT);
        rightListener = (OnClickListener) bundle.getSerializable(BUNDLE_KEY_RIGHT_LISTENER);
    }

    private void init() {
        if (title == null) {
            hvHead.setVisibility(View.GONE);
        } else {
            hvHead.setTitle(title);
        }
        if (right != null) {
            hvHead.setRight(right, 0);
        }
        if (rightListener != null) {
            hvHead.setOnClickRightListener(rightListener);
        }
        //其实应该放在webactivity中的
        if (wvContent != null) {
            WebViewClient client = new WebViewClient() {
//                @Override
//                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//                    super.onReceivedSslError(view, handler, error);
//                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (!wvContent.getSettings().getLoadsImagesAutomatically()) {
                        wvContent.getSettings().setLoadsImagesAutomatically(
                                true);
                    }
//                    wvContent.getSettings().setBlockNetworkImage(false);
//                    wvContent.getSettings().setBlockNetworkLoads(false);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            };

            wvContent.setWebViewClient(client);
            wvContent.setWebChromeClient(new WebChromeClient() {
            });

            WebSettings settings = wvContent.getSettings();
            settings.setBlockNetworkImage(false);//不阻止图片网络图片数据
            settings.setBlockNetworkLoads(false);//不阻止图片网络数据
            settings.setJavaScriptEnabled(true);
            settings.setAllowFileAccess(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setDefaultTextEncodingName("UTF-8");
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
            settings.setLoadWithOverviewMode(true);
            settings.setBuiltInZoomControls(false);
            settings.setSupportZoom(false);

            settings.setAppCacheEnabled(true);
            settings.setAppCachePath(Config.DEFAULT_DIR_NAME);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);


            if (Build.VERSION.SDK_INT >= 19) {
                settings.setLoadsImagesAutomatically(true);
            } else {
                settings.setLoadsImagesAutomatically(false);
            }

            wvContent.addJavascriptInterface(new JSComm(this), "jscomm");
            wvContent.addJavascriptInterface(new JSRequest(this, this), "jsrequest");
            if (jsonBean != null) {
                wvContent.addJavascriptInterface(new JSParams(this, jsonBean), "jsparams");
            }
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        wvContent.loadUrl(url);
    }

    @Override
    public void setRequestSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        updateView(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastAlways(WebActivity.this,jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
                onBack(null);
            }
        });
    }

    public static final String URL_FILE_LINE_DETAIL = "file:///android_asset/www/html/line_detail.html";
    public static final String URL_FILE_COMPANY_DETAIL = "file:///android_asset/www/html/company_detail.html";
    public static final String URL_FILE_COMPANY_INDEX = "file:///android_asset/www/html/company_index.html";
    public static final String URL_FILE_CLAIM = "file:///android_asset/www/html/claim.html";
    public static final String URL_FILE_CONTACT_US = "file:///android_asset/www/html/contact_us.html";
    public static final String URL_FILE_FEEDBACK = "file:///android_asset/www/html/feedback.html";
    public static final String URL_FILE_LOGISTICS_INFO = "file:///android_asset/www/html/logistics_info.html";
    public static final String URL_FILE_MAP = "file:///android_asset/www/html/map.html";
    public static final String URL_FILE_ORDER = "file:///android_asset/www/html/order.html";
    public static final String URL_FILE_ORDER_CANCEL = "file:///android_asset/www/html/order_cancel.html";
    public static final String URL_FILE_ORDER_DETAIL = "file:///android_asset/www/html/order_detail.html";
    public static final String URL_FILE_COMPLAIN = "file:///android_asset/www/html/complain.html";
    public static final String URL_FILE_EVALUATION_LIST = "file:///android_asset/www/html/evaluation_list.html";
    public static final String URL_FILE_SEARCH = "file:///android_asset/www/html/search.html";
    public static final String URL_FILE_KUAIDI = "http://m.kuaidi100.com/";
    public static final String URL_FILE_KUAIDI_ALL = "http://m.kuaidi100.com/all/";
    public static final String URL_FILE_TEST = "file:///android_asset/www/html/test.html";
}
