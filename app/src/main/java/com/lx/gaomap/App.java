package com.lx.gaomap;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.lx.gaomap.utils.AppUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import cn.jpush.android.api.JPushInterface;


public class App extends Application {


    public static boolean isDebug = true;


    private static App instance;
    private static final String TAG = "MyApplication";

    public static App getInstance() {
        return instance;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    public static Context mContext;
    public static App context;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        SPTool.init(mContext, AppUtils.getAppName(this));
        // 主要是添加下面这句代码
        MultiDex.install(this); //65536

        context = (App) getApplicationContext();
        //FrescoImageLoader.init(this, android.R.color.black);
        //极光
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        String registrationID = JPushInterface.getRegistrationID(this);
        SPTool.addSessionMap(AppSP.JupshID, registrationID);
        Log.i(TAG, "onCreate:极光信息 " + registrationID);


        //设置组件化的Log开关  参数: boolean 默认为false，如需查看LOG设置为true
        UMConfigure.setLogEnabled(true);
        //设置日志加密  参数：boolean 默认为false（不加密）
        UMConfigure.setEncryptEnabled(false);

        //初始化友盟,Key在清单文件写过这里就不在需要了,推送不需要传入空字符串
        UMConfigure.init(this, "33665555__KEY", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        //三方登录
        PlatformConfig.setWeixin("wx91781d2b122__KEY", "926627c1ac851f6ef0428da__KEY");
        PlatformConfig.setQQZone("1110049__KEY", "NbnAvFzVYpJo__KEY");//
        PlatformConfig.setSinaWeibo("353419__KEY", "a091c5a7c817086d9c7b1b__KEY", "https://api.weibo.com/oauth2/default.html");
        //腾讯Bugly手机错误日志
        CrashReport.initCrashReport(getApplicationContext(), "15a2d4f__KEY", true);//正确的

    }


}
