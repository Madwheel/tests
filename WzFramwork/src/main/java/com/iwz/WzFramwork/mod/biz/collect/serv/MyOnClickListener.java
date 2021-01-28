package com.iwz.WzFramwork.mod.biz.collect.serv;


import android.view.View;

import com.iwz.WzFramwork.mod.biz.collect.BizCollectMain;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;

public class MyOnClickListener implements View.OnClickListener {
    private View.OnClickListener mUserOnClick;
    private String mPage;
    private String mName;
    private String mUrl;

    public MyOnClickListener(Class page, String name, String url, View.OnClickListener userOnClick) {
        this.mPage = page.getSimpleName();
        this.mName = name;
        this.mUrl = url;
        mUserOnClick = userOnClick;
    }

    public MyOnClickListener(Class page, String name, View.OnClickListener userOnClick) {
        this.mPage = page.getSimpleName();
        this.mName = name;
        this.mUrl = "";
        mUserOnClick = userOnClick;
    }

    public MyOnClickListener(View.OnClickListener userOnClick) {
        this.mPage = "";
        this.mName = "";
        this.mUrl = "";
        mUserOnClick = userOnClick;
    }

    public void onClick(View view) {
        if (ToolSystemMain.getInstance().getControlApp().isFastClick()) {
            return;
        }
        //BizCollectMain.getInstance().addCollect("click:"+view.getId());
        BizCollectMain.getInstance().addCollect(mPage, mName, mUrl, "click:" + view.getResources().getResourceEntryName(view.getId()));
        mUserOnClick.onClick(view);
    }
}
