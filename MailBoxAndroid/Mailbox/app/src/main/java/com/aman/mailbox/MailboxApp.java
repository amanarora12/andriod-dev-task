package com.aman.mailbox;

import android.app.Application;
import android.content.Context;

/**
 * Created by Aman on 19-08-2016.
 */
public class MailboxApp extends Application {
    private static MailboxApp mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }

    public static MailboxApp getInstance() {
        return mInstance;
    }

    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }
}
