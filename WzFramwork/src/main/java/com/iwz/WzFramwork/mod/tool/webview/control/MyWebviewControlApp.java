package com.iwz.WzFramwork.mod.tool.webview.control;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.tool.webview.MyWebviewMain;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/10/1013:49
 * desc   :
 */
public class MyWebviewControlApp extends ControlApp {
    private MyWebviewMain mMain;

    protected MyWebviewControlApp(MyWebviewMain main) {
        super(main);
        mMain = main;
    }

    private static MyWebviewControlApp mMyWebviewControlApp = null;

    public static MyWebviewControlApp getInstance(MyWebviewMain main) {
        synchronized (MyWebviewControlApp.class) {
            if (mMyWebviewControlApp == null) {
                mMyWebviewControlApp = new MyWebviewControlApp(main);
            }
        }
        return mMyWebviewControlApp;
    }

    @Override
    public void born() {
        super.born();
        initX5Env();
    }

    private void initX5Env() {
        //https://x5.tencent.com/tbs/guide/sdkInit.html

        QbSdk.setDownloadWithoutWifi(true);//非wifi条件下允许下载X5内核

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                d("tbstbs onViewInitFinished is BOOL " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                d("tbstbs onCoreInitFinished");
            }
        };

        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                d("tbstbs onDownloadFinish " + i);
            }

            @Override
            public void onInstallFinish(int i) {
                d("tbstbs onInstallFinish " + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                d("tbstbs onDownloadProgress " + i);
            }
        });

        //x5内核初始化接口
        QbSdk.initX5Environment(WzFramworkApplication.getmContext(), cb);
    }

    /**
     * 同步cookie到本地
     */
    public void cookieSync() {
        mMain.pServApi.cookieSync();
    }
}
