package com.iwz.WzFramwork.mod.statistic.serv;

import android.content.Context;

import com.iwz.WzFramwork.mod.statistic.BizStatisticMain;
import com.iwz.WzFramwork.base.api.ServApi;

import cn.jiguang.analytics.android.api.Account;
import cn.jiguang.analytics.android.api.AccountCallback;
import cn.jiguang.analytics.android.api.Event;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/12/03
 */
public class BizStatisticServApi extends ServApi {
    private BizStatisticMain mMain;

    public BizStatisticServApi(BizStatisticMain main) {
        super(main);
        mMain = main;
    }

    private static BizStatisticServApi mBizStatisticServApi;

    public static BizStatisticServApi getInstance(BizStatisticMain main) {
        if (mBizStatisticServApi == null) {
            synchronized (BizStatisticServApi.class) {
                if (mBizStatisticServApi == null) {
                    mBizStatisticServApi = new BizStatisticServApi(main);
                }
            }
        }
        return mBizStatisticServApi;
    }

    @Override
    public void born() {
        super.born();
    }

    /**
     * 设置统计上报的自动周期，未调用前默认即时上报
     *
     * @param context
     * @param period  周期，单位秒，最小10秒，最大1天，超出范围会打印调用失败日志。传0表示统计数据即时上报
     */
    public void setAnalyticsReportPeriod(Context context, int period) {
        JAnalyticsInterface.setAnalyticsReportPeriod(context, period);
    }

    /**
     * 页面启动接口。在页面(activity和fragment)的相关生命周期内调用，和onPageEnd需要成对调用
     *
     * @param context
     * @param pageName
     */
    public void onPageStart(Context context, String pageName) {
        JAnalyticsInterface.onPageStart(context, pageName);
    }

    /**
     * 页面结束接口。在页面(activity和fragment)的相关生命周期内调用，和onPageStart需要成对调用
     *
     * @param context
     * @param pageName
     */
    public void onPageEnd(Context context, String pageName) {
        JAnalyticsInterface.onPageEnd(context, pageName);
    }

    /**
     * 自定义事件。通过传入不同的事件模型来进行各种事件的统计，具体的事件模型请查看事件模型介绍
     *
     * @param context
     * @param event   事件模型，支持CountEvent(计数事件)、CalculateEvent(计算事件)、RegisterEvent(注册事件)、
     *                LoginEvent(登录事件)、BrowseEvent(浏览事件)、PurchaseEvent(购买事件)
     *                1、字符串字段（key与 value）限制大小不超过256字节，超过限制的key或value该事件将会被丢弃。
     *                2、自定义键值对数目不能超过10个，超过10个限制该事件将会被丢弃。
     */
    public void onEvent(Context context, Event event) {
        JAnalyticsInterface.onEvent(context, event);
    }

    /**
     * 登记账户信息
     *
     * @param context
     * @param uid
     * @param nickName
     * @param paid            0未知 1是 2否/不能为其他数字，默认为0
     * @param accountCallback
     */
    public void identifyAccount(Context context, String uid, String nickName, int paid, AccountCallback accountCallback) {
        Account account = new Account(uid);    //account001为账号id
//        account.setCreationTime(1513749859L);        //账户创建的时间戳
        account.setName(nickName);
//        account.setSex(1);
        account.setPaid(paid);
//        account.setBirthdate("19880920");       //"19880920"是yyyyMMdd格式的字符串
//        account.setPhone("13800000000");
//        account.setEmail("support@jiguang.cn");
        account.setExtraAttr("", "初始化账户信息失败");  //key如果为空，或者以极光内部namespace(符号$)开头，会设置失败并打印日志
        JAnalyticsInterface.identifyAccount(context, account, accountCallback);
    }

    /**
     * 解绑当前用户信息
     *
     * @param context
     * @param accountCallback
     */
    public void detachAccount(Context context, AccountCallback accountCallback) {
        JAnalyticsInterface.detachAccount(context, accountCallback);
    }
}
