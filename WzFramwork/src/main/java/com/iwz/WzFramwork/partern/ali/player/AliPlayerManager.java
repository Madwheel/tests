package com.iwz.WzFramwork.partern.ali.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceHolder;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorCode;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.nativeclass.TrackInfo;
import com.aliyun.player.source.UrlSource;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/11/615:01
 * desc   :
 */
public class AliPlayerManager implements IAliPlayer {
    private AliPlayer aliyunVodPlayer;

    private static AliPlayerManager mWzAliPlayerManager = null;

    public static AliPlayerManager getInstance() {
        synchronized (AliPlayerManager.class) {
            if (mWzAliPlayerManager == null) {
                mWzAliPlayerManager = new AliPlayerManager();
            }
        }
        return mWzAliPlayerManager;
    }

    private AliPlayerManager() {
    }

    public void initPlayer(Context context) {
        preparePlay(context);
        initListener();
    }

    @Override
    public void setUrl(String url) {
        UrlSource urlSource = new UrlSource();
        urlSource.setUri(url);
        //设置播放源
        aliyunVodPlayer.setDataSource(urlSource);
        //aliyunVodPlayer.setAutoPlay(true);//播放器SDK支持自动播放视频的设置。在prepare之前调用setAutoPlay即可设置。
        //准备播放
        aliyunVodPlayer.prepare();
    }

    @Override
    public void start() {
        if (aliyunVodPlayer != null) {
            // 开始播放。
            aliyunVodPlayer.start();
        }
    }

    @Override
    public void reload() {
        if (aliyunVodPlayer != null) {
            // 开始播放。
            aliyunVodPlayer.reload();
        }
    }

    @Override
    public void pause() {
        if (aliyunVodPlayer != null) {
            aliyunVodPlayer.pause();
        }
    }

    @Override
    public void reset() {
        if (aliyunVodPlayer != null) {
            aliyunVodPlayer.reset();
        }
    }

    @Override
    public void release() {
        if (aliyunVodPlayer != null) {
            aliyunVodPlayer.release();
            aliyunVodPlayer = null;
        }
    }

    @Override
    public void stop() {
        if (aliyunVodPlayer != null) {
            aliyunVodPlayer.stop();
        }
    }

    @Override
    public void seekTo(long var1) {
        if (aliyunVodPlayer != null) {
            aliyunVodPlayer.seekTo(var1);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        if (holder != null) {
            aliyunVodPlayer.setDisplay(holder);
        }
    }

    @Override
    public void redraw() {
        if (aliyunVodPlayer != null) {
            aliyunVodPlayer.redraw();
        }
    }

    @Override
    public void selectTrack(int var) {
        if (aliyunVodPlayer != null) {
            aliyunVodPlayer.selectTrack(var);
        }
    }

    @Override
    public void setOnPreparedListener(final OnPreparedListener var1) {
        aliyunVodPlayer.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                //准备成功事件
                var1.onPrepared();
            }
        });

    }

    @Override
    public void setOnCompletionListener(final OnCompletionListener var1) {
        aliyunVodPlayer.setOnCompletionListener(new IPlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                //播放完成事件
                var1.onCompletion();
            }
        });
    }

    @Override
    public void setOnStateChangedListener(final OnStateChangedListener var1) {
        aliyunVodPlayer.setOnStateChangedListener(new IPlayer.OnStateChangedListener() {
            @Override
            public void onStateChanged(int i) {
                var1.onStateChanged(i);
            }
        });
    }

    @Override
    public void setOnErrorListener(final OnErrorListener var1) {
        aliyunVodPlayer.setOnErrorListener(new IPlayer.OnErrorListener() {
            @Override
            public void onError(ErrorInfo errorInfo) {
                //出错事件
                com.iwz.WzFramwork.partern.ali.player.ErrorInfo errorInfo1 = new com.iwz.WzFramwork.partern.ali.player.ErrorInfo();
                errorInfo1.setExtra(errorInfo.getExtra());
                errorInfo1.setExtra(errorInfo.getMsg());
                var1.onError(errorInfo1);
            }
        });
    }

    @Override
    public void setOnSnapShotListener(final OnSnapShotListener var1) {
        aliyunVodPlayer.setOnSnapShotListener(new IPlayer.OnSnapShotListener() {
            @Override
            public void onSnapShot(Bitmap bm, int with, int height) {
                //截图事件
                var1.onSnapShot(bm, with, height);
            }
        });
    }

    /**
     * 播放视频准备
     */
    private void preparePlay(Context context) {
        aliyunVodPlayer = AliPlayerFactory.createAliPlayer(context);
    }

    private void initListener() {
        aliyunVodPlayer.setOnInfoListener(new IPlayer.OnInfoListener() {
            @Override
            public void onInfo(InfoBean infoBean) {
                //其他信息的事件，type包括了：循环播放开始，缓冲位置，当前播放位置，自动播放开始等
            }
        });
        aliyunVodPlayer.setOnRenderingStartListener(new IPlayer.OnRenderingStartListener() {
            @Override
            public void onRenderingStart() {
                //首帧渲染显示事件
            }
        });
        aliyunVodPlayer.setOnVideoSizeChangedListener(new IPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(int width, int height) {
                //视频分辨率变化回调
            }
        });
        aliyunVodPlayer.setOnLoadingStatusListener(new IPlayer.OnLoadingStatusListener() {
            @Override
            public void onLoadingBegin() {
                //缓冲开始。
            }

            @Override
            public void onLoadingProgress(int percent, float kbps) {
                //缓冲进度
            }

            @Override
            public void onLoadingEnd() {
                //缓冲结束
            }
        });
        aliyunVodPlayer.setOnSeekCompleteListener(new IPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete() {
                //拖动结束
            }
        });
        aliyunVodPlayer.setOnSubtitleDisplayListener(new IPlayer.OnSubtitleDisplayListener() {
            @Override
            public void onSubtitleExtAdded(int i, String s) {
            }

            @Override
            public void onSubtitleShow(int i, long l, String s) {
                //显示字幕
            }

            @Override
            public void onSubtitleHide(int i, long l) {
                //隐藏字幕
            }

        });
        aliyunVodPlayer.setOnTrackChangedListener(new IPlayer.OnTrackChangedListener() {
            @Override
            public void onChangedSuccess(TrackInfo trackInfo) {
                //切换音视频流或者清晰度成功
            }

            @Override
            public void onChangedFail(TrackInfo trackInfo, ErrorInfo errorInfo) {
                //切换音视频流或者清晰度失败
            }
        });
        aliyunVodPlayer.setOnStateChangedListener(new IPlayer.OnStateChangedListener() {
            @Override
            public void onStateChanged(int newState) {
                //播放器状态改变事件
                Log.e("TAG", "onStateChanged");
            }
        });
    }

    public int getVideoHeight() {
        return aliyunVodPlayer.getVideoHeight();
    }

    public int getVideoWidth() {
        return aliyunVodPlayer.getVideoWidth();
    }

}
