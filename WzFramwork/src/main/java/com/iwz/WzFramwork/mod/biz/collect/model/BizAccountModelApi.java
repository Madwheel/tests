package com.iwz.WzFramwork.mod.biz.collect.model;


import com.iwz.WzFramwork.mod.biz.collect.BizCollectMain;
import com.iwz.WzFramwork.base.api.ModelApi;

public class BizAccountModelApi extends ModelApi {
    private static BizAccountModelApi instance=null;

    protected BizAccountModelApi( BizCollectMain main){
        super(main);
    }
    public static BizAccountModelApi getInstance( BizCollectMain main){
        if(instance == null){
            instance = new BizAccountModelApi(main);
        }
        return instance;
    }

    public void addLine(String line){
        //write to file
    }

    public String getSyncContent(){
        //check upload condition
        return "{}";
    }
}
