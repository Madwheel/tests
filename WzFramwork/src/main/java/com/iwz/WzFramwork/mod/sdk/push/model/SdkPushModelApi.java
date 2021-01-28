package com.iwz.WzFramwork.mod.sdk.push.model;

import com.iwz.WzFramwork.base.api.ModelApi;
import com.iwz.WzFramwork.mod.sdk.push.SdkPushMain;

/**
 * author : 小辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/6/1713:49
 * desc   :
 */
public class SdkPushModelApi extends ModelApi {
    public SdkPushMain mMain;

    private PushIdInfo mPushIdInfo;

    public SdkPushModelApi(SdkPushMain main) {
        super(main);
        mMain = main;
    }

    private static SdkPushModelApi mSdkPushModelApi = null;

    public static SdkPushModelApi getInstance(SdkPushMain main) {
        synchronized (SdkPushModelApi.class) {
            if (mSdkPushModelApi == null) {
                mSdkPushModelApi = new SdkPushModelApi(main);
            }
        }
        return mSdkPushModelApi;
    }

    @Override
    public void born() {
        super.born();
        mPushIdInfo = new PushIdInfo();
    }

    public PushIdInfo getmPushIdInfo() {
        return mPushIdInfo;
    }

    public void setmPushIdInfo(PushIdInfo mPushIdInfo) {
        this.mPushIdInfo = mPushIdInfo;
    }
}
