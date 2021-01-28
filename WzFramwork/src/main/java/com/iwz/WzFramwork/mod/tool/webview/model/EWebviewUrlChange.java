package com.iwz.WzFramwork.mod.tool.webview.model;


import com.iwz.WzFramwork.base.interfaces.IMyEvent;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/11
 */
public class EWebviewUrlChange implements IMyEvent {
    @Override
    public String getName() {
        return getEventName();
    }

    public static String getEventName() {
        return "EWebviewUrlChange";
    }

    String url;
    String oldUrl;

    public EWebviewUrlChange() {
        this.url = "";
        this.oldUrl="";
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOldUrl() {
        return oldUrl;
    }

    public void setOldUrl(String oldUrl) {
        this.oldUrl = oldUrl;
    }
}