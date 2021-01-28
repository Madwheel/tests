package com.iwz.WzFramwork.partern.ali.player;

import android.graphics.Bitmap;
import android.view.SurfaceHolder;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/11/615:24
 * desc   :
 */
public interface IAliPlayer {
    void selectTrack(int var1);

    void setUrl(String url);

    void start();

    void reload();

    void pause();

    void reset();

    void release();

    void stop();

    void seekTo(long var1);

    void setDisplay(SurfaceHolder var1);

    void redraw();

    void setOnPreparedListener(OnPreparedListener var1);

    void setOnStateChangedListener(OnStateChangedListener var1);

    void setOnCompletionListener(final OnCompletionListener var1);

    void setOnErrorListener(OnErrorListener var1);

    void setOnSnapShotListener(final OnSnapShotListener var1);

    public interface OnPreparedListener {
        void onPrepared();
    }

    public interface OnStateChangedListener {
        void onStateChanged(int var1);
    }

    public interface OnCompletionListener {
        void onCompletion();
    }

    public interface OnErrorListener {
        void onError(ErrorInfo var1);
    }

    public interface OnSnapShotListener {
        void onSnapShot(Bitmap var1, int var2, int var3);
    }
}
