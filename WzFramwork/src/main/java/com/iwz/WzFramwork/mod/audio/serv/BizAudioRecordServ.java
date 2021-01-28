package com.iwz.WzFramwork.mod.audio.serv;

import android.media.MediaRecorder;

import com.iwz.WzFramwork.mod.audio.BizAudioMain;
import com.iwz.WzFramwork.base.api.ServApi;

import java.io.File;
import java.io.IOException;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/09/26
 */
public class BizAudioRecordServ extends ServApi {

    protected BizAudioRecordServ(BizAudioMain main) {
        super(main);
    }

    private static BizAudioRecordServ mToolAudioServ;

    public static BizAudioRecordServ getInstance(BizAudioMain main) {
        if (mToolAudioServ == null) {
            synchronized (BizAudioRecordServ.class) {
                if (mToolAudioServ == null) {
                    mToolAudioServ = new BizAudioRecordServ(main);
                }
            }
        }
        return mToolAudioServ;
    }

    @Override
    public void born() {
        super.born();
    }

    private MediaRecorder mMediaRecorder;//媒体录音管理器对象
    private IToolAudiioServCallBack mCallBack;
    private String mPath;

    /**
     * 准备录制语音
     *
     * @param maxLength 最大录音时长
     * @param path      录音缓存路径
     * @param callBack  回调
     */
    public void start(int maxLength, String path, IToolAudiioServCallBack callBack) {
        mPath = path;
        mCallBack = callBack;
        initMediaRecorder();
        preMediaRecorder(maxLength);
        startMediaRecorder();
    }

    private void preMediaRecorder(int maxLength) {
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);//设置录音采集源，VOICE_COMMUNICATION可以大大改善语音质量
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setMaxDuration(maxLength);
        mMediaRecorder.setOutputFile(new File(mPath).getAbsolutePath());
        try {
            mMediaRecorder.prepare();
            if (mCallBack != null) {
                mCallBack.onPrepare();
            }
        } catch (IOException e) {
            if (mCallBack != null) {
                mCallBack.onError("prepare初始化失败！", mPath);
            }
        }
    }

    private void initMediaRecorder() {
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        } else {
            try {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                mMediaRecorder.release();
            } catch (Exception var2) {
                mMediaRecorder.reset();
            }
        }
    }

    /**
     * 开始录制
     */
    private void startMediaRecorder() {
        if (mMediaRecorder == null) {
            if (mCallBack != null) {
                mCallBack.onError("未初始化MediaRecorder！", mPath);
            }
            return;
        }
        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if (what == 800) {//当时间达到时间上限时
                    if (mCallBack != null) {
                        mCallBack.recordOver();
                    }
                }
            }
        });
        this.mMediaRecorder.start();
        if (mCallBack != null) {
            mCallBack.onStart();
        }
    }

    /**
     * 停止录音录制
     */
    public void stop(IRecordStopCallBack stopCallBack) {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            if (stopCallBack != null) {
                stopCallBack.onStop(mPath);
            }
        }
    }

    /**
     * 停止录音录制
     */
    public void stop() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            File path = new File(mPath);
            if (path.exists()) {
                path.delete();
            }
            if (mCallBack != null) {
                mCallBack.recordOver();
            }
        }
    }
}
