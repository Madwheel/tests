package com.iwz.WzFramwork.mod.tool.format.serv;

import com.iwz.WzFramwork.base.api.ServApi;
import com.iwz.WzFramwork.mod.tool.format.ToolFormatMain;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/22
 */
public class ToolFormatServApi extends ServApi {
    private ToolFormatMain mMain;

    protected ToolFormatServApi(ToolFormatMain main) {
        super(main);
        mMain = main;
    }

    private static ToolFormatServApi mToolFormatServApi;

    public static ToolFormatServApi getInstance(ToolFormatMain main) {
        if (mToolFormatServApi == null) {
            synchronized (ToolFormatServApi.class) {
                if (mToolFormatServApi == null) {
                    mToolFormatServApi = new ToolFormatServApi(main);
                }
            }
        }
        return mToolFormatServApi;
    }

    public String formatDecimal(int digits, Object count) {
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(count));
//        科学计数法显示效果
//        NumberFormat nf = NumberFormat.getNumberInstance();
//        nf.setMaximumFractionDigits(digits);
//        return nf.format(count);
    }

    /**
     * 提供getMD5(String)方法
     *
     * @param val 需要进行MD5加密的字符串
     * @return 加密后返回的字符串
     */
    public String formatMD5(String val) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
        }
        md5.update(val.getBytes());
        byte[] bytes = md5.digest();//加密
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append(bytes[i]);
        }
        return stringBuffer.toString();
    }

    public boolean compareArraylist(List<String> arraylist1, List<String> arraylist2) {
        int size = arraylist1.size();
        if (size == arraylist2.size()) {
            for (int i = 0; i < size; i++) {
                if (arraylist1.get(i).equals(arraylist2.get(i))) {
                    if (i == size - 1) {
                        return true;
                    }
                    continue;
                }

            }
        }
        return false;
    }

}
