package com.iwz.WzFramwork;

import android.content.Context;

import com.iwz.WzFramwork.base.interfaces.IAppStatus;
import com.iwz.WzFramwork.base.interfaces.IImageRecognit;
import com.iwz.WzFramwork.base.interfaces.IShare;
import com.iwz.WzFramwork.mod.MyHandlerThread;
import com.iwz.WzFramwork.base.interfaces.IRouter;
import com.iwz.WzFramwork.mod.io.kvdb.IIoKvdb;
import com.iwz.WzFramwork.mod.net.http.interfaces.INetHttp;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/9/2713:28
 * desc   :
 */
public class WzFramworkApplication {
    private static Context mContext;
    private static String mMode;
    private static String mChannel;
    private static MyHandlerThread mThread;
    private static IRouter mRouter;
    private static IImageRecognit mImageRecognit;
    private static IAppStatus mAppStatus;
    private static IShare mShare;
    private static INetHttp mNetHttp;
    private static IIoKvdb mIoKvdb;

    public static void setChannel(Context context, String mode, String channel, MyHandlerThread thread) {
        mContext = context;
        mMode = mode;
        mChannel = channel;
        mThread = thread;
    }

    public static Context getmContext() {
        return mContext;
    }

    public static String getmMode() {
        return mMode;
    }

    public static String getmChannel() {
        return mChannel;
    }

    public static MyHandlerThread getmThread() {
        return mThread;
    }

    public static void setAgency(IRouter router, IImageRecognit imageRecognit, IAppStatus appStatus, IShare share, INetHttp netHttp, IIoKvdb ioKvdb) {
        mRouter = router;
        mImageRecognit = imageRecognit;
        mAppStatus=appStatus;
        mShare=share;
        mNetHttp=netHttp;
        mIoKvdb=ioKvdb;
    }

    public static INetHttp getmNetHttp() {
        return mNetHttp;
    }

    public static IIoKvdb getmIoKvdb() {
        return mIoKvdb;
    }

    public static IRouter getmRouter() {
        return mRouter;
    }

    public static IImageRecognit getmImageRecognit() {
        return mImageRecognit;
    }

    public static IAppStatus getmAppStatus() {
        return mAppStatus;
    }

    public static IShare getmShare() {
        return mShare;
    }
}
