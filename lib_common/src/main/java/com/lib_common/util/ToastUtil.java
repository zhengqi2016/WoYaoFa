package com.lib_common.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lib_common.BaseConfig;

/**
 * 提示工具
 *
 * @author m
 */
public class ToastUtil {
    private static Toast toast;

    public static void printErr(Exception e) {
        if (BaseConfig.IS_DEBUG) {
            e.printStackTrace();
        }
    }

    public static void log(String tag, Object text) {
        if (BaseConfig.IS_DEBUG) {
            if (text == null) {
                Log.i(tag, "" + null);
            } else {
                Log.i(tag, text.toString());
            }

        }
    }

    public static void logE(String tag, Object text) {
        if (BaseConfig.IS_DEBUG) {
            Log.e(tag, text.toString());
        }
    }

    public static void toast(Context context, Object text) {
        if (BaseConfig.IS_DEBUG) {
            toastAlways(context, text);
        }
    }

    public static void toastAlways(Context context, Object text) {
        Toast.makeText(context, text.toString(), Toast.LENGTH_SHORT).show();
    }
}
