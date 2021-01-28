package com.iwz.WzFramwork.partern.xiaomi;

import android.content.Context;

import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * author : 小辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/6/1813:41
 * desc   :
 */
public class MiPushManager {
    private static MiPushManager mMiPushManager = null;

    public static MiPushManager getInstance() {
        synchronized (MiPushManager.class) {
            if (mMiPushManager == null) {
                mMiPushManager = new MiPushManager();
            }
        }
        return mMiPushManager;
    }

    public void registerPush(Context context, String appId, String appKey) {
        MiPushClient.registerPush(context, appId, appKey);
    }

    public void setAlais(Context context, String did) {
        MiPushClient.setAlias(context, did, null);
    }
}
