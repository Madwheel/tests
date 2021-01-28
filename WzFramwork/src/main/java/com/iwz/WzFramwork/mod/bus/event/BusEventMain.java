package com.iwz.WzFramwork.mod.bus.event;


import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.bus.event.control.BusEventControlApp;
import com.iwz.WzFramwork.mod.bus.event.model.IJsCallDealer;
import com.iwz.WzFramwork.mod.bus.event.model.IJsExecuteDealer;
import com.iwz.WzFramwork.mod.bus.event.model.IJsPubDealer;
import com.iwz.WzFramwork.mod.bus.event.model.IMyEventDealer;
import com.iwz.WzFramwork.mod.bus.event.serv.BusEventServApi;
import com.iwz.WzFramwork.base.interfaces.IMyEvent;
import com.iwz.WzFramwork.base.main.ModMain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class BusEventMain extends ModMain {
    public String getModName() {
        return "BusEventMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_BUS;
    }

    private static BusEventMain instance = new BusEventMain();

    private BusEventMain() {
    }

    public static BusEventMain getInstance() {
        return instance;
    }

    public BusEventControlApp pControlApp;
    public BusEventServApi mServ;

    public void born() {
        super.born();
        mEventDealers = new HashMap<>();
        pControlApp = BusEventControlApp.getInstance(this);
        pControlApp.born();
        mServ = BusEventServApi.getInstance(this);
        mServ.born();
    }

    public void run() {
        //handle loop just do it
    }

    private Map<String, CopyOnWriteArrayList<IMyEventDealer>> mEventDealers;

    public void addDealer(String eventName, IMyEventDealer dealer) {
        CopyOnWriteArrayList<IMyEventDealer> dealers = mEventDealers.get(eventName);
        if (dealers == null) {
            dealers = new CopyOnWriteArrayList<>();
            mEventDealers.put(eventName, dealers);
        }
        dealers.add(dealer);
        d("addEventDealer" + eventName);
    }

    public void publish(final IMyEvent event) {
        WzFramworkApplication.getmThread().getHandler().post(new Runnable() {
            @Override
            public void run() {
                CopyOnWriteArrayList<IMyEventDealer> dealers = mEventDealers.get(event.getName());
                d("myevent", "publish");
                if (dealers != null) {
                    for (IMyEventDealer dealer : dealers) {
                        dealer.onOccur(event);
                    }
                }
            }
        });
    }

    public void removeDealer(String eventName, IMyEventDealer dealer) {
        CopyOnWriteArrayList<IMyEventDealer> dealers = mEventDealers.get(eventName);
        if (dealers == null) {
            return;
        }
        dealers.remove(dealer);
    }

    public void addJsCallDealer(String method, IJsCallDealer dealer) {
        pControlApp.addJsCallDealer(method, dealer);
    }

    public void addJsPubDealer(String method, IJsPubDealer dealer) {
        pControlApp.addJsPubDealer(method, dealer);
    }

    public void addJsExecuteDealer(String method, IJsExecuteDealer dealer) {
        pControlApp.addJsExecuteDealer(method, dealer);
    }

}
