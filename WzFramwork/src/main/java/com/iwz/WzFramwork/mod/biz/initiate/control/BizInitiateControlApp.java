package com.iwz.WzFramwork.mod.biz.initiate.control;

import android.os.AsyncTask;

import com.iwz.WzFramwork.mod.constants.FMAppConstants;
import com.iwz.WzFramwork.mod.biz.initiate.BizInitiateMain;
import com.iwz.WzFramwork.mod.biz.initiate.model.JAdvert;
import com.iwz.WzFramwork.mod.biz.initiate.model.JAdvertInfo;
import com.iwz.WzFramwork.mod.biz.initiate.model.JInitiateInfo;
import com.iwz.WzFramwork.base.CommonRes;
import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.base.interfaces.IResCallback;

import java.io.IOException;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/03
 */
public class BizInitiateControlApp extends ControlApp {

    private BizInitiateMain mMain;

    public BizInitiateControlApp(BizInitiateMain main) {
        super(main);
        mMain = main;
    }

    private static BizInitiateControlApp mBizInitiateControlApp;

    public static BizInitiateControlApp getInstance(BizInitiateMain main) {
        if (mBizInitiateControlApp == null) {
            synchronized (BizInitiateControlApp.class) {
                if (mBizInitiateControlApp == null) {
                    mBizInitiateControlApp = new BizInitiateControlApp(main);
                }
            }
        }
        return mBizInitiateControlApp;
    }

    @Override
    public void born() {
        super.born();
        //首先开启支持 Fragment 自定义参数的功能
        AutoSizeConfig.getInstance().setCustomFragment(true);
        CommonRes<JInitiateInfo> infoCommonRes = mMain.pServ.getInitiateFromDb();
        if (infoCommonRes.isOk()) {
            mMain.pModel.setJInitiateInfo(infoCommonRes.getResObj());
        }
        CommonRes<JAdvertInfo> res = mMain.pServ.getAdvertInfoFromDb();
        if (res.isOk()) {
            mMain.pModel.setJAdvertInfo(res.getResObj());
        }
        reqAdvertInfo();
    }

    public void reqAdvertInfo() {
        mMain.pServ.getAdvertInfo(new IResCallback<JAdvert>() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onFinish(CommonRes<JAdvert> res) {
                if (res.isOk()) {
                    JAdvert resObj = res.getResObj();
                    setAdvertInfoInfo(resObj);
                } else if (res.getErrorCode() == FMAppConstants.ERRORCODE10001) {
                    JAdvert resObj = new JAdvert();
                    setAdvertInfoInfo(resObj);
                }
            }
        });
    }

    public JInitiateInfo getJInitiateInfo() {
        return mMain.pModel.getJInitiateInfo();
    }

    public void setJInitiateInfo(JInitiateInfo jInitiateInfo) {
        mMain.pServ.setJInitiateToDb(jInitiateInfo);
    }

    public void setJInitiateInfoAsync(final JInitiateInfo jInitiateInfo) {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... param) {
                setJInitiateInfo(jInitiateInfo);
                return null;
            }
        }.execute();
    }

    public JAdvertInfo getAdvertInfoInfo() {
        return mMain.pModel.getJAdvertInfoInfo();
    }

    public void setAdvertInfoInfo(JAdvert advertInfoInfo) {
        JAdvertInfo jAdvertInfo = new JAdvertInfo();
        jAdvertInfo.setId(advertInfoInfo.getList().getId());
        jAdvertInfo.setImg_1080_1600(advertInfoInfo.getList().getImg_1080_1600());
        jAdvertInfo.setImg_1080_1920(advertInfoInfo.getList().getImg_1080_1920());
        jAdvertInfo.setImg_1125_2436(advertInfoInfo.getList().getImg_1125_2436());
        jAdvertInfo.setTitle(advertInfoInfo.getList().getTitle());
        jAdvertInfo.setUrl(advertInfoInfo.getList().getUrl());
        mMain.pModel.setJAdvertInfo(jAdvertInfo);
        mMain.pServ.setAdvertInfoToDb(jAdvertInfo);
    }
}
