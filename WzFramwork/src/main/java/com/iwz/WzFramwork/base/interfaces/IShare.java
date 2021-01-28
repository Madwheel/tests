package com.iwz.WzFramwork.base.interfaces;

import android.app.Activity;

import com.iwz.WzFramwork.mod.tool.webview.view.MyWebview;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/12/109:59
 * desc   :
 */
public interface IShare {
    void doShareTo(final Activity activity, final MyWebview webView);
}
