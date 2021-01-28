package com.iwz.WzFramwork.partern.unifypay;

import android.app.Activity;

import com.chinaums.pppay.unify.UnifyPayListener;
import com.chinaums.pppay.unify.UnifyPayPlugin;
import com.chinaums.pppay.unify.UnifyPayRequest;
import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.interfaces.IPayResponse;
import com.unionpay.UPPayAssistEx;

public class UnifyPayUtil {
    private static UnifyPayUtil mInstance;

    public static UnifyPayUtil getInstance() {
        if (mInstance == null) {
            synchronized (UnifyPayUtil.class) {
                if (mInstance == null) {
                    mInstance = new UnifyPayUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 微信
     *
     * @param parms
     */
    public void payUnifyWX(String parms, final IPayResponse payResponse) {
        UnifyPayPlugin instance = UnifyPayPlugin.getInstance(WzFramworkApplication.getmContext());
        UnifyPayRequest msg = new UnifyPayRequest();
        msg.payChannel = UnifyPayRequest.CHANNEL_WEIXIN;
        msg.payData = parms;
//        msg.payData = appRequestData;
        instance.setListener(new UnifyPayListener() {
            @Override
            public void onResult(String resultCode, String resultInfo) {
                payResponse.onSuccess(String.valueOf(resultCode), resultInfo);
                /*
                 * 根据返回的支付结果进行处理
                 */
                if (resultCode == "0000") {
                    //支付成功
                } else {
                    //其他
                }
            }
        });
        instance.sendPayRequest(msg);
    }

    /**
     * 支付宝
     *
     * @param parms
     */
    public void payAliPay(String parms, Activity activity, final IPayResponse payResponse) {
        UnifyPayPlugin instance = UnifyPayPlugin.getInstance(activity);
        UnifyPayRequest msg = new UnifyPayRequest();
        msg.payChannel = UnifyPayRequest.CHANNEL_ALIPAY;
        msg.payData = parms;
        instance.setListener(new UnifyPayListener() {
            @Override
            public void onResult(String resultCode, String resultInfo) {
                payResponse.onSuccess(String.valueOf(resultCode), resultInfo);
                /*
                 * 根据返回的支付结果进行处理
                 */
                if (resultCode == "0000") {
                    //支付成功
                } else {
                    //其他
                }
            }
        });
        instance.sendPayRequest(msg);
    }

    public void payCloudQuickPay(Activity activity, String parms) {
        UPPayAssistEx.startPay(activity, null, null, parms, "00");
    }
}