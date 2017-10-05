package com.prostage.l_pha.dental_user.common;

import android.app.Application;

/**
 * Created by USER on 19-Apr-17.
 */

public class MyApplication extends Application {
    private static boolean sIsChatActivityOpen = false;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        MyApplication.sIsChatActivityOpen = isChatActivityOpen;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}