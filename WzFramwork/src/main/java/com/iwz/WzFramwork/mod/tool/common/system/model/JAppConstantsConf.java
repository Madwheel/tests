package com.iwz.WzFramwork.mod.tool.common.system.model;

import com.iwz.WzFramwork.base.JBase;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/12/910:14
 * desc   :
 */
public class JAppConstantsConf extends JBase {
    private String launchPageKey;
    private String launchPosKey;

    private int svnversion;
    private int smallversion;
    private String app;

    public JAppConstantsConf() {
        this.launchPageKey = "";
        this.launchPosKey = "";
        this.svnversion = 0;
        this.smallversion = 0;
        this.app = "a";
    }

    public String getLaunchPageKey() {
        return launchPageKey;
    }

    public void setLaunchPageKey(String launchPageKey) {
        this.launchPageKey = launchPageKey;
    }

    public String getLaunchPosKey() {
        return launchPosKey;
    }

    public void setLaunchPosKey(String launchPosKey) {
        this.launchPosKey = launchPosKey;
    }

    public int getSvnversion() {
        return svnversion;
    }

    public void setSvnversion(int svnversion) {
        this.svnversion = svnversion;
    }

    public int getSmallversion() {
        return smallversion;
    }

    public void setSmallversion(int smallversion) {
        this.smallversion = smallversion;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
