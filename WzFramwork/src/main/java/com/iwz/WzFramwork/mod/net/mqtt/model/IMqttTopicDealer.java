package com.iwz.WzFramwork.mod.net.mqtt.model;

public interface IMqttTopicDealer {
    void onArrive(String topic, String data);
}
