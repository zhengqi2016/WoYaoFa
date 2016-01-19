package com.lib_common;

/**
 * Created by LoaR on 15/11/3.
 */
public class BaseConfig {

    public static final String PLATFORM = "android";
    public static final boolean IS_DEBUG = true;
    /**
     * 手机sd卡根目录
     */
    public static final String ROOT_DIR = android.os.Environment
            .getExternalStorageDirectory().getPath();

    public static final String DIR_NAME = "/woyaofa";

    /**
     * 默认图片保存目录名
     */
    public static final String DEFAULT_DIR_NAME = ROOT_DIR + DIR_NAME + "/";
}
