package com.iwz.WzFramwork.mod.audio.serv;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/09/26
 */
public interface IToolAudiioServCallBack {

    void onPrepare();

    void onStart();

    void onError(Object e, String path);

    void recordOver();

}
