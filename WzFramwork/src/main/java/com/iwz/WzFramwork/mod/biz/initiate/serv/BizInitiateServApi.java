package com.iwz.WzFramwork.mod.biz.initiate.serv;

import com.iwz.WzFramwork.base.CommonRes;
import com.iwz.WzFramwork.base.EErrorCode;
import com.iwz.WzFramwork.base.JBase;
import com.iwz.WzFramwork.base.api.ServApi;
import com.iwz.WzFramwork.base.interfaces.IResCallback;
import com.iwz.WzFramwork.mod.biz.initiate.BizInitiateMain;
import com.iwz.WzFramwork.mod.biz.initiate.model.JAdvert;
import com.iwz.WzFramwork.mod.biz.initiate.model.JAdvertInfo;
import com.iwz.WzFramwork.mod.biz.initiate.model.JInitiateInfo;
import com.iwz.WzFramwork.mod.constants.FMAppConstants;
import com.iwz.WzFramwork.mod.io.kvdb.IoKvdbMain;
import com.iwz.WzFramwork.mod.net.http.NetHttpMain;
import com.snappydb.SnappydbException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/03
 */
public class BizInitiateServApi extends ServApi {
    public BizInitiateMain mMain;

    public BizInitiateServApi(BizInitiateMain main) {
        super(main);
        mMain = main;
    }

    private static BizInitiateServApi mBizInitiateServApi;

    public static BizInitiateServApi getInstance(BizInitiateMain main) {
        if (mBizInitiateServApi == null) {
            synchronized (BizInitiateServApi.class) {
                if (mBizInitiateServApi == null) {
                    mBizInitiateServApi = new BizInitiateServApi(main);
                }
            }
        }
        return mBizInitiateServApi;
    }


    public void getAdvertInfo(final IResCallback<JAdvert> callback) {
        Map<String, String> params = new HashMap<>();
        NetHttpMain.getInstance().getServApi().reqGetResByWzCms(JAdvert.class, FMAppConstants.ADV_FETCH, params, new IResCallback<JAdvert>() {
            @Override
            public void onFailure(IOException e) {

            }

            public void onFinish(CommonRes<JAdvert> res) {
                callback.onFinish(res);
            }
        });
    }

    public CommonRes<JBase> setJInitiateToDb(JInitiateInfo item) {
        try {
            IoKvdbMain.getInstance().getGlobalDb().put(mMain.getModName() + ":initiate", item);
        } catch (SnappydbException e) {
            return new CommonRes<>(false);
        }
        return new CommonRes<>(true);

    }

    public CommonRes<JInitiateInfo> getInitiateFromDb() {
        JInitiateInfo info;
        try {
            info = IoKvdbMain.getInstance().getGlobalDb().getObject(mMain.getModName() + ":initiate", JInitiateInfo.class);
        } catch (SnappydbException e) {
            e.printStackTrace();
            return new CommonRes<>(false);
        }
        if (info == null) {
            return new CommonRes<>(true, EErrorCode.NO_OBJECT.ordinal());
        }
        return new CommonRes<>(true, 0, info);
    }

    public CommonRes<JAdvertInfo> getAdvertInfoFromDb() {
        JAdvertInfo info;
        try {
            info = IoKvdbMain.getInstance().getGlobalDb().getObject(mMain.getModName() + ":advert", JAdvertInfo.class);
        } catch (SnappydbException e) {
            return new CommonRes<>(false);
        }
        if (info == null) {
            return new CommonRes<>(true, EErrorCode.NO_OBJECT.ordinal());
        }
        return new CommonRes<>(true, 0, info);
    }

    public CommonRes<JBase> setAdvertInfoToDb(JAdvertInfo advertInfoInfo) {
        try {
            IoKvdbMain.getInstance().getGlobalDb().put(mMain.getModName() + ":advert", advertInfoInfo);
        } catch (SnappydbException e) {
            return new CommonRes<>(false);
        }
        return new CommonRes<>(true);
    }
}
