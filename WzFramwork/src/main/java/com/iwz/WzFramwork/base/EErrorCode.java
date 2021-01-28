package com.iwz.WzFramwork.base;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/9/213:19
 * desc   : 错误码枚举
 */
public enum EErrorCode {
    OK(0),
    NO_OBJECT(10001),
    INNER_ERROR(10007);

    private final int value;

    EErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
