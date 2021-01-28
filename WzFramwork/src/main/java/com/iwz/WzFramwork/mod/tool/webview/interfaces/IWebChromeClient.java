package com.iwz.WzFramwork.mod.tool.webview.interfaces;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/10/1015:58
 * desc   :
 */
public interface IWebChromeClient {
    void onReceivedTitle(String wvTitle);

    void onProgressChanged(String title, int newProgress);

    void startActivity(Intent intent, int code);
}
