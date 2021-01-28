package com.iwz.WzFramwork.mod.biz.adv.serv;

import com.alibaba.fastjson.JSON;
import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.CommonRes;
import com.iwz.WzFramwork.base.JBase;
import com.iwz.WzFramwork.base.api.ServApi;
import com.iwz.WzFramwork.mod.biz.adv.BizAdvMain;
import com.iwz.WzFramwork.mod.biz.adv.model.JAdvInfo;
import com.iwz.WzFramwork.mod.net.http.interfaces.WzNetCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1513:38
 * desc   :
 */
public class BizAdvServApi extends ServApi {
    private static BizAdvServApi mWzAdvServApi = null;
    private BizAdvMain mMain;

    protected BizAdvServApi(BizAdvMain main) {
        super(main);
        mMain = main;
    }

    public static BizAdvServApi getInstance(BizAdvMain main) {
        synchronized (BizAdvServApi.class) {
            if (mWzAdvServApi == null) {
                mWzAdvServApi = new BizAdvServApi(main);
            }
        }
        return mWzAdvServApi;
    }

    public String getModName() {
        return "BizAdvServApi";
    }


    public <T extends JBase> void getAdverts(final Class<T> clazz, String pageKey, String posKey, final IResParseCallback<T> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("pageKey", pageKey);
        params.put("posKey", posKey);
        WzFramworkApplication.getmNetHttp().reqGetResByWzApi("adv/position/fetch/", params, new WzNetCallback() {
            @Override
            public void onResult(String result) {
                CommonRes<T> cRes;
                T resObj;
                try {
                    resObj = JSON.parseObject(result, clazz);
                } catch (Exception e) {
                    resObj = null;
                }
                if (resObj == null) {
                    cRes = new CommonRes<>(false, "");
                } else {
                    JBase tmp = (JBase) resObj;
                    cRes = new CommonRes<>(true, tmp.getErrorCode(), resObj, result);
                }
                callback.onFinish(cRes);
            }
        });
    }

    public void reportAdvDisplay(int mapId, int pos, int total, final WzNetCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("mapId", "" + mapId);
        params.put("pos", "" + pos);
        params.put("total", "" + total);
        WzFramworkApplication.getmNetHttp().reqGetResByWzApi("adv/position/show/", params, new WzNetCallback() {
            @Override
            public void onResult(String result) {
                callback.onResult(result);
            }
        });
    }

    public CommonRes<JAdvInfo> getJAdvInfoFromDb(String key) {
        JAdvInfo jAdvInfo;
        try {
            jAdvInfo = WzFramworkApplication.getmIoKvdb().getObject(getModName() + ":" + key, JAdvInfo.class);
        } catch (Exception e) {
            return new CommonRes<>(false);
        }
        if (jAdvInfo == null) {
            return new CommonRes<>(true, 10001);
        }
        return new CommonRes<>(true, 0, jAdvInfo);
    }

    public CommonRes<JAdvInfo> setAdvInfoToDb(String key, JAdvInfo item) {
        try {
            WzFramworkApplication.getmIoKvdb().put(getModName() + ":" + key, item);
        } catch (Exception e) {
            return new CommonRes<>(false);
        }
        return new CommonRes<>(true);
    }

}
