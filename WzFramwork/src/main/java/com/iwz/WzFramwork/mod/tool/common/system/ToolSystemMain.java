package com.iwz.WzFramwork.mod.tool.common.system;

import android.os.Environment;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.tool.common.system.conf.ToolSystemConfApi;
import com.iwz.WzFramwork.mod.tool.common.system.control.ToolSystemControlApp;
import com.iwz.WzFramwork.mod.tool.common.system.model.ToolSystemModelApi;
import com.iwz.WzFramwork.mod.tool.common.system.serv.ToolSystemServApi;

import java.io.File;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/11/1818:17
 * desc   :
 */
public class ToolSystemMain extends ModMain {
    public ToolSystemConfApi confApi;
    private ToolSystemControlApp controlApp;
    public ToolSystemServApi servApi;
    public ToolSystemModelApi modelApi;

    @Override
    public String getModName() {
        return "ToolSystemMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_TOOL;
    }

    private static ToolSystemMain mToolSystemMain = null;

    public static ToolSystemMain getInstance() {
        synchronized (ToolSystemMain.class) {
            if (mToolSystemMain == null) {
                mToolSystemMain = new ToolSystemMain();
            }
        }
        return mToolSystemMain;
    }

    private ToolSystemMain() {
        confApi = ToolSystemConfApi.getInstance(this);
        modelApi = ToolSystemModelApi.getInstance(this);
        servApi = ToolSystemServApi.getInstance(this);
        controlApp = ToolSystemControlApp.getInstance(this);
    }

    @Override
    public void born() {
        super.born();
        confApi.born();
        modelApi.born();
        servApi.born();
        controlApp.born();
    }

    public ToolSystemControlApp getControlApp() {
        return controlApp;
    }
}
