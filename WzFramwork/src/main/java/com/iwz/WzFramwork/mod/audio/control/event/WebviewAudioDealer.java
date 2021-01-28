package com.iwz.WzFramwork.mod.audio.control.event;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.base.interfaces.IMyEvent;
import com.iwz.WzFramwork.mod.audio.BizAudioMain;
import com.iwz.WzFramwork.mod.biz.popups.BizPopupsMain;
import com.iwz.WzFramwork.mod.bus.event.BusEventMain;
import com.iwz.WzFramwork.mod.bus.event.model.IMyEventDealer;
import com.iwz.WzFramwork.mod.tool.webview.model.EWebviewDestory;
import com.iwz.WzFramwork.mod.tool.webview.model.EWebviewUrlChange;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/18
 */
public class WebviewAudioDealer extends ControlApp {
    private BizAudioMain mMain;

    protected WebviewAudioDealer(BizAudioMain main) {
        super(main);
        mMain = main;
    }

    private static WebviewAudioDealer mWebviewAudioDealer;

    public static WebviewAudioDealer getInstance(BizAudioMain main) {
        if (mWebviewAudioDealer == null) {
            synchronized (WebviewAudioDealer.class) {
                if (mWebviewAudioDealer == null) {
                    mWebviewAudioDealer = new WebviewAudioDealer(main);
                }
            }
        }
        return mWebviewAudioDealer;
    }

    @Override
    public void born() {
        super.born();
        hookWebview();
    }

    private void hookWebview() {
        //监听url改变时，需要重置右侧按钮
        BusEventMain.getInstance().addDealer(EWebviewUrlChange.getEventName(), new IMyEventDealer() {
            @Override
            public void onOccur(IMyEvent event) {
                hideVolumeDetection();
                mMain.mControl.stopRecord();
                mMain.mControl.stopAudioPlay();
            }
        });
        //监听url改变时，需要重置右侧按钮
        BusEventMain.getInstance().addDealer(EWebviewDestory.getEventName(), new IMyEventDealer() {
            @Override
            public void onOccur(IMyEvent event) {
                hideVolumeDetection();
                mMain.mControl.stopRecord();
                mMain.mControl.stopAudioPlay();
            }
        });
    }

    /**
     * 加测音量
     *
     * @param context
     */
    public void VolumeDetection(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (current == 0) {
            BizPopupsMain.getInstance().getControl().showPWVoice((Activity) context);
        }
    }

    public void hideVolumeDetection() {
        //隐藏声音检测弹窗
        BizPopupsMain.getInstance().getControl().hidePWVoice();
    }
}
