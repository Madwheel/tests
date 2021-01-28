package com.iwz.WzFramwork.mod.biz.initiate.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.WzBaseActivity;
import com.iwz.WzFramwork.mod.biz.adv.BizAdvMain;
import com.iwz.WzFramwork.mod.biz.adv.view.AdvView;
import com.iwz.WzFramwork.mod.constants.FMAppConstants;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;
import com.iwz.wzframwork.R;

import java.util.HashMap;
import java.util.Map;

public class SplashAdActivity extends WzBaseActivity {
    private AdvView rl_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_ad);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected String getPageName() {
        return "BizAdvert";
    }

    private void initData() {
        Map<Object, Object> parmas = new HashMap<>();
        parmas.put("isCacheToDb", true);
        BizAdvMain.getInstance().createAdv(SplashAdActivity.this, ToolSystemMain.getInstance().confApi.getmConf().getLaunchPageKey(), ToolSystemMain.getInstance().confApi.getmConf().getLaunchPosKey(), rl_ad, parmas);
    }

    private void initView() {
        rl_ad = findViewById(R.id.rl_ad);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WzFramworkApplication.getmRouter().jumpToMain(SplashAdActivity.this, false);
        finish();
        return super.onKeyDown(keyCode, event);
    }
}