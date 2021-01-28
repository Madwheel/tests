package com.iwz.WzFramwork.mod.bus.event.control;


import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iwz.WzFramwork.mod.bus.event.BusEventMain;
import com.iwz.WzFramwork.mod.bus.event.model.IJsCallResponse;
import com.iwz.WzFramwork.mod.bus.event.model.JsCall;
import com.iwz.WzFramwork.mod.bus.event.model.JsExecute;
import com.iwz.WzFramwork.mod.bus.event.model.JsPub;
import com.iwz.WzFramwork.base.MyObject;
import com.iwz.WzFramwork.mod.tool.webview.view.MyWebview;

public class WZAppJsInterface extends MyObject {
    private MyWebview mWebView;
    private BusEventMain mMain;

    public WZAppJsInterface(MyWebview webView) {
        mWebView = webView;
        mMain = BusEventMain.getInstance();
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void exec(String command, String content) {
        JSONObject jo = JSON.parseObject(content);
        MyObject.d("WZAppJsInterface");
        MyObject.d(command);
        MyObject.d(content);

        switch (command) {
            case "call":
                final JsCall call;
                call = new JsCall(jo.getIntValue("cid"), jo.getString("method"), jo.getString("ctime"), jo.getJSONObject("query").toJSONString());
                mMain.pControlApp.doJsCall(call, new IJsCallResponse() {
                    public void onResponse(int errorCode, Object result) {
                        if (mWebView.isShown()) {
                            d("webview is  shown");
                            JSONObject callback = new JSONObject();
                            callback.put("errorCode", errorCode);
                            callback.put("cid", call.getCid());
                            callback.put("method", call.getMethod());
                            callback.put("result", result);
                            final String jsCode = "WZWeb.exec(\"call\"," + callback.toJSONString() + ")";
                            //回到ui主线程
                            mMain.mServ.jsExecute(mWebView, jsCode);
                        } else {
                            d("webview is not shown");
                        }
                    }

                    @Override
                    public void onPush(int errorCode, String name, Object result) {
//                        if (mWebView.isShown()) {
                        d("webview is  shown");
                        JSONObject push = new JSONObject();
                        push.put("id", mMain.mServ.getPushID());
                        push.put("name", name);
                        push.put("ctime", System.currentTimeMillis());
                        push.put("data", result);
                        final String jsCode = "javascript:try {WZAppMQ.push(" + push.toString() + ")} catch(e) {throw e + ' WZAppMQ.push: " + call.getMethod() + "'}";
                        Log.e("JSCODE", "" + jsCode);
                        mMain.mServ.jsExecute(mWebView, jsCode);
//                        }
                    }
                });
                break;
            case "push":
                JsPub pub = new JsPub(jo.getIntValue("id"), jo.getString("name"), jo.getString("ctime"), jo.getJSONObject("data").toJSONString());
                mMain.pControlApp.doJsPub(pub);
                break;
            default:
                JsExecute jsExecute = new JsExecute(command, content, mWebView);
                mMain.pControlApp.doJsExecute(jsExecute);
                e("WZAppJsInterface", "errorCommand:" + command);
        }
    }
}
