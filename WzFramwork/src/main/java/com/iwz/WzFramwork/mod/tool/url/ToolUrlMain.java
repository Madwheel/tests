package com.iwz.WzFramwork.mod.tool.url;



import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ToolUrlMain extends ModMain {

    @Override
    public String getModName() {
        return "ToolUrlMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_TOOL;
    }

    private static ToolUrlMain mToolUrlMain;

    public static ToolUrlMain getInstance() {
        if (mToolUrlMain == null) {
            synchronized (ToolUrlMain.class) {
                if (mToolUrlMain == null) {
                    mToolUrlMain = new ToolUrlMain();
                }
            }
        }
        return mToolUrlMain;
    }


    public String fullfil(String url, String host) {
        String reg = "^[A-Za-z0-9_]*://.*";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return host + url;
        }
        return url;
    }

    /**
     * 解析出url参数中的键值对
     *
     * @param query 请求参数
     * @return url请求参数部分
     */
    public Map<String, String> URLRequest(String query) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        if (query == null) {
            return mapRequest;
        }
        arrSplit = query.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }
    /**
     * URLEncoder编码
     */
    public String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }
        return "";
    }

    /**
     * URLDecoder解码
     */
    public String toURLDecoder(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String url = new String(paramString.getBytes(), "UTF-8");
            url = URLDecoder.decode(url, "UTF-8");
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
