package com.iwz.WzFramwork.mod.sdk.play.view;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.WzBaseActivity;
import com.iwz.WzFramwork.mod.constants.FMAppConstants;
import com.iwz.WzFramwork.mod.constants.h5.WZAppMQConstants;
import com.iwz.WzFramwork.mod.sdk.play.model.JLiveDataInfo;
import com.iwz.WzFramwork.mod.sdk.play.model.LiveInfo;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;
import com.iwz.WzFramwork.mod.tool.common.system.model.ENetworkChange;
import com.iwz.WzFramwork.mod.tool.webview.model.IWebviewGoListener;
import com.iwz.WzFramwork.mod.tool.webview.view.MyAppX5WebView;
import com.iwz.WzFramwork.mod.bus.event.BusEventMain;
import com.iwz.WzFramwork.partern.ali.player.AliPlayerManager;
import com.iwz.wzframwork.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/11/615:33
 * desc   :
 */
public class VideoPlayerActivity extends WzBaseActivity {
    private RecyclerView rl_video;
    private PagerSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    private AliPlayerManager aliPlayerManager;
    private int currentPosition = 0;
    private List<LiveInfo> mLiveInfos;
    private VideoPlayerAdapter videoAdapter;
    private IntentFilter mIntentFilter;
    private MyReceiver mReceiver;
    private boolean isInitFirst = true;
    private PowerManager.WakeLock mWakeLock;

    @Override
    protected String getPageName() {
        return "VideoPlayerActivity";
    }

