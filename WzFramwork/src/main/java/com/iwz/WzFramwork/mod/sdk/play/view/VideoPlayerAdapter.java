package com.iwz.WzFramwork.mod.sdk.play.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.mod.sdk.play.model.LiveInfo;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;
import com.iwz.WzFramwork.mod.tool.common.system.model.ENetworkChange;
import com.iwz.WzFramwork.mod.tool.webview.view.MyAppX5WebView;
import com.iwz.WzFramwork.base.interfaces.IMyEvent;
import com.iwz.WzFramwork.mod.bus.event.BusEventMain;
import com.iwz.WzFramwork.mod.bus.event.model.IMyEventDealer;
import com.iwz.WzFramwork.mod.tool.webview.interfaces.IWebChromeClient;
import com.iwz.WzFramwork.partern.ali.player.AliPlayerManager;
import com.iwz.WzFramwork.partern.ali.player.ErrorInfo;
import com.iwz.WzFramwork.partern.ali.player.IAliPlayer;
import com.iwz.WzFramwork.partern.glide.GlideUtil;
import com.iwz.wzframwork.R;

import java.util.List;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/11/109:41
 * desc   :
 */
public class VideoPlayerAdapter extends RecyclerView.Adapter {
    private List<LiveInfo> mLiveInfos;
    private LayoutInflater inflater;
    private Activity mActivity;
    private boolean mIsRefresh = false;

