package com.iwz.WzFramwork.base.interfaces;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/14
 */
public interface IPayResponse {
    /**
     * 支付成功
     * @param state 状态码
     * @param msg 消息
     */
    void onSuccess(String state, String msg);

    /**
     * 支付失败
     * @param state 状态码
     * @param msg 消息
     */
    void onFailed(String state, String msg);
}
