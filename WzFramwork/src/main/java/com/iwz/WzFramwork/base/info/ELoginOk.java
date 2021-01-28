package com.iwz.WzFramwork.base.info;


import com.iwz.WzFramwork.base.interfaces.IMyEvent;

public class ELoginOk implements IMyEvent {
    private static String mEventName = "BizAccountLoginOk";

    public static String getEventName() {
        return mEventName;
    }

    public String getName() {
        return getEventName();
    }

    int mUid;
    boolean isRegister;

    public ELoginOk(int uid) {
        mUid = uid;
        isRegister = false;
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
    }

    public int getUid() {
        return mUid;
    }
}
