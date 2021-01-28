package com.iwz.WzFramwork.mod.bus.event.control;

import android.annotation.SuppressLint;
import android.webkit.JavascriptInterface;

import com.iwz.WzFramwork.mod.bus.event.BusEventMain;
import com.iwz.WzFramwork.base.MyObject;

import java.util.Map;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/14
 */
public class WZAppShareJsInterface extends MyObject {
    private BusEventMain mMain;
    private Map<String, String> shareMap;

    public WZAppShareJsInterface(Map<String, String> shareMap) {
        this.shareMap = shareMap;
        mMain = BusEventMain.getInstance();
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void getMeta(String meta) {
        if (shareMap != null && meta != null && meta.indexOf("=") != -1) {
            int index = meta.indexOf("=");
            String key = meta.substring(0, index);
            String value = meta.substring(index + 1);
            shareMap.put(key, value);
        }
    }
}
