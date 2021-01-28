package com.iwz.WzFramwork.mod.tool.common.file;

import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.tool.common.file.control.FileConrtolApp;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/09/26
 */
public class ToolFileMain extends ModMain {
    @Override
    public String getModName() {
        return "ToolFileMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_TOOL;
    }

    private static ToolFileMain mFileMain;

    public static ToolFileMain getInstance() {
        if (mFileMain == null) {
            synchronized (ToolFileMain.class) {
                if (mFileMain == null) {
                    mFileMain = new ToolFileMain();
                }
            }
        }
        return mFileMain;
    }

    public FileConrtolApp mControl;

    @Override
    public void born() {
        super.born();
        mControl = FileConrtolApp.getInstance(this);
    }
}
