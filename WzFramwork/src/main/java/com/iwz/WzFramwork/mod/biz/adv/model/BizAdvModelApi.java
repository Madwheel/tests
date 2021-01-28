package com.iwz.WzFramwork.mod.biz.adv.model;

import com.iwz.WzFramwork.base.api.ModelApi;
import com.iwz.WzFramwork.mod.biz.adv.BizAdvMain;

import java.util.HashMap;
import java.util.Map;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:52
 * desc   :
 */
public class BizAdvModelApi extends ModelApi {
    private static BizAdvModelApi mWzAdvModelApi = null;


    public static BizAdvModelApi getInstance(BizAdvMain main) {
        synchronized (BizAdvModelApi.class) {
            if (mWzAdvModelApi == null) {
                mWzAdvModelApi = new BizAdvModelApi(main);
            }
        }
        return mWzAdvModelApi;
    }

    private Map<String, JAdvInfo> advInfoMap;
    private SplashAdInfo splashAdInfo;

    private BizAdvModelApi(BizAdvMain main) {
        super(main);
        advInfoMap = new HashMap<>();
        splashAdInfo = new SplashAdInfo();
    }

    public Map<String, JAdvInfo> getAdvInfoMap() {
        return advInfoMap;
    }

    public SplashAdInfo getSplashAdInfo() {
        return splashAdInfo;
    }

    public void setSplashAdInfo(SplashAdInfo splashAdInfo) {
        this.splashAdInfo = splashAdInfo;
    }

    public void setAdvInfoMap(Map<String, JAdvInfo> advInfoMap) {
        this.advInfoMap = advInfoMap;
    }

}
