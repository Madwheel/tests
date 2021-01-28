package com.iwz.WzFramwork.mod.bus.event.model;

import com.alibaba.fastjson.JSON;

public class JsCall  {
    private int mCid;
    private String mMethod;
    private String mCtime;

    private String mModName;
    private String mCmdName;
    private String mQuery;

    public JsCall(int cid, String method, String ctime,String query){
        mCid        = cid;
        mMethod     = method;
        mCtime      = ctime;
        mQuery      = query;
        int cPos    = method.indexOf('.');
        if(cPos == -1){
            mModName    = "";
            mCmdName    = "";
        }
        else{
            mModName    = method.substring(0,cPos);
            mCmdName    = method.substring(cPos+1);
        }
    }

    public int getCid(){
        return mCid;
    }

    public String getMethod(){
        return mMethod;
    }
    public String getQuery(){
        return mQuery;
    }

    public String getModName(){
        return mModName;
    }

    public String getCmdName(){
        return mCmdName;
    }

    public String getCtime(){
        return mCtime;
    }

    public <T> T getParam(Class<T> clazz){
        return JSON.parseObject(mQuery,clazz);
    }

}
