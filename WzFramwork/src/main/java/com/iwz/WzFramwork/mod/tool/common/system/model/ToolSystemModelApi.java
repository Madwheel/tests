package com.iwz.WzFramwork.mod.tool.common.system.model;

import com.iwz.WzFramwork.base.api.ModelApi;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/12/99:48
 * desc   :
 */
public class ToolSystemModelApi extends ModelApi {
    private static ToolSystemModelApi mToolSystemModelApi = null;
    private JVersion mVersion;

    public static ToolSystemModelApi getInstance(ToolSystemMain main) {
        synchronized (ToolSystemModelApi.class) {
            if (mToolSystemModelApi == null) {
                mToolSystemModelApi = new ToolSystemModelApi(main);
            }
        }
        return mToolSystemModelApi;
    }

    protected ToolSystemModelApi(ToolSystemMain main) {
        super(main);
        mVersion = new JVersion();
    }

    public JVersion getVersion() {
        return mVersion;
    }

    public void setVersion(JVersion version) {
        this.mVersion = version;
    }
}
