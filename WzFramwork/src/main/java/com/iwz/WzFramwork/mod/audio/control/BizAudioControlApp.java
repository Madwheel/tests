package com.iwz.WzFramwork.mod.audio.control;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.mod.audio.BizAudioMain;
import com.iwz.WzFramwork.mod.audio.control.event.WebviewAudioDealer;
import com.iwz.WzFramwork.mod.audio.model.IRecordStopCallBack;
import com.iwz.WzFramwork.mod.audio.model.IStartRecordCallBack;
import com.iwz.WzFramwork.mod.audio.serv.IAudiioPlay;
import com.iwz.WzFramwork.mod.audio.serv.IAudioPlayStop;
import com.iwz.WzFramwork.mod.audio.serv.IToolAudiioServCallBack;
import com.iwz.WzFramwork.mod.biz.popups.BizPopupsMain;
import com.iwz.WzFramwork.mod.tool.common.file.ToolFileMain;
import com.iwz.WzFramwork.mod.tool.permission.PermissionUtils;
import com.iwz.WzFramwork.mod.tool.permission.request.RequestPermissions;

import java.io.File;
import java.util.ArrayList;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/09/26
 */
public class BizAudioControlApp extends ControlApp {
    private BizAudioMain mMain;
    private int maxLength;
    private ArrayList<String> list;
    private String routPath;
    private long startTime;
    private long stopTime;
    //录音时长
    private int duration;
    private WebviewAudioDealer mWebviewAudioDealer;
    private IAudiioPlay iStartAudioPlay;

    protected BizAudioControlApp(BizAudioMain main) {
        super(main);
        mMain = main;
    }

    private static BizAudioControlApp mToolAudioControlApp;

    public static BizAudioControlApp getInstance(BizAudioMain main) {
        if (mToolAudioControlApp == null) {
            synchronized (BizAudioControlApp.class) {
                if (mToolAudioControlApp == null) {
                    mToolAudioControlApp = new BizAudioControlApp(main);
                }
            }
        }
        return mToolAudioControlApp;
    }

    @Override
    public void born() {
        super.born();
        mWebviewAudioDealer = WebviewAudioDealer.getInstance(mMain);
        mWebviewAudioDealer.born();
        initAudioRecorder();
        mMain.mPlayServ.initAudio(WzFramworkApplication.getmContext());
    }

    public String getRoutPath() {
        return mMain.mConf.getRoutPath();
    }

    /**
     * 初始化录制权限、路径、最大录音时长、操作
     */
    public void initAudioRecorder() {
        maxLength = '\uea60';
        list = new ArrayList<>();
        routPath = mMain.mConf.getRoutPath();
        File path = new File(routPath);
        if (!path.exists()) {
            path.mkdirs();
        }
        ToolFileMain.getInstance().mControl.delAllFile(routPath);
    }

    /**
     * 开始录音
     */
    public void startRecord(Activity activity,int audioMaxLength, final IStartRecordCallBack callDealer) {
        if (audioMaxLength > 1000) {
            maxLength = audioMaxLength;
        }
//        initPermission();
        if (!RequestPermissions.getInstance().requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, PermissionUtils.ResultCode1)) {
            callDealer.onStart();
            callDealer.recordOver(0);
        }
        record(callDealer);
    }

    /**
     * 继续录音
     */
    public void resumeRecord(final IStartRecordCallBack callDealer) {
        maxLength = maxLength - duration;
        record(callDealer);
    }

    /**
     * 停止录音
     *
     * @param callDealer 停止录音接口代理
     * @param stopType   停止类型：0，取消录音；1，暂停录音；2，停止录音
     */
    public void stopRecord(final IRecordStopCallBack callDealer, final int stopType) {
        mMain.mRecordServ.stop(new com.iwz.WzFramwork.mod.audio.serv.IRecordStopCallBack() {//TODO:
            @Override
            public void onStop(String path) {
                stopTime = System.currentTimeMillis();
                long recordTime = stopTime - startTime;
                duration += recordTime;
                list.add(path);
                callDealer.onSuccess(routPath, list, duration);
                if (stopType != 2) {
                    list = new ArrayList<>();
                    duration = 0;
                }
                startTime = 0;
                stopTime = 0;
            }
        });
    }

    public void stopRecord() {
        mMain.mRecordServ.stop();
    }

    private void initPermission(Activity activity) {
        //获取动态权限
        if (ContextCompat.checkSelfPermission(WzFramworkApplication.getmContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        }
    }

    private void record(final IStartRecordCallBack callDealer) {
        String path = routPath + File.separator + String.valueOf(System.currentTimeMillis()) + "." + mMain.mConf.getRecordType();
        mMain.mRecordServ.start(maxLength, path, new IToolAudiioServCallBack() {
            @Override
            public void onPrepare() {//准备完成，即将开始录制
                if (callDealer != null) {
                    callDealer.onPrepare();
                }
            }

            @Override
            public void onStart() {//开始录制
                startTime = System.currentTimeMillis();
                if (callDealer != null) {
                    callDealer.onStart();
                }
            }

            @Override
            public void onError(Object e, String path) {//异常
                if (callDealer != null) {
                    callDealer.onError(e, path);
                }
            }

            @Override
            public void recordOver() {//录制达到时长上限，自动终止录制
                if (callDealer != null) {
                    callDealer.recordOver(maxLength);
                }
            }
        });
    }


    /**
     * 开始播放录音
     *
     * @param path 录音文件存储地址
     */
    public void startAudioPlay(final String path, int palyType, final IAudiioPlay iStartAudioPlay) {
        this.iStartAudioPlay = iStartAudioPlay;
        //检测音量
        mWebviewAudioDealer.VolumeDetection(WzFramworkApplication.getmContext());
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if (palyType == 0) {
            mMain.mPlayServ.stop();
        }
        mMain.mPlayServ.start(WzFramworkApplication.getmContext(), path, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {//播放完成，推送
                mWebviewAudioDealer.hideVolumeDetection();
                if (iStartAudioPlay != null) {
                    iStartAudioPlay.breakAudioPlay();
                }
            }
        }, iStartAudioPlay);
    }

    /**
     * 暂停播放音频
     */
    public void pauseAudioPlay(String method, int cid, IAudioPlayStop iAudioPlayStop) {
        BizPopupsMain.getInstance().getControl().hidePWVoice();
        mMain.mPlayServ.pause();
        if (iAudioPlayStop != null) {
            iAudioPlayStop.pauseAudioPlay(method, cid, 0);
        }
    }

    /**
     * 停止播放音频
     */
    public void stopAudioPlay(String method, int cid, IAudioPlayStop iStopAudioPlay) {
        mMain.mPlayServ.stop();
        if (iStopAudioPlay != null) {
            iStopAudioPlay.stopAudioPlay(method, cid, 0);
        }
    }

    public void stopAudioPlay() {
        mMain.mPlayServ.stop();
        if (iStartAudioPlay != null) {
            iStartAudioPlay.breakAudioPlay();
        }
    }
}
