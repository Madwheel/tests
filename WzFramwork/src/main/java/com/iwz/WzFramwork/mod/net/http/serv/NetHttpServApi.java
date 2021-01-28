package com.iwz.WzFramwork.mod.net.http.serv;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.iwz.WzFramwork.base.CommonRes;
import com.iwz.WzFramwork.base.JBase;
import com.iwz.WzFramwork.base.api.ServApi;
import com.iwz.WzFramwork.base.interfaces.IResCallback;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.net.core.NetCoreMain;
import com.iwz.WzFramwork.mod.net.http.interfaces.INetHttp;
import com.iwz.WzFramwork.mod.net.http.interfaces.WzNetCallback;
import com.iwz.WzFramwork.mod.net.http.model.IHttpNetCallback;
import com.iwz.WzFramwork.mod.net.http.model.IHttpReqHook;
import com.iwz.WzFramwork.mod.net.http.model.IWzHttpReqHook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class NetHttpServApi extends ServApi implements INetHttp {

    private static NetHttpServApi instance = null;
    private OkHttpClient mClient;

    protected NetHttpServApi(ModMain main) {
        super(main);
        mHttpReqHook = new ArrayList<>();
        mWzHttpReqHook = new ArrayList<>();
        mClient = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
    }

    public static NetHttpServApi getInstance(ModMain main) {
        if (instance == null) {
            instance = new NetHttpServApi(main);
        }
        return instance;
    }

    public String getHost(String url) {
        String host = url;
        HttpUrl parse = HttpUrl.parse(url);
        if (parse != null) {
            host = parse.host();
        }
        return host;
    }

    private List<IHttpReqHook> mHttpReqHook;
    private List<IWzHttpReqHook> mWzHttpReqHook;

    public void addWzHttpReqHook(IWzHttpReqHook hook) {
        mWzHttpReqHook.add(hook);
    }


    public <T extends JBase> void reqGetResByWzCms(Class<T> clazz, String path, Map<String, String> params, final IResCallback<T> callback) {
        reqGetRes(clazz, path, NetCoreMain.getInstance().getConf().getCmsHost(), params, callback);
    }

    public <T extends JBase> void reqGetResByWzApi(Class<T> clazz, String path, Map<String, String> params, final IResCallback<T> callback) {
        reqGetRes(clazz, path, NetCoreMain.getInstance().getConf().getApiHost(), params, callback);
    }

    public <T extends JBase> CommonRes<T> getGetResByWzApi(Class<T> clazz, String path, Map<String, String> params) {
        return getGetRes(clazz, path, NetCoreMain.getInstance().getConf().getApiHost(), params);
    }

    //下载文件

    @Override
    public void reqGetResByWzApi(String path, Map<String, String> params, final WzNetCallback callback) {
        reqGetResByWzApi(JBase.class, path, params, new IResCallback<JBase>() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onFinish(CommonRes<JBase> res) {
                callback.onResult(res.getResult());
            }
        });
    }

    public <T extends JBase> void reqGetRes(Class<T> clazz, String path, String url, Map<String, String> params, final IResCallback<T> callback) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        reqRes(clazz, path, params, callback, httpUrl, ERequest.GET);
    }

    public <T extends JBase> void reqPostRes(Class<T> clazz, String path, String url, Map<String, String> params, IResCallback<T> callback) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        reqRes(clazz, path, params, callback, httpUrl, ERequest.POST);
    }

    public <T extends JBase> CommonRes<T> getGetRes(Class<T> clazz, String path, String host, Map<String, String> params) {
        HttpUrl url = HttpUrl.parse(host);
        if (url == null) {
            return new CommonRes<>(false, "");
        }
        HttpUrl.Builder urlBuild = new HttpUrl.Builder()
                .scheme(url.scheme())
                .host(url.host())
                .addPathSegments(path);
        //param回调
        //预处理
        Map<String, String> extraHeaders = new HashMap<>();
        for (IWzHttpReqHook hook : mWzHttpReqHook) {
            System.out.println("hook");
            params = hook.prepostParams(url.host(), params);
            extraHeaders = hook.prepostExtraHeaders(url.host(), extraHeaders);
        }
        //再处理
        for (IWzHttpReqHook hook : mWzHttpReqHook) {
            System.out.println("hook");
            params = hook.postParams(url.host(), params);
            extraHeaders = hook.postExtraHeaders(url.host(), extraHeaders);
        }

        for (String key : params.keySet()) {
            urlBuild.addQueryParameter(key, params.get(key));
        }

        HttpUrl targetUrl = urlBuild.build();
        d("getGetResByWzApi", targetUrl.toString());
        CommonRes<T> cRes = getGetRes(clazz, targetUrl, extraHeaders);

        for (IWzHttpReqHook hook : mWzHttpReqHook) {
            cRes = hook.prepostRes(url.host(), params, cRes);
        }

        for (IWzHttpReqHook hook : mWzHttpReqHook) {
            cRes = hook.postRes(url.host(), params, cRes);
        }
//        Thread thread = Thread.currentThread();
//        long id = thread.getId();
//        Log.e("ThreadID", "getGetRes:" + id);
        return cRes;
    }

    private <T extends JBase> CommonRes<T> getGetRes(Class<T> clazz, HttpUrl url, Map<String, String> extraHeaders) {
        for (IHttpReqHook hook : mHttpReqHook) {
            hook.postRequest(url, extraHeaders);
        }

        Request request = new Request.Builder().url(url)
                .headers(extraHeaders == null ? new Headers.Builder().build() : Headers.of(extraHeaders))
                .get().build();

        try {
            Response response = mClient.newCall(request).execute();

            for (IHttpReqHook hook : mHttpReqHook) {
                hook.postResponse(url, response);
            }

            if (response.isSuccessful()) {
                ResponseBody bodyRes = response.body();
                if (bodyRes != null) {
                    String body = bodyRes.string();
                    T resObj;
                    try {
                        resObj = JSON.parseObject(body, clazz);

                    } catch (Exception e) {
                        resObj = null;
                    }
                    if (resObj == null) {
                        JBase tmpExp;
                        try {
                            tmpExp = JSON.parseObject(body, JBase.class);
                        } catch (Exception e) {
                            return new CommonRes<>(false, "");
                        }
                        return new CommonRes<>(true, tmpExp.getErrorCode());
                    }
                    JBase tmp = (JBase) resObj;
                    return new CommonRes<>(true, tmp.getErrorCode(), resObj, body);
                } else {
                    return new CommonRes<>(false, "");
                }
            } else {
                return new CommonRes<>(false, "");
            }
        } catch (IOException e) {
            return new CommonRes<>(false, "");
        }
    }

    //下载文件
    public void downloadFile(final String path, final String url) {
        new Thread() {
            public void run() {
                download(path, url);
            }
        }.start();
    }

    private <T extends JBase> void reqRes(Class<T> clazz, String path, Map<String, String> params, IResCallback<T> callback, HttpUrl url, ERequest eRequest) {
        if (url == null) {
            return;
        }
        if (TextUtils.isEmpty(path)) {
            path = url.encodedPath();
            if (!TextUtils.isEmpty(path) && path.startsWith("/")) {
                path = path.substring(1, path.length()).toString();
            }
        }
        HttpUrl.Builder urlBuild = new HttpUrl.Builder()
                .scheme(url.scheme())
                .host(url.host())
                .addPathSegments(path);
        //param回调
        //预处理
        Map<String, String> extraHeaders = new HashMap<>();
        for (IWzHttpReqHook hook : mWzHttpReqHook) {
            params = hook.prepostParams(url.host(), params);
            extraHeaders = hook.prepostExtraHeaders(url.host(), extraHeaders);
        }
        //再处理
        for (IWzHttpReqHook hook : mWzHttpReqHook) {
            params = hook.postParams(url.host(), params);
            extraHeaders = hook.postExtraHeaders(url.host(), extraHeaders);
        }

        for (String key : params.keySet()) {
            urlBuild.addQueryParameter(key, params.get(key));
        }

        HttpUrl targetUrl = urlBuild.build();
        WzResHttpNetCallBack nBack = new WzResHttpNetCallBack<>(url, params, mWzHttpReqHook, new WzResCallback<>(callback), clazz);
        reqRes(targetUrl, eRequest, extraHeaders, nBack);
    }

    private <T extends JBase> void reqRes(final HttpUrl url, ERequest eRequest, Map<String, String> extraHeaders, final IHttpNetCallback callback) {
        for (IHttpReqHook hook : mHttpReqHook) {
            hook.postRequest(url, extraHeaders);
        }
        Call call = mClient.newCall(eRequest == ERequest.GET ?
                (new Request.Builder().url(url)
                        .headers(extraHeaders == null ? new Headers.Builder().build() : Headers.of(extraHeaders))
                        .get()
                        .build())
                : (new Request.Builder().url(url)
                .headers(extraHeaders == null ? new Headers.Builder().build() : Headers.of(extraHeaders))
                .get()
                .build()));
//        Request request = new Request.Builder().url(url)
//                .headers(extraHeaders == null ? new Headers.Builder().build() : Headers.of(extraHeaders))
//                .get().build();
//        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            public void onResponse(Call call, Response response) throws IOException {

                for (IHttpReqHook hook : mHttpReqHook) {
                    hook.postResponse(url, response);
                }

                ResponseBody bodyRes = response.body();
                if (bodyRes != null) {
                    String body = bodyRes.string();
                    d("reqGetRes", body);
                    callback.onResponse(body);
                }
            }
        });
    }

    //下载
    private void download(final String path, final String url) {
        Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Sink sink = null;
                BufferedSink bufferedSink = null;
                try {
                    File dest = new File(path);
                    sink = Okio.sink(dest);
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedSink != null) {
                        bufferedSink.close();
                    }
                }
            }
        });
    }
}

