package com.iwz.WzFramwork.mod.tool.webview.serv;

import android.os.Build;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.api.ServApi;
import com.iwz.WzFramwork.mod.tool.webview.MyWebviewMain;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/04
 */
public class MyWebviewServApi extends ServApi {

    public MyWebviewServApi(MyWebviewMain main) {
        super(main);
    }

    private static MyWebviewServApi mBizWebviewServApi;

    public static MyWebviewServApi getInstance(MyWebviewMain main) {
        if (mBizWebviewServApi == null) {
            synchronized (MyWebviewServApi.class) {
                if (mBizWebviewServApi == null) {
                    mBizWebviewServApi = new MyWebviewServApi(main);
                }
            }
        }
        return mBizWebviewServApi;
    }

    public void cookieSync() {
        CookieSyncManager.createInstance(WzFramworkApplication.getmContext());
        if (Build.VERSION.SDK_INT >= 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }
}
