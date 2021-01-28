package com.iwz.WzFramwork.mod;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/9/2910:39
 * desc   :
 */
public class MyHandlerThread extends HandlerThread {

    Handler mHandler;

    public MyHandlerThread(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // process incoming messages here
                // this will run in non-ui/background thread
            }
        };
    }

    public Handler getHandler() {
        return mHandler;
    }
}
