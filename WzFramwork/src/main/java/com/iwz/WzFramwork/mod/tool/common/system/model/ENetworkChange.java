package com.iwz.WzFramwork.mod.tool.common.system.model;

import android.net.NetworkInfo;

import com.iwz.WzFramwork.base.interfaces.IMyEvent;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/11/1214:06
 * desc   :
 */
public class ENetworkChange implements IMyEvent {
    private static String mEventName = "ENetworkChange";

    public static String getEventName() {
        return mEventName;
    }

    @Override
    public String getName() {
        return getEventName();
    }

    NetworkInfo mNetworkInfo;

    public ENetworkChange(NetworkInfo networkInfo) {
        this.mNetworkInfo = networkInfo;
    }

    public NetworkInfo getmNetworkInfo() {
        return mNetworkInfo;
    }

    public void setmNetworkInfo(NetworkInfo mNetworkInfo) {
        this.mNetworkInfo = mNetworkInfo;
    }
}
