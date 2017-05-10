package com.baijia.live.app;

import android.app.Application;

import com.baijiahulian.livecore.LiveSDK;
import com.baijiahulian.livecore.context.LPConstants;

/**
 * Created by Shubo on 2017/4/21.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // catch捕获的异常
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
//        Thread.setDefaultUncaughtExceptionHandler(crashHandler);

        LiveSDK.init(LPConstants.LPDeployType.Test);
    }
}
