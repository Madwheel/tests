package com.iwz.WzFramwork.partern.ali.pay;

import android.app.Activity;

import com.iwz.WzFramwork.base.interfaces.IPayResponse;

public class AliPayManager {
    private static AliPayManager mInstance;

    public static AliPayManager getInstance() {
        if (mInstance == null) {
            synchronized (AliPayManager.class) {
                if (mInstance == null) {
                    mInstance = new AliPayManager();
                }
            }
        }
        return mInstance;
    }

    public void pay(IPayResponse listener, String orderInfo, Activity activity) {
        AliPay.getInstance().intListener(listener);
        AliPay.getInstance().pay(orderInfo, activity);
    }
}
