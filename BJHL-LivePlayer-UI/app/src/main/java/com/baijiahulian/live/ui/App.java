package com.baijiahulian.live.ui;

import android.app.Application;

/**
 * Created by Shubo on 2017/4/21.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // catch捕获的异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);

//        LiveSDK.init(LPConstants.LPDeployType.Test);
    }
}
