package com.iwz.WzFramwork.mod.tool.webview.interfaces;

import java.util.HashMap;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/10/1013:54
 * desc   :
 */
public interface IX5WebViewHeadHook {
    HashMap<String, String> postExtraHeaders(String host, HashMap<String, String> params);
}
