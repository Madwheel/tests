package com.iwz.WzFramwork.mod.net.mqtt.model;


import com.iwz.WzFramwork.base.CommonRes;

public interface IMqttResCallback<T> {
    void onFinish(CommonRes<T> res);
    void onArrive(String topic, String data);
}
