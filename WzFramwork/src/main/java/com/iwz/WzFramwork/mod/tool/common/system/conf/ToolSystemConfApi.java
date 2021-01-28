package com.iwz.WzFramwork.mod.tool.common.system.conf;

import android.graphics.Typeface;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.api.ConfApi;
import com.iwz.WzFramwork.mod.core.config.CoreConfigMain;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;
import com.iwz.WzFramwork.mod.tool.common.system.model.JAppConstantsConf;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/12/99:58
 * desc   :
 */
public class ToolSystemConfApi extends ConfApi {
    private static ToolSystemConfApi mToolSystemConf = null;
    private JAppConstantsConf mConf;

    public static ToolSystemConfApi getInstance(ToolSystemMain main) {
        synchronized (ToolSystemConfApi.class) {
            if (mToolSystemConf == null) {
                mToolSystemConf = new ToolSystemConfApi(main);
            }
        }
        return mToolSystemConf;
    }

    protected ToolSystemConfApi(ToolSystemMain main) {
        super(main);
    }
    private Typeface pingFang;
    @Override
    public void born() {
        mConf = CoreConfigMain.getInstance().getModConf("mod.tool.appconstants", JAppConstantsConf.class);
        if (mConf == null) {
            mConf = new JAppConstantsConf();
        }
        pingFang = Typeface.createFromAsset(WzFramworkApplication.getmContext().getAssets(), "fonts/PingFang.ttf");
    }



    public Typeface getPingFang() {
        if (pingFang == null) {
            pingFang = Typeface.createFromAsset(WzFramworkApplication.getmContext().getAssets(), "fonts/PingFang.ttf");
        }
        return pingFang;
    }
    public JAppConstantsConf getmConf() {
        return mConf;
    }
}
