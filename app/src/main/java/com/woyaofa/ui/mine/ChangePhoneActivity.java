package com.woyaofa.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 更换手机
 */
public class ChangePhoneActivity extends MBaseActivity {

    @Bind(R.id.activity_change_phone_et_code)
    EditText etCode;

    @Bind(R.id.activity_change_phone_tv_get_code)
    TextView tvGetCode;

    @Bind(R.id.activity_change_phone_et_phone)
    EditText etPhone;

    @Bind(R.id.activity_change_phone_tv_submit)
    TextView tvSubmit;

    @Bind(R.id.activity_change_phone_tv_phone)
    TextView tvPhone;

    private String code;
    private String phone;

    private EventHandler eh;
    private Timer timer;
    private int count;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_change_phone);
        init();
    }

    private void init() {
        tvPhone.setText("当前绑定的手机号:" + MApplication.getApp().getAccount().getName());
        SMSSDK.initSDK(this, Config.SMSSDK_KEY, Config.SMSSDK_SECRET);
        eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        updateView(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toastAlways(ChangePhoneActivity.this, "提交验证码成功");
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        getTime();
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
//                        HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//                        country = (String) phoneMap.get("country");
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    @OnClick(R.id.activity_change_phone_tv_get_code)
    public void onGetCode(View v) {
        phone = etPhone.getText().toString();
        if (phone == null) {
            ToastUtil.toastAlways(this, "手机不能为空哦！");
            return;
        } else if (Config.PATTERN_PHONE_NUMBER.matches(phone)) {
            ToastUtil.toastAlways(this, "手机号格式不对！");
            return;
        }
        List<Object> params = new ArrayList<>();
        params.add(phone);
        ApiAccount.getInstance().getPhone(this, params);
    }

    @OnClick(R.id.activity_change_phone_tv_submit)
    public void onSubmit(View v) {
        code = etCode.getText().toString();
        phone = etPhone.getText().toString();
        if (phone == null) {
            ToastUtil.toastAlways(this, "手机不能为空哦！");
            return;
        } else if (Config.PATTERN_PHONE_NUMBER.matches(phone)) {
            ToastUtil.toastAlways(this, "手机号格式不对！");
            return;
        } else if (code == null) {
            ToastUtil.toastAlways(this, "验证码不能为空哦！");
            return;
        }
        RequestBody body = new FormEncodingBuilder()
                .add("code", code)
                .add("phone", phone)
                .add("appkey", Config.SMSSDK_KEY)
                .add("accountId", MApplication.getApp().getAccountWithLogin().getId() + "")
                .build();
        ApiAccount.getInstance().postChangePhone(this, body);
        showLoading();
    }

    private void getTime() {
        count = 60;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                updateView(new Runnable() {
                    @Override
                    public void run() {
                        count--;
                        setGetCodeState(count);
                    }
                });
            }
        }, 0, 1000);
    }

    protected void setGetCodeState(int count) {
        if (count <= 0) {
            tvGetCode.setClickable(false);
            tvGetCode.setText("获取验证码");
            timer.cancel();
        } else {
            tvGetCode.setClickable(true);
            tvGetCode.setText("" + count);
        }
    }

    @Override
    public void setRequestSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        if (url.equals(ApiAccount.URL_CHANGE_PHONE)){
            AccountBean account = MApplication.getApp().getAccount();
            account.setName(phone);
            account.getUser().setPhone(phone);
            MApplication.getApp().saveUserAsyn(account);
            updateView(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastAlways(ChangePhoneActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
                    onBack(null);
                }
            });
        }else if (url.equals(ApiAccount.URL_PHONE)){
            SMSSDK.getVerificationCode("86", phone);
        }
    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, jo);
        updateView(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastAlways(ChangePhoneActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eh != null) {
            SMSSDK.unregisterEventHandler(eh); //注册短信回调
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
