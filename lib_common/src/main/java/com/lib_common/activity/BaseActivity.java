package com.lib_common.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.JsonObject;
import com.lib_common.BaseApp;
import com.lib_common.dialog.LoadingDialog;
import com.lib_common.observer.ActivityObserver;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.ButterKnife;

public class BaseActivity extends Activity implements ActivityObserver {

    private LoadingDialog loadingDialog;

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.getInstance(this);
        }
        loadingDialog.showLoading();
    }

    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismissLoading();
        }
    }

    public void showKeyBoard(View view) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
//        if (!isOpen) {
//            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
//        view.requestFocus(View.FOCUS_DOWN);
        InputMethodManager inputManager =
                (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        ((BaseApp) getApplicationContext()).addActivity(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dealIntent(bundle);
        }
        mainHanler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    if (!isRequesting()) {
                        loadData();
                    }
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
            }
        });
    }

    public void setContentViews(int layoutResID) {
        setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    /**
     * 对传过来的数据进行处理
     *
     * @param bundle
     */
    public void dealIntent(Bundle bundle) {
    }

    protected Handler mainHanler = new Handler();

    /**
     * 数据加载后更新界面(在主线程)
     */
    public void updateView(Runnable runnable) {
        if (runnable != null) {
            mainHanler.post(runnable);
        }
    }

    /**
     * 数据加载,有数据请求的基本都要放在这里,以跟新界面
     */
    public void loadData() {
        setIsRequesting(true);
    }

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {

    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, JsonObject jo) {

    }

    @Override
    public void setRequestErr(String url, String statusCode, Response response) {
        updateView(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastAlways(BaseActivity.this, "服务器繁忙，请重新尝试！");
            }
        });
    }

    @Override
    public void setRequestException(String url, Request request, IOException e) {
        updateView(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastAlways(BaseActivity.this, "网络好像出了问题，请检查网络哦！");
            }
        });
    }

    @Override
    public void setRequestFinish(String url) {
        setIsRequesting(false);
    }

    /**
     * 统一返回事件
     *
     * @param v
     */
    public void onBack(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((BaseApp) getApplicationContext()).removeActivity(this);
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


    private boolean isRequesting;

    public void setIsRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public boolean isRequesting() {
        return isRequesting;
    }
}
