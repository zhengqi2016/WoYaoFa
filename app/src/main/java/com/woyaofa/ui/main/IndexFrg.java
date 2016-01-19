package com.woyaofa.ui.main;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
import com.google.gson.JsonObject;
import com.lib_common.bean.KVbean;
import com.lib_common.fragment.BaseFragment;
import com.lib_common.util.PCAUtil;
import com.lib_common.util.ToastUtil;
import com.woyaofa.R;
import com.woyaofa.api.ApiMessage;
import com.woyaofa.bean.PCABean;
import com.woyaofa.config.Config;
import com.woyaofa.js.JSComm;
import com.woyaofa.ui.WebActivity;
import com.woyaofa.ui.mine.MessageListActivity;
import com.woyaofa.ui.widget.LinkageDialogView;
import com.woyaofa.util.LocationUtil;
import com.woyaofa.util.NavUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 首页
 *
 * @author loar
 */
public class IndexFrg extends BaseFragment {

    @Bind(R.id.frg_index_wv_body)
    WebView wvBody;
    @Bind(R.id.frg_index_tv_pca)
    TextView tvPca;
    @Bind(R.id.frg_index_tv_message)
    TextView tvMessage;
    @Bind(R.id.frg_index_sv_keyword)
    TextView svKeyword;

    private List<KVbean> datas;
    private LinkageDialogView linkageDialogView;
    private PCABean pca;
    private LatLng latLng;
    private String province;
    private String city;
    private String district;


    @Override
    public View onContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_index, null, false);
    }

    @Override
    public void init(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        setListener();
    }

    private void init() {

        WebViewClient client = new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!wvBody.getSettings().getLoadsImagesAutomatically()) {
                    wvBody.getSettings().setLoadsImagesAutomatically(
                            true);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        };

        wvBody.setWebViewClient(client);
        wvBody.setWebChromeClient(new WebChromeClient() {
        });

        WebSettings settings = wvBody.getSettings();
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(false);// 设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);

        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(Config.DEFAULT_DIR_NAME);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        wvBody.addJavascriptInterface(new JSComm(getContext()), "jscomm");
//        wvBody.addJavascriptInterface(new JSParams(getContext(), GsonUtil.getInstance().toJsonString(bundle)), "jsparams");

        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

        loadData();

        datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            KVbean item = new KVbean();
            item.setKey("黑龙江" + i);
            datas.add(item);
        }
        wvBody.addJavascriptInterface(datas, "datas");
        tvPca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPCADialog();
            }
        });
    }

    private void setListener() {
        wvBody.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        svKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_SEARCH);
                NavUtil.goToNewAct(getContext(), WebActivity.class, bundle);
            }
        });

    }

    @Override
    public void loadData() {
        super.loadData();
        wvBody.loadUrl("file:///android_asset/www/index.html");
        ApiMessage.getInstance().getUnRead(this);
    }


    @OnClick(R.id.frg_index_sv_keyword)
    public void onKeyword(View v) {
//        svKeyword.ge
    }

    @OnClick(R.id.frg_index_rl_message)
    public void onMessage(View v) {
        NavUtil.goToNewAct(getContext(), MessageListActivity.class);
//        Bundle bundle=new Bundle();
//        bundle.putString(WebActivity.BUNDLE_KEY_URL,WebActivity.URL_FILE_TEST);
//        NavUtil.goToNewAct(getContext(), WebActivity.class,bundle);
    }

    @OnClick(R.id.frg_index_tv_pca)
    public void onPca(View v) {
        showPCADialog();
    }

    public void showPCADialog() {
        if (linkageDialogView == null) {
            if (pca == null) {
                pca = PCAUtil.getPCA(getContext(), "pca.json", PCABean.class);
            }
            linkageDialogView = new LinkageDialogView(getContext());
            linkageDialogView.setTitle("城市选择");
            linkageDialogView.setPca(pca);
            linkageDialogView.setOnOkListener(new LinkageDialogView.OnOkListener() {
                @Override
                public void okClick(LinkageDialogView linkageDialogView, View v, String pName, String cName, String aName) {
                    ToastUtil.toast(getContext(), pName + " " + cName + " " + aName);
                    tvPca.setText(aName);
                    linkageDialogView.dismiss();
                }
            });
        }
        linkageDialogView.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationUtil.getInstance().startLocation(getContext(), new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                        province = amapLocation.getProvince();//省信息
                        city = amapLocation.getCity();//城市信息
                        district = amapLocation.getDistrict();//城区信息
                        tvPca.setText(district);
                        if (!(province == null || city == null || district == null)) {
                            LocationUtil.getInstance().stopLocation();
                        }
                    }
                }
            }
        });
        pca = PCAUtil.getPCA(getContext(), "pca.json", PCABean.class);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationUtil.getInstance().stopLocation();
    }

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
//        if (url.equals(ApiMessage.URL_UN_READ)) {
//            final List<MessageBean> list = GsonUtil.getInstance().toJsonArr(jo.toString(), new TypeToken<List<MessageBean>>() {
//            });
//            updateView(new Runnable() {
//                @Override
//                public void run() {
//                    tvMessage.setText(list.size());
//                }
//            });
//        }
    }
}
