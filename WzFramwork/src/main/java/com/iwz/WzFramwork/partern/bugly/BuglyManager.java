package com.iwz.WzFramwork.partern.bugly;

import android.content.Context;

import com.iwz.WzFramwork.partern.bugly.upgrade.UpgradeApk;
import com.tencent.bugly.beta.UpgradeInfo;

public class BuglyManager {
    private static BuglyManager mInstance;

    public static BuglyManager getInstance() {
        if (mInstance == null) {
            synchronized (BuglyManager.class) {
                if (mInstance == null) {
                    mInstance = new BuglyManager();
                }
            }
        }
        return mInstance;
    }

    public void intBugly(Class mClass, Context context, String appId,  boolean isDebug,int img) {
        UpgradeApk.getInstance().intBugly(mClass, context, appId, isDebug,img);
    }

    public void checkUpgrade() {
        UpgradeApk.getInstance().checkUpgrade();
    }

    public UpgradeInfo getUpgradeInfo() {
        return UpgradeApk.getInstance().getUpgradeInfo();
    }
}
