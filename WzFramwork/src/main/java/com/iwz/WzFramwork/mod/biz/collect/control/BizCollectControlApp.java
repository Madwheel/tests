package com.iwz.WzFramwork.mod.biz.collect.control;


import android.text.TextUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.mod.biz.collect.BizCollectMain;
import com.iwz.WzFramwork.mod.statistic.BizStatisticMain;

import java.util.HashMap;
import java.util.Map;

public class BizCollectControlApp extends ControlApp {
    private static BizCollectControlApp instance = null;
    private BizCollectMain mMain;

    protected BizCollectControlApp(BizCollectMain main) {
        super(main);
        mMain = main;
    }

    public static BizCollectControlApp getInstance(BizCollectMain main) {
        if (instance == null) {
            instance = new BizCollectControlApp(main);
        }
        return instance;
    }


    public void born() {

    }

    public void create() {

    }

    public void addCollect(String page, String name, String url, final String content) {
        d("addCollect:" + content);
        if (!TextUtils.isEmpty(content)) {
            Map<String, String> params = new HashMap<>();
            params.put("eventPage", "wzClick:" + page);
            params.put("eventName", "wzClick:" + name);
            params.put("eventUrl", "wzClick:" + url);
            params.put("eventId", "wzClick:" + content);
            BizStatisticMain.getInstance().getpControl().onEvent(content, params);
        }
        WzFramworkApplication.getmThread().getHandler().post(new Runnable() {
            @Override
            public void run() {
                String line;
                try {
                    JSONObject item = new JSONObject();
                    item.put("content", content);
                    line = item.toString();
                } catch (JSONException e) {
                    return;
                }
                mMain.pModelApi.addLine(line);
                String c = mMain.pModelApi.getSyncContent();
                if (c != null) {
                    mMain.pServApi.uploadToServer(c);
                }
            }
        });
    }
}
