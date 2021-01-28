package com.iwz.WzFramwork.mod.biz.initiate.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.WzBaseActivity;
import com.iwz.WzFramwork.mod.biz.adv.BizAdvMain;
import com.iwz.WzFramwork.mod.biz.initiate.BizInitiateMain;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;
import com.iwz.wzframwork.R;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends WzBaseActivity {
    private BizInitiateMain mMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mMain = BizInitiateMain.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                if (!TextUtils.isEmpty(data.getQueryParameter("url"))) {
                    WzFramworkApplication.getmRouter().toUri(SplashActivity.this, data.toString());
                    finish();
                    return;
                }
            }
        }
        initData();
    }

    @Override
    protected String getPageName() {
        return "BizSplash";
    }

    //初始化数据
    private void initData() {
//        if (mMain.getpControl().getJInitiateInfo().getFirstUse() || mMain.getpControl().getJInitiateInfo().getGuideCount() != FMAppConstants.GUIDE_CODE) {
//            jumpToGuide();
//            return;
//        }
        if (!BizAdvMain.getInstance().isAdvExist(ToolSystemMain.getInstance().confApi.getmConf().getLaunchPageKey(), ToolSystemMain.getInstance().confApi.getmConf().getLaunchPosKey())) {
            WzFramworkApplication.getmRouter().jumpToMain(SplashActivity.this, false);
            Map<Object, Object> parmas = new HashMap<>();
            parmas.put("isCacheToDb", true);
            BizAdvMain.getInstance().preLoadAdv(ToolSystemMain.getInstance().confApi.getmConf().getLaunchPageKey(), ToolSystemMain.getInstance().confApi.getmConf().getLaunchPosKey(), parmas);
            SplashActivity.this.finish();
        } else {
            jumpToSplashAd();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void jumpToGuide() {
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    private void jumpToSplashAd() {
        Intent intent = new Intent(SplashActivity.this, SplashAdActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }
}
