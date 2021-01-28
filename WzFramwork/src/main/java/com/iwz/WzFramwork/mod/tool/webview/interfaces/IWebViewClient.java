package com.iwz.WzFramwork.mod.tool.webview.interfaces;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.iwz.WzFramwork.mod.tool.common.UrlParse;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/10/1014:58
 * desc   :
 */
public interface IWebViewClient {
    boolean shouldOverrideUrlLoading(View view, String url);

    void onPageFinished(String url);

    void doUpdateVisitedHistory(String s, boolean b);

    void onPageStarted(String url, Bitmap bitmap);

    void onReceivedError(boolean isForMainFrame);
}
