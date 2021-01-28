package com.iwz.WzFramwork.mod.statistic.control;

import android.content.Context;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.mod.statistic.BizStatisticMain;
import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.mod.core.env.CoreEnvMain;

import java.util.Map;
import java.util.Set;

import cn.jiguang.analytics.android.api.AccountCallback;
import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.Event;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/12/03
 */
public class BizStatisticControlApp extends ControlApp {

    private BizStatisticMain mMain;

    public BizStatisticControlApp(BizStatisticMain main) {
        super(main);
        mMain = main;
    }

    private static BizStatisticControlApp mBizStatisticControlApp;

    public static BizStatisticControlApp getInstance(BizStatisticMain main) {
        if (mBizStatisticControlApp == null) {
            synchronized (BizStatisticControlApp.class) {
                if (mBizStatisticControlApp == null) {
                    mBizStatisticControlApp = new BizStatisticControlApp(main);
                }
            }
        }
        return mBizStatisticControlApp;
    }

    @Override
    public void born() {
        super.born();
        initJAnalytics();
    }

    private void initJAnalytics() {
        JAnalyticsInterface.init(WzFramworkApplication.getmContext());
        JAnalyticsInterface.initCrashHandler(WzFramworkApplication.getmContext());//开启crashlog日志上报
        JAnalyticsInterface.setChannel(WzFramworkApplication.getmContext(), CoreEnvMain.getInstance().getChannelName());
        JAnalyticsInterface.setDebugMode(CoreEnvMain.getInstance().isDev());//设置调试模式：参数为 true 表示打开调试模式，可看到 sdk 的日志
    }

    public void setAnalyticsReportPeriod(Context context, int period) {
        mMain.pServ.setAnalyticsReportPeriod(context, period);
    }

    public void onPageStart(Context context, String pageName) {
        mMain.pServ.onPageStart(context, pageName);
    }

    public void onPageEnd(Context context, String pageName) {
        mMain.pServ.onPageEnd(context, pageName);
    }

    public void onEvent(String eventName, Map<String, String> params) {
        CountEvent cEvent = new CountEvent(eventName);
        if (params != null && params.size() > 0) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                cEvent.addKeyValue(key, params.get(key));
            }
        }
        mMain.pServ.onEvent(WzFramworkApplication.getmContext(), cEvent);
    }

    public void onEvent(Event event) {
        mMain.pServ.onEvent(WzFramworkApplication.getmContext(), event);
    }

    public void identifyAccount(String uid, String nickName, int paid) {
        mMain.pServ.identifyAccount(WzFramworkApplication.getmContext(), uid, nickName, paid, new AccountCallback() {
            @Override
            public void callback(int code, String msg) {

            }
        });
    }

    public void detachAccount() {
        mMain.pServ.detachAccount(WzFramworkApplication.getmContext(), new AccountCallback() {
            @Override
            public void callback(int code, String msg) {

            }
        });
    }
}
