package com.iwz.WzFramwork.mod.bus.event.model;

import com.alibaba.fastjson.JSON;

public class JsPub {
    private int mId;
    private String mName;
    private String mCtime;

    private String mModName;
    private String mCmdName;
    private String mData;

    public JsPub(int id, String name, String ctime, String data){
        mId         = id;
        mName       = name;
        mCtime      = ctime;
        mData       = data;
        int cPos    = name.indexOf('.');
        if(cPos == -1){
            mModName    = "";
            mCmdName    = "";
        }
        else{
            mModName    = name.substring(0,cPos);
            mCmdName    = name.substring(cPos+1);
        }
    }

    public int getId(){
        return mId;
    }

    public String getName(){
        return mName;
    }
    public String getData(){
        return mData;
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
        return JSON.parseObject(mData,clazz);
    }

}
