package com.woyaofa.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.woyaofa.R;
import com.woyaofa.ui.main.MainActivity;
import com.woyaofa.util.NavUtil;

/**
 * 扉页、欢迎页
 *
 * @author loar
 */
public class WelcomeActivity extends Activity {

    private final String IS_FIRST_IN = "isFirstIn";

    private Handler handler = new Handler();

    private SharedPreferences preferences;

    private boolean isFirstIn;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init() {
//		MImageLoader.displayWithDefaultOptions(this, "" + R.drawable.welcome,
//				ivLogo);
        // 使用SharedPreferences来记录程序的使用次数
        preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = preferences.getBoolean(IS_FIRST_IN, true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                NavUtil.goToNewAct(WelcomeActivity.this, MainActivity.class);
                finish();
//				if (isFirstIn) {
//					preferences.edit().putBoolean(IS_FIRST_IN, false).commit();
//				} else {
//
//				}
            }
        }, 2000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
