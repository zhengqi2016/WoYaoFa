package com.lib_common.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.lib_common.observer.ActivityObserver;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements ActivityObserver {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = onContentView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        init(rootView, inflater, container, savedInstanceState);
        return rootView;
    }

    /**
     * 只能设置布局视图view，其他操作请在init等方法中执行
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public abstract View onContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void dealIntent(Bundle bundle) {

    }

    @Override
    public void loadData() {
    }

    private Handler updateHandler = new Handler();

    @Override
    public void updateView(Runnable runnable) {
        updateHandler.post(runnable);
    }

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {

    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, JsonObject jo) {

    }

    @Override
    public void setRequestErr(String url, String statusCode, Response response) {

    }

    @Override
    public void setRequestException(String url, Request request, IOException e) {

    }

    @Override
    public void setRequestFinish(String url) {
    }

    private boolean isRequesting;

    public void setIsRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public boolean isRequesting() {
        return isRequesting;
    }
}
