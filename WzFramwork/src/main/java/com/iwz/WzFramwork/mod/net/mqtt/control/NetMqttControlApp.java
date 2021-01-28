package com.iwz.WzFramwork.mod.net.mqtt.control;

import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.mod.net.mqtt.NetMqttMain;
import com.iwz.WzFramwork.mod.net.mqtt.model.IMqttTopicDealer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetMqttControlApp extends ControlApp {
    private static NetMqttControlApp instance = null;
    private NetMqttMain mMain;

    protected NetMqttControlApp(NetMqttMain main) {
        super(main);
        mMain = main;
        mTopicDealers = new HashMap<>();
    }

    public static NetMqttControlApp getInstance(NetMqttMain main) {
        if (instance == null) {
            instance = new NetMqttControlApp(main);
        }
        return instance;
    }

    private Map<String, ArrayList<IMqttTopicDealer>> mTopicDealers;

    public void addTopicDealer(String topic, IMqttTopicDealer dealer) {
        ArrayList<IMqttTopicDealer> dealers = mTopicDealers.get(topic);
        if (dealers == null) {
            dealers = new ArrayList<>();
            mTopicDealers.put(topic, dealers);
        }
        dealers.add(dealer);
    }


    public void dispatchMsg(String topic, String data) {
        ArrayList<IMqttTopicDealer> dealers = mTopicDealers.get(topic);
        if (dealers != null) {
            for (IMqttTopicDealer dealer : dealers) {
                dealer.onArrive(topic, data);
            }
        }
    }
}
