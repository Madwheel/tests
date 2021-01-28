package com.iwz.WzFramwork.base;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/9/213:33
 * desc   :
 */
public class JBase {
    private int errorCode;

    public JBase() {
        this.errorCode = 0;
    }

    @JSONField(name = "error_code")
    public int getErrorCode() {
        return errorCode;
    }

    @JSONField(name = "error_code")
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
