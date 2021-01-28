package com.iwz.WzFramwork.mod.io.file;


import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.io.file.serv.IoFileServApi;

import java.io.InputStream;

public class IoFileMain extends ModMain {
    public String getModName() {
        return "IoFileMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_IO;
    }

    private static IoFileMain instance = new IoFileMain();

    private IoFileMain() {
    }

    public static IoFileMain getInstance() {
        return instance;
    }

    public IoFileServApi pServApi;

    public void born() {
        super.born();
        pServApi = IoFileServApi.getInstance(this);
    }


    public String readFileStr(String filePath) {
        return pServApi.readFileStr(filePath);
    }

    public String readFileStr(InputStream is) {
        return pServApi.readFileStr(is);
    }
}
