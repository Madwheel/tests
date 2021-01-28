package com.iwz.WzFramwork.mod.bus.event.model;

public interface IJsCallResponse {
    void onResponse(int errorCode, Object result);

    void onPush(int errorCode, String name, Object result);
}
