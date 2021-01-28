package com.iwz.WzFramwork.partern.ali.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.iwz.WzFramwork.base.interfaces.IPayResponse;

import java.util.Map;

public class AliPay {
    private static AliPay mInstance;
    private IPayResponse mListener;

    public static AliPay getInstance() {
        if (mInstance == null) {
            synchronized (AliPay.class) {
                if (mInstance == null) {
                    mInstance = new AliPay();
                }
            }
        }
        return mInstance;
    }

    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    if (TextUtils.equals(resultStatus, "9000")) {//支付成功
                        if (mListener != null) {
                            mListener.onSuccess(resultStatus, resultInfo);
                        }
                    } else if (TextUtils.equals(resultStatus, "6001")) {//用户取消
                        if (mListener != null) {
                            mListener.onFailed(resultStatus, resultInfo);
                        }
                    } else {//支付失败
                        if (mListener != null) {
                            mListener.onFailed(resultStatus, resultInfo);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    public void intListener(IPayResponse listener) {
        this.mListener = listener;
    }

    public void pay(final String orderInfo, final Activity activity) {
        //异步调用支付
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
