package com.iwz.WzFramwork.mod.bus.event.model;

import com.iwz.WzFramwork.mod.tool.webview.view.MyWebview;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/11
 */
public class JsExecute {
    String name;
    String content;
    MyWebview webView;

    public JsExecute(String name, String content, MyWebview webView) {
        this.name = name;
        this.content = content;
        this.webView = webView;
    }

    public MyWebview getWebView() {
        return webView;
    }

    public void setWebView(MyWebview webView) {
        this.webView = webView;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
