package com.iwz.WzFramwork.mod.biz.initiate;

import com.iwz.WzFramwork.mod.biz.initiate.control.BizInitiateControlApp;
import com.iwz.WzFramwork.mod.biz.initiate.model.BizInitiateModelApi;
import com.iwz.WzFramwork.mod.biz.initiate.serv.BizInitiateServApi;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/03
 */
public class BizInitiateMain extends ModMain {
    public BizInitiateModelApi pModel;
    public BizInitiateServApi pServ;
    public BizInitiateControlApp pControl;

    @Override
    public String getModName() {
        return "BizInitiateMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_BIZ;
    }

    private static BizInitiateMain mBizInitiateMain;

    public static BizInitiateMain getInstance() {
        if (mBizInitiateMain == null) {
            synchronized (BizInitiateMain.class) {
                if (mBizInitiateMain == null) {
                    mBizInitiateMain = new BizInitiateMain();
                }
            }
        }
        return mBizInitiateMain;
    }

    @Override
    public void born() {
        super.born();
        pModel = BizInitiateModelApi.getInstance(this);
        pModel.born();
        pServ = BizInitiateServApi.getInstance(this);
        pControl = BizInitiateControlApp.getInstance(this);
        pControl.born();
    }

    public BizInitiateControlApp getpControl() {
        return pControl;
    }
}
