package com.iwz.WzFramwork.mod.net.mqtt;


import android.util.Log;

import com.iwz.WzFramwork.base.CommonRes;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.net.mqtt.control.NetMqttControlApp;
import com.iwz.WzFramwork.mod.net.mqtt.model.IMqttResCallback;
import com.iwz.WzFramwork.mod.net.mqtt.model.IMqttTopicDealer;
import com.iwz.WzFramwork.mod.net.mqtt.serv.NetMqttServApi;


public class NetMqttMain extends ModMain {
    public String getModName() {
        return "NetMqttMain";
    }
    @Override
    public ModType getModType() {
        return ModType.MODE_NET;
    }
    private static NetMqttMain instance = new NetMqttMain();

    public static NetMqttMain getInstance() {
        return instance;
    }

    public NetMqttServApi pServApi;
    public NetMqttControlApp pControlApp;

    private NetMqttMain() {
        pServApi = NetMqttServApi.getInstance(this);
        pControlApp = NetMqttControlApp.getInstance(this);
    }

    public void born() {
        super.born();
    }

    public void addTopicDealer(String topic, IMqttTopicDealer dealer) {
        pControlApp.addTopicDealer(topic, dealer);
    }

    public <T> void startWork(String host, int port, final String token, String[] topics, String clientId) {
        d("xxxmqttstartWork");

        pServApi.startWork(host, port, token, topics, clientId, new IMqttResCallback<T>() {
            public void onFinish(CommonRes<T> res) {

            }

            public void onArrive(String topic, String data) {
                d("mqttOnArrive", topic + data);
                pControlApp.dispatchMsg(topic, data);
            }
        });
    }

    public void stopWork() {
        d("xxxmqttstopWork");
        pServApi.terminateWork();
    }
}
