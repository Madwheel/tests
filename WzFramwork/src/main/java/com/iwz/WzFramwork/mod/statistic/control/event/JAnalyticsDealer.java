package com.iwz.WzFramwork.mod.statistic.control.event;

import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.base.info.ELoginOk;
import com.iwz.WzFramwork.base.interfaces.IMyEvent;
import com.iwz.WzFramwork.mod.bus.event.BusEventMain;
import com.iwz.WzFramwork.mod.bus.event.model.IMyEventDealer;
import com.iwz.WzFramwork.mod.statistic.BizStatisticMain;

import cn.jiguang.analytics.android.api.LoginEvent;
import cn.jiguang.analytics.android.api.RegisterEvent;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/12/03
 */
public class JAnalyticsDealer extends ControlApp {

    private BizStatisticMain mMain;

    protected JAnalyticsDealer(BizStatisticMain main) {
        super(main);
        mMain = main;
    }

    private static JAnalyticsDealer mJAnalyticsDealer;

    public static JAnalyticsDealer getInstance(BizStatisticMain main) {
        if (mJAnalyticsDealer == null) {
            synchronized (JAnalyticsDealer.class) {
                if (mJAnalyticsDealer == null) {
                    mJAnalyticsDealer = new JAnalyticsDealer(main);
                }
            }
        }
        return mJAnalyticsDealer;
    }

    @Override
    public void born() {
        super.born();
        hookLogin();
    }

    private void hookLogin() {
        BusEventMain.getInstance().addDealer(ELoginOk.getEventName(), new IMyEventDealer() {
            @Override
            public void onOccur(IMyEvent event) {
                boolean register = ((ELoginOk) event).isRegister();
                if (register) {//注册登录
                    RegisterEvent registerEvent = new RegisterEvent("h5", true);
                    mMain.pControl.onEvent(registerEvent);
                } else {
                    LoginEvent loginEvent = new LoginEvent("h5", true);
                    mMain.pControl.onEvent(loginEvent);
                }
            }
        });
    }
}
