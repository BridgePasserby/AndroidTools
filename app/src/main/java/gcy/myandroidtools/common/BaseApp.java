package gcy.myandroidtools.common;

/**
 * Copyright (c) 2018, 北京视达科科技有限责任公司 All rights reserved.
 * author：chongyang.gao
 * date：2018/1/31
 * description：
 */

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class BaseApp extends Application {
    private static BaseApp application;
    private static int mainTid;
    private static Handler handler;

    @Override
//  在主线程运行的
    public void onCreate() {
        super.onCreate();
        application = this;
        mainTid = android.os.Process.myTid();
        handler = new Handler();
    }

    public static Context getApplication() {
        return application;
    }

    public static int getMainTid() {
        return mainTid;
    }

    public static Handler getHandler() {
        return handler;
    }


}

