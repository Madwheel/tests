package com.iwz.WzFramwork.mod.net.core.conf;


import android.util.Log;

import com.iwz.WzFramwork.base.api.ConfApi;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.core.config.CoreConfigMain;

public class NetCoreConfApi extends ConfApi {
    private static NetCoreConfApi instance = null;

    protected NetCoreConfApi(ModMain main) {
        super(main);
    }

    public static NetCoreConfApi getInstance(ModMain main) {
        if (instance == null) {
            instance = new NetCoreConfApi(main);
        }
        return instance;
    }

    private JNetCoreConf mConf;

    public void born() {
        mConf = CoreConfigMain.getInstance().getModConf("mod.net.core", JNetCoreConf.class);
        if (mConf == null) {
            mConf = new JNetCoreConf();
        }
        Log.e("TESRTSS", "mConf" + mConf);
    }

    public String getApiHost() {
        return mConf.getApiHost();
    }

    public JNetCoreConf getConf() {
        return mConf;
    }
}
