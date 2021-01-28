package com.iwz.WzFramwork.mod.tool.format;

import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.tool.format.serv.ToolFormatServApi;

import java.util.List;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/22
 */
public class ToolFormatMain extends ModMain {
    private ToolFormatServApi mServ;

    @Override
    public String getModName() {
        return "ToolFormatMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_TOOL;
    }

    private static ToolFormatMain mToolFormatMain;

    public static ToolFormatMain getInstance() {
        if (mToolFormatMain == null) {
            synchronized (ToolFormatMain.class) {
                if (mToolFormatMain == null) {
                    mToolFormatMain = new ToolFormatMain();
                }
            }
        }
        return mToolFormatMain;
    }

    @Override
    public void born() {
        super.born();
        mServ = ToolFormatServApi.getInstance(this);
    }

    public String formatDecimal(int digits, Object count) {
        return mServ.formatDecimal(digits, count);
    }

    /**
     * 提供getMD5(String)方法
     *
     * @param str 需要进行MD5加密的字符串
     * @return 加密后返回的字符串
     */
    public String formatMD5(String str) {
        return mServ.formatMD5(str);
    }

    public boolean compareArraylist(List<String> arraylist1, List<String> arraylist2) {
        return mServ.compareArraylist(arraylist1, arraylist2);
    }
}
