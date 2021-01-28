package com.iwz.WzFramwork.mod.bus.event.control;


import com.iwz.WzFramwork.mod.bus.event.BusEventMain;
import com.iwz.WzFramwork.mod.bus.event.model.IJsCallDealer;
import com.iwz.WzFramwork.mod.bus.event.model.IJsCallResponse;
import com.iwz.WzFramwork.mod.bus.event.model.IJsExecuteDealer;
import com.iwz.WzFramwork.mod.bus.event.model.IJsPubDealer;
import com.iwz.WzFramwork.mod.bus.event.model.JsCall;
import com.iwz.WzFramwork.mod.bus.event.model.JsExecute;
import com.iwz.WzFramwork.mod.bus.event.model.JsPub;
import com.iwz.WzFramwork.base.app.ControlApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BusEventControlApp extends ControlApp {
    private static BusEventControlApp instance = null;
    private BusEventMain mMain;

    protected BusEventControlApp(BusEventMain main) {
        super(main);
        mMain = main;
    }

    public static BusEventControlApp getInstance(BusEventMain main) {
        if (instance == null) {
            instance = new BusEventControlApp(main);
        }
        return instance;
    }

    public void born() {
        mJsCallDealers = new HashMap<>();
        mJsPubDealers = new HashMap<>();
        mJsExecuteDealers = new HashMap<>();
    }

    private Map<String, ArrayList<IJsCallDealer>> mJsCallDealers;

    public void addJsCallDealer(String method, IJsCallDealer dealer) {
        ArrayList<IJsCallDealer> dealers = mJsCallDealers.get(method);
        if (dealers == null) {
            dealers = new ArrayList<>();
            mJsCallDealers.put(method, dealers);
        }
        dealers.add(dealer);
        d("addJsCallDealer", "addDealer" + method);
    }

    public void doJsCall(JsCall res, IJsCallResponse response) {
        ArrayList<IJsCallDealer> dealers = mJsCallDealers.get(res.getMethod());
        if (dealers != null) {
            for (IJsCallDealer dealer : dealers) {
                dealer.call(res, response);
            }
        }
    }

    private Map<String, ArrayList<IJsPubDealer>> mJsPubDealers;

    public void addJsPubDealer(String method, IJsPubDealer dealer) {
        ArrayList<IJsPubDealer> dealers = mJsPubDealers.get(method);
        if (dealers == null) {
            dealers = new ArrayList<>();
            mJsPubDealers.put(method, dealers);
        }
        dealers.add(dealer);
        d("addJsPubDealer", "addDealer" + method);
    }

    public void doJsPub(JsPub res) {
        ArrayList<IJsPubDealer> dealers = mJsPubDealers.get(res.getName());
        if (dealers != null) {
            for (IJsPubDealer dealer : dealers) {
                dealer.pub(res);
            }
        }
    }

    private Map<String, ArrayList<IJsExecuteDealer>> mJsExecuteDealers;

    public void addJsExecuteDealer(String method, IJsExecuteDealer dealer) {
        ArrayList<IJsExecuteDealer> dealers = mJsExecuteDealers.get(method);
        if (dealers == null) {
            dealers = new ArrayList<>();
            mJsExecuteDealers.put(method, dealers);
        }
        dealers.add(dealer);
    }

    public void doJsExecute(JsExecute jsExecute) {
        ArrayList<IJsExecuteDealer> dealers = mJsExecuteDealers.get(jsExecute.getName());
        if (dealers != null) {
            for (IJsExecuteDealer dealer : dealers) {
                dealer.execute(jsExecute);
            }
        }
    }
}
