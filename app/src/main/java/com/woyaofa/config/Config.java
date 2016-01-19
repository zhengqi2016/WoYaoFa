package com.woyaofa.config;

import com.lib_common.BaseConfig;

/**
 * Created by LoaR on 15/11/3.
 */
public class Config extends BaseConfig {
    public static final String SMSSDK_KEY = "d1ed6f148024";
    public static final String SMSSDK_SECRET = "a774daa7da5fcab5593fc71e4d078931";

    // ======================begin=====密码姓名正则 ============================
    public static final String PATTERN_NAME = "^[0-9a-zA-Z\u4e00-\u9fa5]{2,11}$";
    public static final String PATTERN_ACCOUNT_NAME = "^[a-zA-Z][0-9a-zA-Z]{5,14}$";
    public static final String PATTERN_NICKNAME = "^[0-9a-zA-Z\u4e00-\u9fa5]{1,10}$";
    public static final String PATTERN_PASSWORD = "^[0-9a-zA-Z]{6,18}$";
    public static final String PATTERN_PHONE_NUMBER = "^((\\+{0,1}86){0,1})((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";
    // ======================end=====密码姓名正则 ============================

}
