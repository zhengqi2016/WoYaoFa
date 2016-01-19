package com.woyaofa.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.JsonObject;
import com.lib_common.fragment.BaseFragment;
import com.lib_common.observer.ActivityObserver;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.woyaofa.MApplication;
import com.woyaofa.R;
import com.woyaofa.ui.mine.LoginActivity;
import com.woyaofa.ui.widget.NavBarView;
import com.woyaofa.util.NavUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class MainActivity extends FragmentActivity implements ActivityObserver {

    @Bind(R.id.activity_main_nav_bar)
    NavBarView navBar;

    private List<BaseFragment> fragments;

    public String[] frgClasses = {IndexFrg.class.getCanonicalName(),
            OrderFrg.class.getCanonicalName(),
            MineFrg.class.getCanonicalName()};

    private int[] frgLayoutIds = {R.id.activity_main_fl_0,
            R.id.activity_main_fl_1,
            R.id.activity_main_fl_2};

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MApplication.getApp().addActivity(this);
        fm = getSupportFragmentManager();

        initFragments();

        navBar.setOnTabChangedCallback(new NavBarView.OnTabChangedCallback() {
            @Override
            public boolean onTabChanged(View v, int index) {
                if (index == 2 && MApplication.getApp().getAccount() == null) {
                    NavUtil.goToNewAct(MainActivity.this, LoginActivity.class);
                    return false;
                } else {
                    selectTab(index);
                }
                return true;
            }
        });
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        try {
            FragmentTransaction ft = fm.beginTransaction();
            for (int i = 0; i < frgClasses.length; i++) {
                BaseFragment f = (BaseFragment) Class.forName(frgClasses[i])
                        .newInstance();
                fragments.add(f);
                ft.add(frgLayoutIds[i], f, frgClasses[i]);
            }
            ft.commit();
        } catch (Exception e) {
            ToastUtil.printErr(e);
        } finally {
            selectTab(0);
        }
    }

    public void selectTab(int selectedIndex) {
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            ft.hide(fragment);
        }
        ft.show(fragments.get(selectedIndex)).commit();
        navBar.selectTab(selectedIndex);
    }

    @Override
    public void dealIntent(Bundle bundle) {

    }

    @Override
    public void updateView(Runnable runnable) {

    }

    @Override
    public void loadData() {
        for (BaseFragment bf : fragments) {
            bf.loadData();
        }
//        selectTab(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MApplication.getApp().removeActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }
}
