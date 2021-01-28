package com.iwz.WzFramwork.mod.net.http;


import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.net.http.serv.NetHttpServApi;

public class NetHttpMain extends ModMain {
    public String getModName() {
        return "NetHttpMain";
    }
    @Override
    public ModType getModType() {
        return ModType.MODE_NET;
    }
    private static NetHttpMain mNetHttpMain;

    public static NetHttpMain getInstance() {
        if (mNetHttpMain == null) {
            synchronized (NetHttpMain.class) {
                if (mNetHttpMain == null) {
                    mNetHttpMain = new NetHttpMain();
                }
            }
        }
        return mNetHttpMain;
    }

    private NetHttpMain() {
        pServApi = NetHttpServApi.getInstance(this);
    }

    public NetHttpServApi pServApi;

    public void born() {
        super.born();

    }

    public NetHttpServApi getServApi() {
        return pServApi;
    }
}
