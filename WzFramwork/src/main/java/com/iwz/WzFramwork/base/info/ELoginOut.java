package com.iwz.WzFramwork.base.info;


import com.iwz.WzFramwork.base.interfaces.IMyEvent;

public class ELoginOut implements IMyEvent {
    private static String mEventName = "BizAccountLoginOut";

    public static String getEventName() {
        return mEventName;
    }

    public String getName() {
        return getEventName();
    }

}
