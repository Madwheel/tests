package com.iwz.WzFramwork.mod.statistic;

import com.iwz.WzFramwork.mod.statistic.control.BizStatisticControlApp;
import com.iwz.WzFramwork.mod.statistic.serv.BizStatisticServApi;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/12/03
 */
public class BizStatisticMain extends ModMain {
    public BizStatisticServApi pServ;
    public BizStatisticControlApp pControl;
    @Override
    public ModType getModType() {
        return ModType.MODE_BIZ;
    }

    @Override
    public String getModName() {
        return "BizStatisticMain";
    }

    private static BizStatisticMain mBizStatisticMain;

    public static BizStatisticMain getInstance() {
        if (mBizStatisticMain == null) {
            synchronized (BizStatisticMain.class) {
                if (mBizStatisticMain == null) {
                    mBizStatisticMain = new BizStatisticMain();
                }
            }
        }
        return mBizStatisticMain;
    }

    @Override
    public void born() {
        super.born();
        pServ = BizStatisticServApi.getInstance(this);
        pServ.born();
        pControl = BizStatisticControlApp.getInstance(this);
        pControl.born();
    }

    public BizStatisticControlApp getpControl() {
        return pControl;
    }
}
