package com.iwz.WzFramwork.mod.audio.model;

import java.util.List;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/09/27
 */
public interface IRecordStopCallBack {
    void onSuccess(String path, List list, int duration);

    void onError(Object object, String path);

}
