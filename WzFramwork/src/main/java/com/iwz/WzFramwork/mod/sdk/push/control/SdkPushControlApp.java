package com.iwz.WzFramwork.mod.sdk.push.control;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.EAppPhase;
import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.mod.BaseMgr;
import com.iwz.WzFramwork.mod.sdk.push.SdkPushMain;
import com.iwz.WzFramwork.mod.sdk.push.interfaces.ISdkPushListener;
import com.iwz.WzFramwork.mod.sdk.push.model.PushIdInfo;
import com.iwz.WzFramwork.partern.xiaomi.MiPushManager;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/03
 */
public class SdkPushControlApp extends ControlApp {

    public SdkPushMain mMain;
    private ISdkPushListener mListener;

    public SdkPushControlApp(SdkPushMain main) {
        super(main);
        mMain = main;
    }

    private static SdkPushControlApp mSdkPushControlApp;

    public static SdkPushControlApp getInstance(SdkPushMain main) {
        if (mSdkPushControlApp == null) {
            synchronized (SdkPushControlApp.class) {
                if (mSdkPushControlApp == null) {
                    mSdkPushControlApp = new SdkPushControlApp(main);
                }
            }
        }
        return mSdkPushControlApp;
    }


    public void setSdkPushListener(ISdkPushListener listener) {
        this.mListener = listener;
    }

    public void launchMiId(String miId) {
        if (mListener != null) {
            mListener.launchByMiId(miId);
        }
    }

    /**
     * 跳转URL，适配url和路由
     *
     * @param context 上下文
     */
    public void toUri(Context context, String uri) {
        if (mListener != null) {
            mListener.pushClickToUri(context, uri);
        }
    }


    public PushIdInfo getmPushIdInfo() {
        return mMain.mModelApi.getmPushIdInfo();
    }

    public void setmPushIdInfo(PushIdInfo mPushIdInfo) {
        mMain.mModelApi.setmPushIdInfo(mPushIdInfo);
    }
}
