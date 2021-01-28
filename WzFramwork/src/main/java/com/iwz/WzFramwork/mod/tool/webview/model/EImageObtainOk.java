package com.iwz.WzFramwork.mod.tool.webview.model;


import com.iwz.WzFramwork.base.interfaces.IMyEvent;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/24
 */
public class EImageObtainOk implements IMyEvent {
    private String url;
    private static String mEventName = "BizWebviewImageObtainOk";

    public static String getEventName() {
        return mEventName;
    }

    @Override
    public String getName() {
        return getEventName();
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void resetUrl() {
        this.url = "";
    }
}
