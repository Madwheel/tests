package com.iwz.WzFramwork.mod.tool.webview.model;


import com.iwz.WzFramwork.base.interfaces.IMyEvent;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/11
 */
public class EWebviewDestory implements IMyEvent {
    @Override
    public String getName() {
        return getEventName();
    }

    public static String getEventName() {
        return "EWebviewDestory";
    }

    String url;

    public EWebviewDestory() {
        this.url = "";
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
