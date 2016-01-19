package com.woyaofa.ui.mine;

import android.os.Bundle;
import android.view.View;

import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.util.NavUtil;

import butterknife.OnClick;

/**
 * 账户与安全
 */
public class SafeActivity extends MBaseActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_safe);
    }

    @OnClick(R.id.activity_safe_iv_find_pwd)
    public void changePwd(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(RegisterActivity.BUNDLE_KEY_ACTION, RegisterActivity.KEY_ACTION_CHANGE);
        bundle.putString(RegisterActivity.BUNDLE_KEY_TITLE, "修改密码");
        NavUtil.goToNewAct(this, RegisterActivity.class, bundle);
    }

    @OnClick(R.id.activity_safe_iv_change_phone)
    public void changePhone(View v) {
        NavUtil.goToNewAct(this, ChangePhoneActivity.class);
    }

}
