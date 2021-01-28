package com.iwz.WzFramwork.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.iwz.WzFramwork.mod.tool.permission.request.IRequestPermissions;
import com.iwz.WzFramwork.mod.tool.permission.request.RequestPermissions;
import com.iwz.WzFramwork.mod.tool.permission.requestresult.IRequestPermissionsResult;
import com.iwz.WzFramwork.mod.tool.permission.requestresult.RequestPermissionsResultSetApp;

public abstract class WzBaseActivity extends FragmentActivity {
    public IRequestPermissions requestPermissions;
    public IRequestPermissionsResult requestPermissionsResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        requestPermissions = RequestPermissions.getInstance();
        requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置状态栏背景状态
     */
    @SuppressLint("InlinedApi")
    private void initSystemBar() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setStatusBar(false);
    }

    public void setStatusBar(boolean useStatusBarColor) {
        // 当系统版本为5.0以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (useStatusBarColor) {//设置状态栏文字为白色
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {//设置状态栏文字为黑色
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            // 跟app同颜色的状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //自定义状态栏背景
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    protected abstract String getPageName();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults);
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public android.content.res.Resources getResources() {
        android.content.res.Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            android.content.res.Configuration newConfig = new android.content.res.Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;

    }
}
