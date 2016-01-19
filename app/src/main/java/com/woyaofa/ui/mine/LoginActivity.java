package com.woyaofa.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MD5Util;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.MApplication;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiAccount;
import com.woyaofa.bean.AccountBean;
import com.woyaofa.config.Config;
import com.woyaofa.util.NavUtil;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends MBaseActivity {

    @Bind(R.id.activity_login_et_phone)
    EditText etPhone;

    @Bind(R.id.activity_login_et_pwd)
    EditText etPwd;

    @Bind(R.id.activity_login_tv_register)
    TextView tvRegister;

    @Bind(R.id.activity_login_ll_content)
    LinearLayout llContent;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_login);
        controlKeyboardLayout(llContent, tvRegister);
    }

    @OnClick(R.id.activity_login_tv_login)
    public void onLogin(View v) {
        String phone = etPhone.getText().toString();
        String pwd = etPwd.getText().toString();
        if (StringUtil.isEmpty(phone)) {
            ToastUtil.toastAlways(this, "登录名不能为空！");
            return;
        } else if (StringUtil.isEmpty(pwd)) {
            ToastUtil.toastAlways(this, "密码不能为空哦！");
            return;
        } else if (Config.PATTERN_PASSWORD.matches(pwd)) {
            ToastUtil.toastAlways(this, "密码格式不对哦！");
            return;
        }
        RequestBody body = new FormEncodingBuilder()
                .add("username", phone).add("password", MD5Util.md5(pwd)).build();
        ApiAccount.getInstance().postLogin(this, body);
        showLoading();
    }

    @OnClick(R.id.activity_login_tv_register)
    public void onRegister(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(RegisterActivity.BUNDLE_KEY_TITLE, "注册");
        bundle.putString(RegisterActivity.BUNDLE_KEY_ACTION, RegisterActivity.KEY_ACTION_REGISTER);
        NavUtil.goToNewAct(this, RegisterActivity.class, bundle);
        onBack(v);
    }

    @OnClick(R.id.activity_login_tv_forget_pwd)
    public void onForgetPwd(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(RegisterActivity.BUNDLE_KEY_TITLE, "修改密码");
        bundle.putString(RegisterActivity.BUNDLE_KEY_ACTION, RegisterActivity.KEY_ACTION_CHANGE);
        NavUtil.goToNewAct(this, RegisterActivity.class, bundle);
        onBack(v);
    }

    @OnClick(R.id.activity_login_rl_back)
    @Override
    public void onBack(View v) {
        super.onBack(v);
    }

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        AccountBean accountBean = GsonUtil.getInstance().toJsonObj(jo.getAsJsonObject(Api.KEY_DATA), AccountBean.class);
        accountBean.setPassword(etPwd.getText().toString());
        MApplication.getApp().saveUserSync(accountBean);
        updateView(new Runnable() {
            @Override
            public void run() {
                MApplication.getApp().updateActivities();
            }
        });
        onBack(null);
    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, jo);
        updateView(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastAlways(LoginActivity.this, "登录失败，请重新尝试！");
            }
        });
    }

    @Override
    public void setRequestFinish(String url) {
        super.setRequestFinish(url);
        updateView(new Runnable() {
            @Override
            public void run() {
                hideLoading();
            }
        });
    }
}