    //        mVideoUrls.add("rtmp://liveabc-dev.pydp888.com/livestreaming/22?auth_key=1604599205-596677-0-62d0957a43c1ff388d227510a55bdd7e");
//        mUrls.add("https://xuanshang95.com/");
//        urls.add("https://imgcdn-dev.pydp888.com/file/0/3/100195/20201102/8a45abb54676173a3c5d0d4cb10e5030.mp4");
    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        String pageUrl = intent.getStringExtra("pageUrl");
        LiveInfo liveInfo = new LiveInfo();
        liveInfo.setPageUrl(pageUrl);
        mLiveInfos = new ArrayList<>();
        mLiveInfos.add(liveInfo);
        aliPlayerManager = AliPlayerManager.getInstance();
        mReceiver = new MyReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mIntentFilter.addAction(FMAppConstants.WZAPP_ROOTER_GOAPPHOME);
        mIntentFilter.addAction(FMAppConstants.WZAPP_DO_CLEARCACHE);
        mIntentFilter.addAction(FMAppConstants.DO_SETSCREENMODE);
        mIntentFilter.addAction(FMAppConstants.HISTORY_GO);
        mIntentFilter.addAction(FMAppConstants.DO_LANDSCAPE);
        mIntentFilter.addAction(FMAppConstants.DO_UNLANDSCAPE);
        mIntentFilter.addAction(FMAppConstants.FS_CHOOSE_LOCALFILE);
        mIntentFilter.addAction(FMAppConstants.WEBVIEW_DOSHARETO_ACTION);
        mIntentFilter.addAction(FMAppConstants.BIZ_WEBVIEW_UIUPDATE);
        mIntentFilter.addAction(FMAppConstants.WZAPP_SETLIVEDATA);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager != null) {
            mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "WakeLock");
        }
        initView();
        iniEvent();
    }

    private void iniEvent() {
        rl_video.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                        View view = snapHelper.findSnapView(layoutManager);
                        //当前固定后的item position
                        int position = recyclerView.getChildAdapterPosition(view);
                        if (currentPosition != position) {
                            //如果当前position 和 上一次固定后的position 相同, 说明是同一个, 只不过滑动了一点点, 然后又释放了
                            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                            if (viewHolder != null && viewHolder instanceof VideoViewHolder) {
                                aliPlayerManager.stop();
                                aliPlayerManager.reset();
                                aliPlayerManager.release();
                                aliPlayerManager.initPlayer(VideoPlayerActivity.this);
                                videoAdapter.initData((VideoViewHolder) viewHolder, position, false);
                                aliPlayerManager.setDisplay(((VideoViewHolder) viewHolder).video_view.getHolder());
                                videoAdapter.initEvent((VideoViewHolder) viewHolder);
                            }
                        }
                        currentPosition = position;
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                int position = recyclerView.getLayoutManager().getItemCount() - 1;
                if (lastChildBottom == recyclerBottom && lastPosition == position) {
                    if (isInitFirst) {
                        View view = snapHelper.findSnapView(layoutManager);
                        RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                        isInitFirst = false;
                        videoAdapter.initData((VideoViewHolder) viewHolder, position, false);
                        aliPlayerManager.setDisplay(((VideoViewHolder) viewHolder).video_view.getHolder());
                        videoAdapter.initEvent((VideoViewHolder) viewHolder);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
        aliPlayerManager.start();
        if (mWakeLock != null) {
            mWakeLock.acquire();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
        aliPlayerManager.pause();
        if (mWakeLock != null) {
            mWakeLock.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aliPlayerManager.stop();
        aliPlayerManager.reset();
        aliPlayerManager.release();
    }

    private void initView() {
        rl_video = findViewById(R.id.rl_video);
        layoutManager = new LinearLayoutManager(VideoPlayerActivity.this, LinearLayoutManager.VERTICAL, false);
        rl_video.setLayoutManager(layoutManager);
        videoAdapter = new VideoPlayerAdapter(VideoPlayerActivity.this, mLiveInfos);
        rl_video.setAdapter(videoAdapter);
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rl_video);
        rl_video.setItemViewCacheSize(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //当前固定后的item position
        //如果当前position 和 上一次固定后的position 相同, 说明是同一个, 只不过滑动了一点点, 然后又释放了
        MyAppX5WebView webView = getWebview();
        if (webView != null) {
            webView.onActivityResult(requestCode, resultCode, intent);
        }
        ToolSystemMain.getInstance().getControlApp().onActivityResult(requestCode, resultCode, intent, VideoPlayerActivity.this);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                BusEventMain.getInstance().publish(new ENetworkChange(networkInfo));
            } else if (action.equals(FMAppConstants.WZAPP_ROOTER_GOAPPHOME)) {
                String pageKey = intent.getStringExtra("pageKey");
                WzFramworkApplication.getmRouter().jumpToMain(VideoPlayerActivity.this,pageKey, false);
                VideoPlayerActivity.this.finish();
            } else if (action.equals(FMAppConstants.FS_CHOOSE_LOCALFILE)) {
                ToolSystemMain.getInstance().getControlApp().chooseLocalFile(VideoPlayerActivity.this);
            } else if (action.equals(FMAppConstants.WEBVIEW_DOSHARETO_ACTION)) {
                MyAppX5WebView webView = getWebview();
                if (webView != null) {
                    WzFramworkApplication.getmShare().doShareTo(VideoPlayerActivity.this, webView);
                }
            } else if (action.equals(FMAppConstants.DO_SETSCREENMODE)) {
                if (intent.getBooleanExtra("isReset", false)) {
                    ToolSystemMain.getInstance().getControlApp().setScreenMode(VideoPlayerActivity.this, 0, null, null);
                } else {
                    ToolSystemMain.getInstance().getControlApp().setScreenMode(VideoPlayerActivity.this, intent.getIntExtra("mode", 0), intent.getStringExtra("statusBarColor"), null);
                }
            } else if (action.equals(FMAppConstants.HISTORY_GO)) {
                MyAppX5WebView webView = getWebview();
                if (webView != null) {
                    ToolSystemMain.getInstance().getControlApp().goBackOrForward(intent.getIntExtra("delta", 0), webView, new IWebviewGoListener() {
                        @Override
                        public void webviewClose() {
                            VideoPlayerActivity.this.finish();
                        }
                    });
                }
            } else if (action.equals(FMAppConstants.DO_LANDSCAPE)) {
                ToolSystemMain.getInstance().getControlApp().setScreenOrient(VideoPlayerActivity.this, 0);
            } else if (action.equals(FMAppConstants.DO_UNLANDSCAPE)) {
                ToolSystemMain.getInstance().getControlApp().setScreenOrient(VideoPlayerActivity.this, 1);
            } else if (action.equals(FMAppConstants.WZAPP_DO_CLEARCACHE)) {
                MyAppX5WebView webView = getWebview();
                if (webView != null) {
                    String msg = intent.getStringExtra("msg");
                    JSONObject jbody = new JSONObject();
                    try {
                        jbody.put("cacheSize", msg);
                        jbody.put("error_code", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String exec = "javascript:WZWeb.exec('" + WZAppMQConstants.CACHE_APPCLEAR + "','" + jbody.toString() + "')";
                    BusEventMain.getInstance().mServ.jsExecute(webView, exec);
                }
            } else if (action.equals(FMAppConstants.BIZ_WEBVIEW_UIUPDATE)) {
                MyAppX5WebView webview = getWebview();
                if (webview != null) {
                    webview.reload();
                }
            } else if (action.equals(FMAppConstants.WZAPP_SETLIVEDATA)) {
                if (intent == null) {
                    return;
                }
                String liveData = intent.getStringExtra("liveData");
                JLiveDataInfo jLiveDataInfo = JSON.parseObject(liveData, JLiveDataInfo.class);
                mLiveInfos.clear();
                LiveInfo current = jLiveDataInfo.getCurrent();
                List<LiveInfo> list = jLiveDataInfo.getList();
                int currentPositon = -1;
                for (int i = 0; i < list.size(); i++) {
                    LiveInfo liveInfo = list.get(i);
                    mLiveInfos.add(liveInfo);
                    if (current.getLiveId() == liveInfo.getLiveId()) {
                        currentPositon = i;
                    }
                }
                if (currentPositon == -1) {
                    mLiveInfos.add(0, current);
                    currentPositon = 0;
                }
                rl_video.scrollToPosition(currentPositon);
                videoAdapter.notifyDataSetChanged();
                View view = snapHelper.findSnapView(layoutManager);
                RecyclerView.ViewHolder viewHolder = rl_video.getChildViewHolder(view);
                aliPlayerManager.stop();
                aliPlayerManager.reset();
                aliPlayerManager.release();
                View childAt = rl_video.getChildAt(currentPosition);
                videoAdapter.resetData((VideoViewHolder) rl_video.getChildViewHolder(childAt), currentPosition);
                aliPlayerManager.initPlayer(VideoPlayerActivity.this);
                videoAdapter.initData((VideoViewHolder) viewHolder, currentPositon, true);
                aliPlayerManager.setDisplay(((VideoViewHolder) viewHolder).video_view.getHolder());
                videoAdapter.initEvent((VideoViewHolder) viewHolder);
                currentPosition = currentPositon;
            }
        }
    }

    private MyAppX5WebView getWebview() {
        View snapView = snapHelper.findSnapView(layoutManager);
        VideoViewHolder childViewHolder = (VideoViewHolder) rl_video.getChildViewHolder(snapView);
        if (childViewHolder != null) {
            if (childViewHolder.web_view != null) {
                MyAppX5WebView webView = (MyAppX5WebView) childViewHolder.web_view.getTag();
                if (webView != null) {
                    return webView;
                }
            }
        }
        return null;
    }
}
