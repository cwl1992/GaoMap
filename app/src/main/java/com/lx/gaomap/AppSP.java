package com.lx.gaomap;

import android.util.Log;

import java.util.Random;

public class AppSP {

    public static final String TAG = "YunDongSP";
    public static final String sCity = "sCity";
    public static final String sStringJ = "sStringJ";
    public static final String sStringW = "sStringW";
    public static final String JupshID = "JupshID";
    public static final String isLogin = "isLogin";



    public static final int PMS_LOCATION = 1003;// 权限编号，自定义----定位  打电话
    public static final int PMS_CALL_PHONE = 1004;// 权限编号，打电话
    public static final int PMS_CALL_PHONE1 = 1002;// 权限编号，打电话
    public static final int PMS_CAMERA = 1005;// 权限编号，自定义----相机




    public static String getNum() {
        StringBuilder sb = new StringBuilder();
        //随机生成6位数  发送到聚合
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int a = random.nextInt(10);
            sb.append(a);
        }
        Log.i(TAG, "getNum:  随机数" + sb.toString());
        return sb.toString();
    }


    /**
     * 获取随机验证码
     */
    public static String getNum6() {
        StringBuilder sb = new StringBuilder();
        //随机生成6位数  发送到聚合
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int a = random.nextInt(10);
            sb.append(a);
        }
        Log.i(TAG, "getNum:  随机数" + sb.toString());
        return sb.toString();
    }
}
