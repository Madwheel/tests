package com.iwz.WzFramwork.mod.biz.initiate.model;

import com.iwz.WzFramwork.mod.biz.initiate.BizInitiateMain;
import com.iwz.WzFramwork.base.api.ModelApi;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/03
 */
public class BizInitiateModelApi extends ModelApi {
    private BizInitiateMain mMain;
    private JInitiateInfo jInitiateInfo;
    private JAdvertInfo jAdvertInfo;

    public BizInitiateModelApi(BizInitiateMain main) {
        super(main);
        mMain = main;
    }

    private static BizInitiateModelApi mJInitiateModelApi;

    public static BizInitiateModelApi getInstance(BizInitiateMain main) {
        if (mJInitiateModelApi == null) {
            synchronized (BizInitiateModelApi.class) {
                if (mJInitiateModelApi == null) {
                    mJInitiateModelApi = new BizInitiateModelApi(main);
                }
            }
        }
        return mJInitiateModelApi;
    }

    @Override
    public void born() {
        super.born();
        jInitiateInfo = new JInitiateInfo();
        jAdvertInfo = new JAdvertInfo();
    }

    public JInitiateInfo getJInitiateInfo() {
        return jInitiateInfo;
    }

    public void setJInitiateInfo(JInitiateInfo jInitiateInfo) {
        this.jInitiateInfo = jInitiateInfo;
    }

    public JAdvertInfo getJAdvertInfoInfo() {
        return jAdvertInfo;
    }

    public void setJAdvertInfo(JAdvertInfo advertInfo) {
        jAdvertInfo = advertInfo;
    }
}