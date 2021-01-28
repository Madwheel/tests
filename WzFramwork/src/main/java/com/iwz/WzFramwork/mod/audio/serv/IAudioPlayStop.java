package com.iwz.WzFramwork.mod.audio.serv;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/26
 */
public interface IAudioPlayStop {
    void pauseAudioPlay(String method, int cid, int errorCode);

    void stopAudioPlay(String method, int cid, int errorCode);
}
