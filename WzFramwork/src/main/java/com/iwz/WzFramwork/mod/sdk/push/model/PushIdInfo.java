package com.iwz.WzFramwork.mod.sdk.push.model;

/**
 * author : 小辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/6/1713:51
 * desc   :
 */
public class PushIdInfo {
    private String hwId;
    private String miId;
    private String jgId;

    public PushIdInfo() {
        this.hwId = "";
        this.miId = "";
        this.jgId = "";
    }

    public String getHwId() {
        return hwId;
    }

    public void setHwId(String hwId) {
        this.hwId = hwId;
    }

    public String getMiId() {
        return miId;
    }

    public void setMiId(String miId) {
        this.miId = miId;
    }

    public String getJgId() {
        return jgId;
    }

    public void setJgId(String jgId) {
        this.jgId = jgId;
    }
}
