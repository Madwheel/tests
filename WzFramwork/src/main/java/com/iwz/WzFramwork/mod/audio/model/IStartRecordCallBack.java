package com.iwz.WzFramwork.mod.audio.model;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/09/27
 */
public interface IStartRecordCallBack {
    void onPrepare();

    void onStart();

    void onError(Object e, String path);

    void recordOver(int duration);
}
