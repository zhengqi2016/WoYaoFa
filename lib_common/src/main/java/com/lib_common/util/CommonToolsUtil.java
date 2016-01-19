/**
 * www.mbaobao.com Inc.
 * Copyright (c) 2013 All Rights Reserved.
 */
package com.lib_common.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.lib_common.bean.DeviceInfoBean;

public class CommonToolsUtil {

    /**
     * 获取手机设备信息实体类
     *
     * @param context
     * @param accountId 用户的id
     * @return
     */
    public static DeviceInfoBean getDeviceInfo(Context context, String accountId) {
        DeviceInfoBean result = new DeviceInfoBean();
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        result.setAccountId(accountId);
        result.setCurrentVersion(getVersionName(context));
        result.setDeviceId(tm.getDeviceId());
        result.setDeviceSoftwareVersion(tm.getDeviceSoftwareVersion());
        result.setLastLoginPlatform("android");
        result.setMac(wifi.getConnectionInfo().getMacAddress());
        result.setOSRelease(android.os.Build.VERSION.RELEASE);
        result.setPhoneModel(android.os.Build.MODEL);
        result.setSubscriberId(tm.getSubscriberId());
        return result;
    }

    /**
     * 获取app的版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String result = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            result = pInfo.versionName;
        } catch (NameNotFoundException e) {
            ToastUtil.printErr(e);
        }
        return result;
    }
}
