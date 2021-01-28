package com.iwz.WzFramwork.mod.biz.collect;


import com.iwz.WzFramwork.mod.biz.collect.control.BizCollectControlApp;
import com.iwz.WzFramwork.mod.biz.collect.model.BizAccountModelApi;
import com.iwz.WzFramwork.mod.biz.collect.serv.BizCollectServApi;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;

public class BizCollectMain extends ModMain {
    public String getModName() {
        return "BizCollectMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_BIZ;
    }

    private static BizCollectMain instance = new BizCollectMain();

    private BizCollectMain() {
    }

    public static BizCollectMain getInstance() {
        return instance;
    }

    public BizCollectControlApp pControlApp;
    public BizAccountModelApi pModelApi;
    public BizCollectServApi pServApi;

    public void born() {
        super.born();
        pControlApp = BizCollectControlApp.getInstance(this);
        pModelApi = BizAccountModelApi.getInstance(this);
        pServApi = BizCollectServApi.getInstance(this);
        pControlApp.born();
    }

    public void create() {
        super.create();
        pControlApp.create();
    }

    public void addCollect(String page, String name, String url, final String content) {
        pControlApp.addCollect(page, name, url, content);
    }
}
