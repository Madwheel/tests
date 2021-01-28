package com.iwz.WzFramwork.mod.net.mqtt.model;


import com.iwz.WzFramwork.mod.net.core.model.NetRes;

public class MqttNetRes extends NetRes {
    protected String mBody;
    public MqttNetRes(String body){
        mBody = body;
    }
}
