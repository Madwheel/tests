package com.iwz.WzFramwork.mod.sdk.push;

import com.iwz.WzFramwork.base.EAppPhase;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.sdk.push.control.SdkPushControlApp;
import com.iwz.WzFramwork.mod.sdk.push.model.SdkPushModelApi;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/03
 */
public class SdkPushMain extends ModMain {
    public SdkPushControlApp mControl;
    public SdkPushModelApi mModelApi;

    @Override
    public String getModName() {
        return "SdkPushMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_SDK;
    }

    private static SdkPushMain mSdkPushMain = null;

    public static SdkPushMain getInstance() {
        synchronized (SdkPushMain.class) {
            if (mSdkPushMain == null) {
                mSdkPushMain = new SdkPushMain();
            }
        }
        return mSdkPushMain;
    }

    private SdkPushMain() {
        mModelApi = SdkPushModelApi.getInstance(this);
        mControl = SdkPushControlApp.getInstance(this);
    }

    @Override
    public void born() {
        super.born();
        mModelApi.born();
        mControl.born();
    }

    public SdkPushControlApp getmControl() {
        return mControl;
    }
}