class WzResHttpNetCallBack<T> implements IHttpNetCallback {
    private Class<T> mClazz;
    private IResCallback<T> mCallback1;
    private List<IWzHttpReqHook> mWzHttpReqHook;
    private Map<String, String> mParams;
    private HttpUrl mUrl;

    public WzResHttpNetCallBack(final HttpUrl url, final Map<String, String> params, List<IWzHttpReqHook> wzHttpReqHook, IResCallback<T> callback, Class<T> clazz) {
        mCallback1 = callback;
        mClazz = clazz;
        mWzHttpReqHook = wzHttpReqHook;
        mUrl = url;
        mParams = params;
    }

    public void onFailure(IOException e) {
        mCallback1.onFailure(e);
    }

    public void onResponse(String response) {
        CommonRes<T> cRes;
        T resObj;
        try {
            resObj = JSON.parseObject(response, mClazz);
        } catch (Exception e) {
            resObj = null;
        }
        if (resObj == null) {
            cRes = new CommonRes<>(false, "");
        } else {
            JBase tmp = (JBase) resObj;
            cRes = new CommonRes<>(true, tmp.getErrorCode(), resObj, response);
        }

        for (IWzHttpReqHook hook : mWzHttpReqHook) {
            cRes = hook.prepostRes(mUrl.host(), mParams, cRes);
        }

        for (IWzHttpReqHook hook : mWzHttpReqHook) {
            cRes = hook.postRes(mUrl.host(), mParams, cRes);
        }

        mCallback1.onFinish(cRes);
    }
}

class WzResCallback<T> implements IResCallback<T> {
    public IResCallback<T> mCallback;

    public WzResCallback(IResCallback<T> callback) {
        mCallback = callback;
    }

    @Override
    public void onFailure(IOException e) {
        mCallback.onFailure(e);
    }

    public void onFinish(CommonRes<T> res) {
        mCallback.onFinish(res);
    }
}