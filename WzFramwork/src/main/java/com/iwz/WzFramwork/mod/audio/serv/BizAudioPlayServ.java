package com.iwz.WzFramwork.mod.audio.serv;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.iwz.WzFramwork.mod.audio.BizAudioMain;
import com.iwz.WzFramwork.base.api.ServApi;

import java.io.File;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/26
 */
public class BizAudioPlayServ extends ServApi {
    private MediaPlayer mPlayer;
    public BizAudioMain mMain;
    private AudioManager audioManager;
    private boolean isPause = false;
    private IAudiioPlay iStartAudioPlay;

    public BizAudioPlayServ(BizAudioMain main) {
        super(main);
        mMain = main;
    }

    private static BizAudioPlayServ mToolAudioPlayServ;

    public static BizAudioPlayServ getInstance(BizAudioMain main) {
        if (mToolAudioPlayServ == null) {
            synchronized (BizAudioPlayServ.class) {
                if (mToolAudioPlayServ == null) {
                    mToolAudioPlayServ = new BizAudioPlayServ(main);
                }
            }
        }
        return mToolAudioPlayServ;
    }

    public void initAudio(Context context) {
        //音频管理器AudioManager
        if (audioManager == null) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
    }

    /**
     * 开始播放录音
     *
     * @param url      要播放的录音本地文件
     * @param listener 回调监听
     */
    public void start(Context context, String url, MediaPlayer.OnCompletionListener listener, IAudiioPlay iStartAudioPlay) {
        this.iStartAudioPlay = iStartAudioPlay;
        if (audioManager.isWiredHeadsetOn()) {
            //切换到耳机模式
            changeToHeadset();
        }
        if (!isPause) {//不是暂停播放
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
            } else {
                mPlayer.reset();
            }
            try {
                String filePath = mMain.mConf.getFilePath(url);
                if (!isUseCache(filePath)) {
                    Uri uri = Uri.parse(url);
                    mPlayer.setDataSource(context, uri);
//                    MyNetHttpMain.getInstance().getServApi().downloadFile(filePath, url);
                } else {
                    mPlayer.setDataSource(filePath);
                }
                mPlayer.prepare();
                mPlayer.start();
                if (iStartAudioPlay != null) {
                    iStartAudioPlay.startAudioPlay();
                }
                if (listener != null) {
                    mPlayer.setOnCompletionListener(listener);
                }
            } catch (Exception ex) {
            }
        } else {//是暂停播放
            mPlayer.start();//继续播放
        }
        isPause = false;
    }

    //暂停播放
    public void pause() {
        if (mPlayer.isPlaying() && !isPause) {
            mPlayer.pause();//暂停播放
            isPause = true;
        }
    }

    //停止播放
    public void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            isPause = false;
        }
    }

    //切换到耳机模式
    private void changeToHeadset() {
        audioManager.setSpeakerphoneOn(false);
    }

    private boolean isUseCache(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     *  为true打开喇叭扩音器；为false关闭喇叭扩音器.
     *  setMode跟使用听筒和扬声器没关系。只是为了避免播发语音和通话造成声音的重叠混乱。
     *
     * @param on
     */
    @SuppressWarnings("deprecation")
    private void setSpeakerphoneOn1(boolean on) {
        if (on) {
            audioManager.setSpeakerphoneOn(true);
        } else {
            audioManager.setSpeakerphoneOn(false);//关闭扬声器
            audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
//            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            audioManager.setMode(AudioManager.STREAM_VOICE_CALL);
            //把声音设定成Earpiece（听筒）出来，设定为正在通话中
            //Android 5.0版本限制使用AudioManager.MODE_IN_CALL模式了，除非是系统应用，第三方应用使用AudioManager.MODE_IN_COMMUNICATION替代之
            // audioManager.setMode(AudioManager.MODE_IN_CALL);//打电话呼叫建立时使用。
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);//建立一个音频/视频Voip呼叫。
        }
    }
}
