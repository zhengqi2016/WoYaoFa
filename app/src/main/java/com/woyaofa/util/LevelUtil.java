package com.woyaofa.util;

/**
 * Created by LoaR on 15/12/30.
 */
public class LevelUtil {
    /**
     * 积分是成交量*10，成交量是已完成的订单量，10积分是一级，20积分是二级，40积分是三级，80积分是四级，160积分是五级，
     * 320积分是六级，640积分是七级，1280积分是八级，2560积分是九级，5120积分是十级
     */
    public static String computeLevel(int credit) {
        if (credit < 10) {
            return "v0";
        } else if (credit < 20) {
            return "v1";
        } else if (credit < 40) {
            return "v2";
        } else if (credit < 80) {
            return "v3";
        } else if (credit < 160) {
            return "v4";
        } else if (credit < 320) {
            return "v5";
        } else if (credit < 640) {
            return "v6";
        } else if (credit < 1280) {
            return "v7";
        } else if (credit < 2560) {
            return "v8";
        } else if (credit < 5120) {
            return "v9";
        } else {
            return "v10";
        }
    }
}
