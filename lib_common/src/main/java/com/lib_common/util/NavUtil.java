package com.lib_common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.lib_common.BaseConfig;

public class NavUtil {
    /**
     * 不知道写来干嘛的
     *
     * @param context
     * @param classOfT
     */
    public static <T> void goToTAct(Context context, Class<T> classOfT) {
        Intent intent = new Intent(context, classOfT);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 界面跳转带参数
     *
     * @param context
     * @param cla
     * @param bundle
     */
    public static <T> void goToNewActForResult(Activity context, Class<T> cla,
                                               Bundle bundle, int requestCode) {
        ToastUtil.log("goToNewAct", context.getClass().getCanonicalName()
                + " ==> " + cla.getCanonicalName());
        Intent intent = new Intent(context, cla);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 界面跳转带参数
     *
     * @param context
     * @param cla
     * @param bundle
     */
    public static <T> void goToNewAct(Context context, Class<T> cla,
                                      Bundle bundle) {
        ToastUtil.log("goToNewAct", context.getClass().getCanonicalName()
                + " ==> " + cla.getCanonicalName());
        Intent intent = new Intent(context, cla);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 界面跳转不带参数
     *
     * @param context
     * @param cla
     */
    public static <T> void goToNewAct(Context context, Class<T> cla) {
        goToNewAct(context, cla, null);
    }

    /**
     * 调用图片裁剪功能
     *
     * @param act
     * @param url
     * @param requestCode
     * @return 裁剪后保存的uri
     */
    public static Uri goToPicCrop(Activity act, String url, int requestCode) {
        Uri tempUri = Uri.fromFile(FileUtil.initFile(
                BaseConfig.DEFAULT_DIR_NAME, "temp.jpg"));
        int w = DensityUtil.getScreenW(act);
        Uri mUri = Uri.parse(url);
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(mUri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", w);// 输出图片大小
        intent.putExtra("outputY", w);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        act.startActivityForResult(intent, requestCode);
        return tempUri;
    }

    public static void callPhone(Context act, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(uri);
        act.startActivity(intent);
    }

    public static void callMap(Context act, String geo) {
        Uri uri = Uri.parse("geo:" + geo);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        act.startActivity(intent);
    }

}
