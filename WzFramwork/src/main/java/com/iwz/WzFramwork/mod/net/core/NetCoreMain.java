package com.iwz.WzFramwork.mod.net.core;


import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.net.core.conf.JNetCoreConf;
import com.iwz.WzFramwork.mod.net.core.conf.NetCoreConfApi;

public class NetCoreMain extends ModMain {
    public String getModName() {
        return "NetCoreMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_NET;
    }

    private NetCoreMain() {
        pConfApi = NetCoreConfApi.getInstance(this);
    }

    private static NetCoreMain mNetCoreMain = null;

    public static NetCoreMain getInstance() {
        synchronized (NetCoreMain.class) {
            if (mNetCoreMain == null) {
                mNetCoreMain = new NetCoreMain();
            }
        }
        return mNetCoreMain;
    }

    public NetCoreConfApi pConfApi;

    public void born() {
        super.born();
        pConfApi.born();
    }

    public JNetCoreConf getConf() {
        return pConfApi.getConf();
    }
}
