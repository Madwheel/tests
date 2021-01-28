package com.iwz.WzFramwork.mod.biz.initiate.model;


import com.iwz.WzFramwork.base.JBase;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/03
 */
public class JInitiateInfo extends JBase {
    private Boolean isFirstUse;
    private int guideCount;

    public JInitiateInfo() {
        this.isFirstUse = true;
        this.guideCount = 0;
    }

    public int getGuideCount() {
        return guideCount;
    }

    public void setGuideCount(int guideCount) {
        this.guideCount = guideCount;
    }

    public Boolean getFirstUse() {
        return isFirstUse;
    }

    public void setFirstUse(Boolean firstUse) {
        isFirstUse = firstUse;
    }
}
