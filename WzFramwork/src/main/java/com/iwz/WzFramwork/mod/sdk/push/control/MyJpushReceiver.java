package com.iwz.WzFramwork.mod.sdk.push.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.mod.sdk.push.model.JJpushInfo;

import cn.jpush.android.api.JPushInterface;

/**
 * 描述：广播接收器，接收极光推送的消息
 * 作者：小辉
 * 时间：2019/11/03
 */
public class MyJpushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();//获得通知额外的数据
        //解析通知消息
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (extra == null) {
            return;
        }

        JJpushInfo jJpushInfo = JSON.parseObject(extra, JJpushInfo.class);
        if (jJpushInfo == null) {
            return;
        }
        String uri = jJpushInfo.getUrl();
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            //收到推送并点击统计
            //跳转URL
            //check h5 webview  type
            WzFramworkApplication.getmRouter().toUri(context, uri);
        }
    }
}