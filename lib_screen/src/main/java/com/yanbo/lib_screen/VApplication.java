package com.yanbo.lib_screen;

import android.content.Context;

import com.yanbo.lib_screen.manager.ClingManager;


/**
 * Created by lzan13 on 2018/3/15.
 */
public class VApplication {
    protected static Context mcontext;
    public static Context getContext() {
        return mcontext;
    }
    public static void init(Context context) {
        mcontext=context;
        ClingManager.getInstance().startClingService();
    }
}
