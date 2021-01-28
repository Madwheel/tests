package com.iwz.WzFramwork.mod.biz.popups;

import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.biz.popups.control.BizPopupsControlApp;
import com.iwz.WzFramwork.mod.biz.popups.model.BizPopupsModel;
import com.iwz.WzFramwork.mod.biz.popups.serv.BizPopupsServ;

/**
 * 描述：
 * 作者：小辉
 * 时间：2020/02/04
 */
public class BizPopupsMain extends ModMain {

    @Override
    public String getModName() {
        return "BizPopupsMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_BIZ;
    }

    private static BizPopupsMain mInstance;

    public static BizPopupsMain getInstance() {
        if (mInstance == null) {
            synchronized (BizPopupsMain.class) {
                if (mInstance == null) {
                    mInstance = new BizPopupsMain();
                }
            }
        }
        return mInstance;
    }

    public BizPopupsModel pModel;
    public BizPopupsServ pServ;
    public BizPopupsControlApp pControl;

    @Override
    public void born() {
        super.born();
        pModel = BizPopupsModel.getInstance(this);
        pModel.born();
        pServ = BizPopupsServ.getInstance(this);
        pControl = BizPopupsControlApp.getInstance(this);
        pControl.born();
    }

    public BizPopupsControlApp getControl() {
        return pControl;
    }
}
