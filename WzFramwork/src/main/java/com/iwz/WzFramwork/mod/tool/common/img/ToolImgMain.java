package com.iwz.WzFramwork.mod.tool.common.img;

import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.tool.common.img.serv.ToolImgServApi;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/09
 */
public class ToolImgMain extends ModMain {
    public ToolImgServApi mServ;

    @Override
    public String getModName() {
        return "ToolImgMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_TOOL;
    }

    private static ToolImgMain mToolImgMain;

    public static ToolImgMain getInstance() {
        if (mToolImgMain == null) {
            synchronized (ToolImgMain.class) {
                if (mToolImgMain == null) {
                    mToolImgMain = new ToolImgMain();
                }
            }
        }
        return mToolImgMain;
    }

    @Override
    public void born() {
        super.born();
        mServ = ToolImgServApi.getInstance(this);
    }
}
