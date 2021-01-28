package com.iwz.WzFramwork.mod.sdk.push.view;

import android.content.Intent;
import android.os.Bundle;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.WzBaseActivity;
import com.iwz.wzframwork.R;

import org.json.JSONObject;

import java.net.URLDecoder;

public class DeeplinkActivity extends WzBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink);
        getIntentData(getIntent());
    }

    private void getIntentData(Intent intent) {
        if (intent != null) {
            try {
                String custom = intent.getData().getQueryParameter("custom");
                JSONObject jsonObject = new JSONObject(URLDecoder.decode(custom, "utf-8"));
                String url = jsonObject.getString("url");
                WzFramworkApplication.getmRouter().toUri(DeeplinkActivity.this, url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //跳转到首页
            WzFramworkApplication.getmRouter().startSplash(DeeplinkActivity.this, "");
        }
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
    }

    @Override
    protected String getPageName() {
        return "DeeplinkActivity";
    }
}