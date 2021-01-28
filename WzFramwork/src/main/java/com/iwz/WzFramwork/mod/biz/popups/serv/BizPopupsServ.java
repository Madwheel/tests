package com.iwz.WzFramwork.mod.biz.popups.serv;

import com.iwz.WzFramwork.base.CommonRes;
import com.iwz.WzFramwork.base.JBase;
import com.iwz.WzFramwork.base.api.ServApi;
import com.iwz.WzFramwork.base.interfaces.IResCallback;
import com.iwz.WzFramwork.mod.biz.popups.BizPopupsMain;
import com.iwz.WzFramwork.mod.biz.popups.model.DialogItemInfo;
import com.iwz.WzFramwork.mod.constants.FMAppConstants;
import com.iwz.WzFramwork.mod.net.http.NetHttpMain;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * 作者：小辉
 * 时间：2020/02/04
 */
public class BizPopupsServ extends ServApi {

    private BizPopupsMain mMain;

    protected BizPopupsServ(BizPopupsMain main) {
        super(main);
        this.mMain = main;
    }

    private static BizPopupsServ mInstance;

    public static BizPopupsServ getInstance(BizPopupsMain main) {
        if (mInstance == null) {
            synchronized (BizPopupsServ.class) {
                if (mInstance == null) {
                    mInstance = new BizPopupsServ(main);
                }
            }
        }
        return mInstance;
    }

    @Override
    public void born() {
        super.born();
    }

    /**
     * 全站页面弹窗获取一条
     *
     * @param plat 平台 1：王者财经 2：斗kApp 3：电脑网页 4：移动网页 5：玄商app
     * @param page 页面 1：发现页 2：我的页 3：斗k首页 4：商城首页 5：情报首页 6:玄商首页
     */
    public void obtainUnifiedPopFetch(int plat, int page, IResCallback<DialogItemInfo> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("plat", String.valueOf(plat));
        params.put("page", String.valueOf(page));
        NetHttpMain.getInstance().pServApi.reqGetResByWzApi(DialogItemInfo.class, FMAppConstants.MESSAGE_DIALOG_FETCH, params, callback);
    }

    /**
     * 统一弹窗用户动作
     *
     * @param id     弹窗id
     * @param action 1：关闭 2：点击浏览
     */
    public void unifiedPopActiondo(int id, int action) {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("action", String.valueOf(action));
        NetHttpMain.getInstance().pServApi.reqGetResByWzApi(JBase.class, FMAppConstants.MESSAGE_DIALOG_ACTIONDO,
                params, new IResCallback<JBase>() {
                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onFinish(CommonRes<JBase> res) {

                    }
                });
    }
}
