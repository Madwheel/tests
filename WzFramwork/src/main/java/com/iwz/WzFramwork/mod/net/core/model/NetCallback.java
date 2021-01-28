package com.iwz.WzFramwork.mod.net.core.model;


import com.iwz.WzFramwork.base.interfaces.IResCallback;

abstract public class NetCallback {
    protected IResCallback mCallback;
    public NetCallback(IResCallback callback){
        mCallback = callback;
    }

}
