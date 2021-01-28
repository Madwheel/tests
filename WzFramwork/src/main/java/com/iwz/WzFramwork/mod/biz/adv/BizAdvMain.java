package com.iwz.WzFramwork.mod.biz.adv;

import android.app.Activity;

import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.biz.adv.control.BizAdvControlApp;
import com.iwz.WzFramwork.mod.biz.adv.model.BizAdvModelApi;
import com.iwz.WzFramwork.mod.biz.adv.serv.BizAdvServApi;
import com.iwz.WzFramwork.mod.biz.adv.view.AdvView;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:29
 * desc   :
 */
public class BizAdvMain extends ModMain {
    private static BizAdvMain mWzAdvApplication = null;
    public BizAdvControlApp mControlApp;
    public BizAdvServApi mServApi;
    public BizAdvModelApi mModelApi;
    private Map<Object, Object> mOption;

    public static BizAdvMain getInstance() {
        synchronized (BizAdvMain.class) {
            if (mWzAdvApplication == null) {
                mWzAdvApplication = new BizAdvMain();
            }
        }
        return mWzAdvApplication;
    }

    @Override
    public String getModName() {
        return "BizAdvMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_BIZ;
    }

    private BizAdvMain() {
        mModelApi = BizAdvModelApi.getInstance(this);
        mServApi = BizAdvServApi.getInstance(this);
        mControlApp = BizAdvControlApp.getInstance(this);
    }

    public void init(Map<Object, Object> option) {
        this.mOption = option;
    }

    public void preLoadAdv(String pageKey, String posKey, Map<Object, Object> option) {
        mControlApp.loadAdv(pageKey, posKey, option, null);
    }

    public void createAdv(Activity activity, String pageKey, String posKey, AdvView view, Map<Object, Object> option) {
        Activity activityWeakReference = new WeakReference<>(activity).get();
        mControlApp.createAdv(activityWeakReference, pageKey, posKey, view, option);
    }

    public boolean isAdvExist(String pageKey, String posKey) {
        return mControlApp.isAdvExist(pageKey, posKey);
    }

    public Map<Object, Object> getmOption() {
        return mOption;
    }

    public void setmOption(Map<Object, Object> mOption) {
        this.mOption = mOption;
    }
}
