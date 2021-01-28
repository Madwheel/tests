package com.iwz.WzFramwork.mod.bus.event.serv;

import android.os.Looper;

import com.iwz.WzFramwork.mod.bus.event.BusEventMain;
import com.iwz.WzFramwork.base.api.ServApi;
import com.iwz.WzFramwork.mod.tool.webview.interfaces.IValueCallback;
import com.iwz.WzFramwork.mod.tool.webview.view.MyWebview;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/09/27
 */
public class BusEventServApi extends ServApi {

    public BusEventServApi(BusEventMain main) {
        super(main);
    }

    private static BusEventServApi mBusEventServApi;

    public static BusEventServApi getInstance(BusEventMain main) {
        if (mBusEventServApi == null) {
            synchronized (BusEventServApi.class) {
                if (mBusEventServApi == null) {
                    mBusEventServApi = new BusEventServApi(main);
                }
            }
        }
        return mBusEventServApi;
    }

    @Override
    public void born() {
        super.born();
    }

    public void jsExecute(final MyWebview webView, final String jsCode) {
        //回到ui主线程
        webView.post(new Runnable() {
            @Override
            public void run() {
                d("isMainThread", "" + (Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId()));
                d("jscode", jsCode);
                webView.evaluateJavascript(jsCode, new IValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        d("callback", s);
                    }
                });
            }
        });
    }

    //获取H5推送消息的id
    private static int pushID = 999;

    public int getPushID() {
        if (pushID < 10000) {
            pushID += 1;
        }
        return pushID;
    }
}