    public VideoPlayerAdapter(Activity activity, List<LiveInfo> liveInfos) {
        this.mActivity = activity;
        this.mLiveInfos = liveInfos;
        inflater = LayoutInflater.from(mActivity);
        AliPlayerManager.getInstance().initPlayer(mActivity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VideoViewHolder(inflater.inflate(R.layout.video_player_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        VideoViewHolder cViewHolder = (VideoViewHolder) viewHolder;
        initView(cViewHolder, position);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (mIsRefresh) {
            mIsRefresh = false;
            return;
        }
        VideoViewHolder cViewHolder = (VideoViewHolder) holder;
        MyAppX5WebView tag = (MyAppX5WebView) cViewHolder.web_view.getTag();
        if (tag != null) {
            tag.destroy();
        }
        cViewHolder.web_view.removeAllViews();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void initData(final VideoViewHolder cViewHolder, final int position, boolean isRefresh) {
        final MyAppX5WebView webView = new MyAppX5WebView(mActivity, null);
        mIsRefresh = isRefresh;
        cViewHolder.web_view.removeAllViews();
        cViewHolder.web_view.addView(webView);
        cViewHolder.web_view.setTag(webView);
        webView.setEventListener(new MyAppX5WebView.IEventListener() {
            @Override
            public void identifyPic(String url) {
                WzFramworkApplication.getmImageRecognit().showPicScan(url);
            }

            @Override
            public void startActivity(Intent intent) {
                startActivity(intent);
            }

            @Override
            public void openBrowser(String url) {
                webView.openBrowser(url, mActivity);
            }

            @Override
            public void showCommonWebview(String url) {
                WzFramworkApplication.getmRouter().startWebview(url, "", false, false);
            }

            @Override
            public void cancleDownLoad() {
                webView.cancleDownLoad(mActivity);
            }
        });
        webView.loadUrl(mLiveInfos.get(position).getPageUrl());
        loadWebViewInfo(webView, cViewHolder.web_error);
        cViewHolder.web_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(mLiveInfos.get(position).getPageUrl());
            }
        });
        SurfaceHolder surfaceHolder = cViewHolder.video_view.getHolder();
        surfaceHolder.setFormat(0);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                AliPlayerManager.getInstance().setDisplay(holder);
                cViewHolder.iv_bg.setVisibility(View.GONE);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                AliPlayerManager.getInstance().redraw();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (!WzFramworkApplication.getmAppStatus().isBackGround()) {
                    // 执行切换到后台的逻辑
                    AliPlayerManager.getInstance().setDisplay(null);
                    holder.removeCallback(this);
                }
            }
        });
        AliPlayerManager.getInstance().setUrl(mLiveInfos.get(position).getLiveUrl());
        AliPlayerManager.getInstance().start();
    }

    /**
     * 加载WebView进度条
     */
    private void loadWebViewInfo(final MyAppX5WebView mWebview, final TextView textView) {
        mWebview.setWebChromeClient(mActivity, ToolSystemMain.getInstance().getControlApp().getExternalFileDir(), new IWebChromeClient() {
            @Override
            public void onReceivedTitle(String wvTitle) {
            }

            @Override
            public void onProgressChanged(String title, int newProgress) {
                if (newProgress == 100) {
                }
            }

            @Override
            public void startActivity(Intent intent, int code) {
                mActivity.startActivityForResult(intent, code);
            }
        });
        mWebview.setOnWebViewErrorListener(new MyAppX5WebView.IWebViewErrorListener() {
            @Override
            public void onReceivedError() {
                textView.setVisibility(View.VISIBLE);
                mWebview.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onReceivedError(int i, String s, String s1) {
                textView.setVisibility(View.VISIBLE);
                mWebview.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageStarted(String url, Bitmap bitmap) {
                textView.setVisibility(View.GONE);
                mWebview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(String url) {
            }
        });
    }

    private void initView(VideoViewHolder cViewHolder, int position) {
        ViewGroup.LayoutParams layoutParams = cViewHolder.rl_video.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        cViewHolder.iv_bg.setVisibility(View.VISIBLE);
        GlideUtil.getInstance().blurImg(mActivity, mLiveInfos.get(position).getPosterUrl(), cViewHolder.iv_bg);
    }

    public void initEvent(@NonNull final VideoViewHolder viewHolder) {
        viewHolder.iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        viewHolder.bt_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, "尝试重新加载", Toast.LENGTH_SHORT).show();
                AliPlayerManager.getInstance().reload();
            }
        });
        AliPlayerManager.getInstance().setOnPreparedListener(new IAliPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                int videoWidth = AliPlayerManager.getInstance().getVideoWidth();
                int videoHeight = AliPlayerManager.getInstance().getVideoHeight();
                WindowManager manager = mActivity.getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getRealMetrics(outMetrics);
                int widthPixels = outMetrics.widthPixels;
                int heightPixels = outMetrics.heightPixels;
                int mWidth = widthPixels;
                int mHeight = videoHeight * mWidth / videoWidth;
                if (widthPixels / videoWidth > heightPixels / videoHeight) {//高度低，按宽来适配
                    mWidth = widthPixels;
                    mHeight = videoHeight * widthPixels / videoWidth;
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(mWidth, mHeight);
                    layoutParams.gravity = Gravity.TOP;
                    layoutParams.topMargin = -(mHeight - heightPixels) / 2;
                    layoutParams.leftMargin = 0;
                    layoutParams.rightMargin = 0;
                    viewHolder.video_view.setLayoutParams(layoutParams);
                } else {
                    mHeight = heightPixels;
                    mWidth = videoWidth * heightPixels / videoHeight;
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(mWidth, mHeight);
                    layoutParams.gravity = Gravity.TOP;
                    layoutParams.leftMargin = -(mWidth - widthPixels) / 2;
                    layoutParams.rightMargin = (mWidth - widthPixels) / 2;
                    viewHolder.video_view.setLayoutParams(layoutParams);
                }
                viewHolder.fl_video.setVisibility(View.VISIBLE);
                viewHolder.iv_bg.setVisibility(View.GONE);

            }
        });
        AliPlayerManager.getInstance().setOnErrorListener(new IAliPlayer.OnErrorListener() {
            @Override
            public void onError(ErrorInfo var1) {
                if (ToolSystemMain.getInstance().getControlApp().isNetworkAvalible()) {
                    //1、发生错误显示“主播暂时离开，马上回来”，背景色为黑色，并自动刷新页面
                    GlideUtil.getInstance().loadImg(mActivity, R.drawable.video_play_error, viewHolder.iv_bg);
                    AliPlayerManager.getInstance().reload();
                } else {
                    //2、无网时显示“亲，您的网络不太顺畅哦”，背景色透明，并中间显示重新加载，点击重新加载提示尝试重新加载
                    viewHolder.rl_reload.setVisibility(View.VISIBLE);
                }
            }
        });
        BusEventMain.getInstance().addDealer(ENetworkChange.getEventName(), new IMyEventDealer() {
            @Override
            public void onOccur(IMyEvent event) {
                if (event != null) {
                    final NetworkInfo networkInfo = ((ENetworkChange) event).getmNetworkInfo();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (networkInfo != null && networkInfo.isAvailable()) {//有网
                                //3、有网后自动刷新当前页面，隐藏无网提示
                                viewHolder.rl_reload.setVisibility(View.GONE);
                                AliPlayerManager.getInstance().reload();
                            } else {
                                //2、无网时显示“亲，您的网络不太顺畅哦”，背景色透明，并中间显示重新加载，点击重新加载提示尝试重新加载
                                viewHolder.rl_reload.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }
            }
        });
        AliPlayerManager.getInstance().setOnSnapShotListener(new IAliPlayer.OnSnapShotListener() {
            @Override
            public void onSnapShot(Bitmap var1, int var2, int var3) {
                //TODO:截图
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLiveInfos.size();
    }

    public void resetData(VideoViewHolder viewHolder, int position) {
        viewHolder.iv_bg.setVisibility(View.VISIBLE);
        GlideUtil.getInstance().blurImg(mActivity, mLiveInfos.get(position).getPosterUrl(), viewHolder.iv_bg);
    }
}
