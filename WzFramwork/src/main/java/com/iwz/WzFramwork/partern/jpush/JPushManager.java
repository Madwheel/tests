package com.iwz.WzFramwork.partern.jpush;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.iwz.WzFramwork.WzFramworkApplication;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JPushManager {
    private static JPushManager mInstance;
    private String registrationID = "";

    public static JPushManager getInstance() {
        if (mInstance == null) {
            synchronized (JPushManager.class) {
                if (mInstance == null) {
                    mInstance = new JPushManager();
                }
            }
        }
        return mInstance;
    }

    public void initJpush(IJpushListener listener) {
        try {
            // 注册极光
            JPushInterface.setDebugMode(false);
            JPushInterface.init(WzFramworkApplication.getmContext());
            registrationID = JPushInterface.getRegistrationID(WzFramworkApplication.getmContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (listener != null) {
            listener.onSuccess(registrationID);
        }
    }

    /**
     * 设置极光推送的别名
     */
    public void setAlias(String did) {
        if (!TextUtils.isEmpty(registrationID)) {
            String alias = did;
            // 调用 Handler 来异步设置别名
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
        }
    }

    private String TAG = "setAlias";
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    try {
                        JPushInterface.setAliasAndTags(WzFramworkApplication.getmContext(),
                                (String) msg.obj,
                                null,
                                mAliasCallback);
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
}