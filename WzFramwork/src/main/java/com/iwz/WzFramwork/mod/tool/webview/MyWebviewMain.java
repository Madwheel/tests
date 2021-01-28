package com.iwz.WzFramwork.mod.tool.webview;

import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.tool.webview.control.MyWebviewControlApp;
import com.iwz.WzFramwork.mod.tool.webview.serv.MyWebviewServApi;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/10/1013:48
 * desc   :
 */
public class MyWebviewMain extends ModMain {
    private final MyWebviewControlApp pControl;
    public final MyWebviewServApi pServApi;

    @Override
    public String getModName() {
        return "MyWebviewMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_TOOL;
    }

    private static MyWebviewMain mMyWebviewMain = null;

    public static MyWebviewMain getInstance() {
        synchronized (MyWebviewMain.class) {
            if (mMyWebviewMain == null) {
                mMyWebviewMain = new MyWebviewMain();
            }
        }
        return mMyWebviewMain;
    }

    private MyWebviewMain() {
        pServApi = MyWebviewServApi.getInstance(this);
        pControl = MyWebviewControlApp.getInstance(this);
    }

    @Override
    public void born() {
        super.born();
        pServApi.born();
        pControl.born();
    }

    public MyWebviewControlApp getpControl() {
        return pControl;
    }
}
