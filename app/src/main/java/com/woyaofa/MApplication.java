package com.woyaofa;

import android.os.Bundle;
import android.os.Handler;

import com.google.gson.JsonObject;
import com.lib_common.BaseApp;
import com.lib_common.observer.ActivityObserver;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MD5Util;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiAccount;
import com.woyaofa.bean.AccountBean;
import com.woyaofa.ui.mine.LoginActivity;
import com.woyaofa.util.NavUtil;

import java.io.IOException;

/**
 * Created by LoaR on 15/11/2.
 */
public class MApplication extends BaseApp {
    protected static MApplication app;
    private AccountBean account;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static MApplication getApp() {
        return app;
    }

    public AccountBean getAccount() {
        if (account == null) {
            account = getUser(AccountBean.class);
        }
        return account;
    }

    public AccountBean getAccountWithLogin() {
        if (getAccount() == null) {
            NavUtil.goToNewAct(this, LoginActivity.class);
        }
        return account;
    }


    @Override
    public void requestOnBack() {
        super.requestOnBack();
        account = getUser(AccountBean.class);
        if (account != null) {
            ToastUtil.log("login", "login on back");
            RequestBody body = new FormEncodingBuilder()
                    .add("username", account.getName()).add("password", MD5Util.md5(account.getPassword())).build();
            ApiAccount.getInstance().postLoginOnBack(new ActivityObserver() {

                @Override
                public void dealIntent(Bundle bundle) {

                }

                @Override
                public void updateView(Runnable runnable) {

                }

                @Override
                public void loadData() {

                }

                @Override
                public void setRequestSuc(String url, String statusCode, JsonObject jo) {
                    try {
                        String pwd = account.getPassword();
                        AccountBean accountBean = GsonUtil.getInstance().toJsonObj(jo.getAsJsonObject(Api.KEY_DATA), AccountBean.class);
                        accountBean.setPassword(pwd);
                        account = accountBean;
                        saveUserSync(account);
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                updateActivities();
                            }
                        });
                    } catch (Exception e) {
                        ToastUtil.printErr(e);
                    }
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
            }, body);
        }
    }

    public void logout() {
        getUserSP().clear();
        account = null;
    }

    /**
     * 异步
     *
     * @param accountBean
     */
    public void saveUserAsyn(final AccountBean accountBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveUserSync(accountBean);
            }
        }).start();
    }

    /**
     * 同步
     *
     * @param accountBean
     */
    public void saveUserSync(AccountBean accountBean) {
        super.saveUserSync(accountBean);
        this.account = accountBean;
    }
}
