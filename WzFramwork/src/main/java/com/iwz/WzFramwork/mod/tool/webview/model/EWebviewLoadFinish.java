package com.iwz.WzFramwork.mod.tool.webview.model;


import com.iwz.WzFramwork.base.interfaces.IMyEvent;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/15
 */
public class EWebviewLoadFinish implements IMyEvent {
    @Override
    public String getName() {
        return getEventName();
    }

    public static String getEventName() {
        return "EWebviewLoadFinish";
    }
}
