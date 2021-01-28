package com.iwz.WzFramwork.mod.biz.collect.serv;


import com.iwz.WzFramwork.mod.biz.collect.BizCollectMain;
import com.iwz.WzFramwork.base.CommonRes;
import com.iwz.WzFramwork.base.MyObject;
import com.iwz.WzFramwork.base.api.ServApi;

public class BizCollectServApi extends ServApi {
    private static BizCollectServApi instance=null;
    protected BizCollectServApi(BizCollectMain main){
        super(main);
    }
    public static BizCollectServApi getInstance(BizCollectMain main){
        if(instance == null){
            instance = new BizCollectServApi(main);
        }
        return instance;
    }

    public CommonRes<MyObject> uploadToServer(String c){
        //post to serv
        d("uploadToServer");
        return new CommonRes<>(true);
    }
}
