package com.iwz.WzFramwork.partern.bugly.upgrade;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;

public class UpgradeApk {
    private static UpgradeApk mInstance;

    public static UpgradeApk getInstance() {
        if (mInstance == null) {
            synchronized (UpgradeApk.class) {
                if (mInstance == null) {
                    mInstance = new UpgradeApk();
                }
            }
        }
        return mInstance;
    }

    private static final String TAG = "DouKBugly";

    public void intBugly(Class mClass, Context context, String appId, final boolean isDebug, int img) {
        Beta.autoInit = true;//自动初始化升级模块
        Beta.autoCheckUpgrade = true;//自动检测升级
        Beta.initDelay = 0;//检测周期
        Beta.largeIconId = img;//通知状态栏大图标
        Beta.smallIconId = img;//通知状态栏小图标
        Beta.defaultBannerId = img;//更新弹窗默认banner
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);//sd卡的下载地址
        if (mClass != null) {
            Beta.canShowUpgradeActs.add(mClass);//只允许在mClass页面弹窗
        }
        //监听安装包下载状态
        Beta.downloadListener = new DownloadListener() {
            @Override
            public void onReceive(DownloadTask downloadTask) {
                if (isDebug) {
                    Log.d(TAG, "downloadListener receive apk file");
                }
            }

            @Override
            public void onCompleted(DownloadTask downloadTask) {
                if (isDebug) {
                    Log.d(TAG, "downloadListener download apk file success");
                }
            }

            @Override
            public void onFailed(DownloadTask downloadTask, int i, String s) {
                if (isDebug) {
                    Log.d(TAG, "downloadListener download apk file fail");
                }
            }
        };
        //监听APP升级状态
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeFailed(boolean b) {
                if (isDebug) {
                    Log.d(TAG, "upgradeStateListener upgrade fail");
                }
            }

            @Override
            public void onUpgradeSuccess(boolean b) {
                if (isDebug) {
                    Log.d(TAG, "upgradeStateListener upgrade success");
                }
            }

            @Override
            public void onUpgradeNoVersion(boolean b) {
                if (isDebug) {
                    Log.d(TAG, "upgradeStateListener upgrade has no new version");
                }
            }

            @Override
            public void onUpgrading(boolean b) {
                if (isDebug) {
                    Log.d(TAG, "upgradeStateListener upgrading");
                }
            }

            @Override
            public void onDownloadCompleted(boolean b) {
                if (isDebug) {
                    Log.d(TAG, "upgradeStateListener download apk file success");
                }
            }
        };
        Bugly.init(context, appId, isDebug);
    }

    public void checkUpgrade() {
        Beta.checkUpgrade();

    }

    public UpgradeInfo getUpgradeInfo() {
        return Beta.getUpgradeInfo();
    }
}
