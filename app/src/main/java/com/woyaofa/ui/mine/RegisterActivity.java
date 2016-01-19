package com.woyaofa.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
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
import com.woyaofa.bean.UserBean;
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
 * 注册和修改密码
 */
public class RegisterActivity extends MBaseActivity {
    public static String BUNDLE_KEY_ACTION = "action";
    public static String BUNDLE_KEY_TITLE = "title";
    public static String KEY_ACTION_REGISTER = "register";
    public static String KEY_ACTION_CHANGE = "change";

    @Bind(R.id.activity_register_et_code)
    EditText etCode;

    @Bind(R.id.activity_register_et_confirm_pwd)
    EditText etConfirmPwd;

    @Bind(R.id.activity_register_tv_get_code)
    TextView tvGetCode;

    @Bind(R.id.activity_register_et_phone)
    EditText etPhone;

    @Bind(R.id.activity_register_et_pwd)
    EditText etPwd;

    @Bind(R.id.activity_register_tv_submit)
    TextView tvSubmit;

    private String code;
    private String phone;
    private String pwd;
    private String comfirmPwd;
    private String title;
    private String action;
    private EventHandler eh;
    private String country;
    private Timer timer;
    private int count;//获取验证码的时间(秒)

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_register);
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        action = bundle.getString(BUNDLE_KEY_ACTION);
        title = bundle.getString(BUNDLE_KEY_TITLE);
    }

    private void init() {
        hvHead.setTitle(title);
        if (KEY_ACTION_CHANGE.equals(action)) {
            tvSubmit.setText("提交");
        } else if (KEY_ACTION_REGISTER.equals(action)) {
            tvSubmit.setText("注册");
        }

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
                                ToastUtil.toastAlways(RegisterActivity.this, "提交验证码成功");
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

    @OnClick(R.id.activity_register_tv_get_code)
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
        if (KEY_ACTION_REGISTER.equals(action)) {
            params.add(ApiAccount.TYPE_CODE_REGISTER);
        } else if (KEY_ACTION_CHANGE.equals(action)) {
            params.add(ApiAccount.TYPE_CODE_CHANGE_PWD);
        }
        ApiAccount.getInstance().getPhone(this, params);

    }

    @OnClick(R.id.activity_register_tv_submit)
    public void onSubmit(View v) {
        code = etCode.getText().toString();
        phone = etPhone.getText().toString();
        pwd = etPwd.getText().toString();
        comfirmPwd = etConfirmPwd.getText().toString();
        if (StringUtil.isEmpty(phone)) {
            ToastUtil.toastAlways(this, "手机不能为空哦！");
            return;
        } else if (Config.PATTERN_PHONE_NUMBER.matches(phone)) {
            ToastUtil.toastAlways(this, "手机号格式不对！");
            return;
        } else if (StringUtil.isEmpty(code)) {
            ToastUtil.toastAlways(this, "验证码不能为空哦！");
            return;
        } else if (StringUtil.isEmpty(pwd)) {
            ToastUtil.toastAlways(this, "密码不能为空哦！");
            return;
        } else if (Config.PATTERN_PASSWORD.matches(pwd)) {
            ToastUtil.toastAlways(this, "密码格式不对哦！");
            return;
        } else if (StringUtil.isEmpty(comfirmPwd)) {
            ToastUtil.toastAlways(this, "请确认密码哦！");
            return;
        } else if (!comfirmPwd.equals(pwd)) {
            ToastUtil.toastAlways(this, "两次密码不同哦！");
            return;
        }
        RequestBody body = new FormEncodingBuilder().add("name", phone)
                .add("password", MD5Util.md5(pwd))
                .add("code", code)
                .add("appkey", Config.SMSSDK_KEY)
                .build();
//        SMSSDK.submitVerificationCode("86", phone, code);
        if (KEY_ACTION_REGISTER.equals(action)) {
            ApiAccount.getInstance().postRegister(RegisterActivity.this, body);
        } else if (KEY_ACTION_CHANGE.equals(action)) {
            ApiAccount.getInstance().postChangePwd(RegisterActivity.this, body);
        }
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
            tvGetCode.setClickable(true);
            tvGetCode.setText("获取验证码");
            timer.cancel();
        } else {
            tvGetCode.setClickable(false);
            tvGetCode.setText("" + count);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eh != null) {
            SMSSDK.unregisterEventHandler(eh); //注册短信回调
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void setRequestSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        if (url.equals(ApiAccount.URL_REGISTER)) {
            AccountBean accountBean = new AccountBean();
            accountBean.setPassword(pwd);
            accountBean.setName(phone);
            accountBean.setUser(new UserBean());
            MApplication.getApp().saveUserSync(accountBean);
            MApplication.getApp().requestOnBack();
            updateView(new Runnable() {
                @Override
                public void run() {
//                    MApplication.getApp().updateActivities();
                    ToastUtil.toast(getBaseContext(), jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
                    onBack(null);
                }
            });
        } else if (url.equals(ApiAccount.URL_PHONE)) {
            SMSSDK.getVerificationCode("86", phone);
        }
    }

    @Override
    public void setRequestNotSuc(final String url, String statusCode, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, jo);
        updateView(new Runnable() {
            @Override
            public void run() {
                if (url.equals(ApiAccount.URL_REGISTER)) {
                    ToastUtil.toastAlways(RegisterActivity.this, "注册失败了啊，请重新试试吧！");
                } else if (url.equals(ApiAccount.URL_PHONE)) {
                    ToastUtil.toastAlways(RegisterActivity.this, "获取验证码失败了，请重新试试吧！");
                } else if (url.equals(ApiAccount.URL_CHANGE_PWD)) {
                    ToastUtil.toastAlways(RegisterActivity.this, "修改密码失败，请重新试试吧！");
                }
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
