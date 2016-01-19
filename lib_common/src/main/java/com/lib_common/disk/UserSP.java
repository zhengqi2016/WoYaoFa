package com.lib_common.disk;

import android.content.Context;
import android.content.SharedPreferences;

import com.lib_common.bean.UserBean;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;

public class UserSP {
    private SharedPreferences sharedPreferences;

    public static String KEY_USER_INFO = "userInfo";

    public UserSP(Context context) {
        sharedPreferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void saveUserBean(UserBean userbean) {
        ToastUtil.log("UserSP", "--> " + GsonUtil.getInstance().toJsonString(userbean));
        sharedPreferences.edit()
                .putString(KEY_USER_INFO, GsonUtil.getInstance().toJsonString(userbean))
                .commit();
    }

    public <T> T getUserBean(Class<T> t) {
        String str = sharedPreferences.getString(KEY_USER_INFO, null);
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        return GsonUtil.getInstance().toJsonObj(str, t);
    }

    public void clear() {
        sharedPreferences.edit().remove(KEY_USER_INFO).commit();
    }
}
