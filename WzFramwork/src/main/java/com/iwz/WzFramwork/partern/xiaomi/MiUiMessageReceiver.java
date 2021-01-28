package com.iwz.WzFramwork.partern.xiaomi;

import android.content.Context;
import android.util.Log;

//import com.caogenwang.douk.mod.sdk.push.SdkPushMain;
import com.iwz.WzFramwork.mod.sdk.push.SdkPushMain;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * author : 小辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/6/39:33
 * desc   :
 */
public class MiUiMessageReceiver extends PushMessageReceiver {
    private String mRegId;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        Log.e("MINI", message.getContent());
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {//点击消息处理
        try {
            JSONObject jsonObject = new JSONObject(message.getContent());
            String url = jsonObject.getString("url");
            Log.e("MIMI", "" + url);
            //收到推送并点击统计
            SdkPushMain.getInstance().getmControl().toUri(context, url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {//接收到消息处理方式
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                SdkPushMain.getInstance().getmControl().launchMiId(mRegId);
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mAccount = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mAccount = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mStartTime = cmdArg1;
//                mEndTime = cmdArg2;
            }
        }
    }

    @Override
    public void onReceiveMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceiveMessage(context, miPushMessage);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                Log.e("MIMI", "1111" + mRegId);
                SdkPushMain.getInstance().getmControl().launchMiId(mRegId);
            }
        }
    }
}
