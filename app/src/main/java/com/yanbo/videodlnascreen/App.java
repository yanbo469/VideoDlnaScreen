package com.yanbo.videodlnascreen;

import android.app.Application;
import android.content.Context;

import com.yanbo.lib_screen.VApplication;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 2018/11/6
 */
public class App extends Application {

    protected static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        VApplication.init(this);
        context = this;
    }

    public static Context getContext() {
        return context;
    }


}
