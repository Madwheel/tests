package com.iwz.WzFramwork.mod.net.http.model;



import com.iwz.WzFramwork.base.CommonRes;

import java.util.Map;

import okhttp3.HttpUrl;

public interface IWzHttpReqHook<T> {
    Map<String, String> prepostParams(String host, Map<String, String> params);

    Map<String, String> postParams(String host, Map<String, String> params);

    Map<String, String> prepostExtraHeaders(String host, Map<String, String> params);

    Map<String, String> postExtraHeaders(String host, Map<String, String> params);

    <T> CommonRes<T> postRes(String host, Map<String, String> params, CommonRes<T> cRes);

    <T> CommonRes<T> prepostRes(String host, Map<String, String> params, CommonRes<T> cRes);
}
