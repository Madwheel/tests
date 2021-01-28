package com.iwz.WzFramwork.mod.core.env;


import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;

public class CoreEnvMain extends ModMain {
    public String getModName() {
        return "CoreEnvMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_CORE;
    }

    private static CoreEnvMain instance = new CoreEnvMain();

    private CoreEnvMain() {
    }

    public static CoreEnvMain getInstance() {
        return instance;
    }

    public void born() {
        super.born();
    }

    public String getEnvName() {
        return WzFramworkApplication.getmMode();
    }

    public String getChannelName() {
        return WzFramworkApplication.getmChannel();
    }

    public boolean isDev() {
        return getEnvName().equals("dev");
    }
}


