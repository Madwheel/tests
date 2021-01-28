package com.iwz.WzFramwork.mod.net.http.interfaces;

import java.util.Map;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/9/2917:29
 * desc   :
 */
public interface INetHttp {
    void reqGetResByWzApi(String url, Map<String, String> params, WzNetCallback callback);
}

