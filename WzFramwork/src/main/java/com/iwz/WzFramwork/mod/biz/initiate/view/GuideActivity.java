package com.iwz.WzFramwork.mod.biz.initiate.view;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.VideoView;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.WzBaseActivity;
import com.iwz.WzFramwork.mod.biz.initiate.BizInitiateMain;
import com.iwz.WzFramwork.mod.biz.initiate.model.JInitiateInfo;
import com.iwz.wzframwork.R;

public class GuideActivity extends WzBaseActivity {
    private VideoView videoView;
    private BizInitiateMain mMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mMain = BizInitiateMain.getInstance();
        initView();
        initData();
        initEvent();
    }

    @Override
    protected String getPageName() {
        return "BizGuide";
    }

    private void initEvent() {
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                WzFramworkApplication.getmRouter().jumpToMain(GuideActivity.this, false);
                finish();
            }
        });
    }

    private void initData() {
        //设置已经开启过引导页
        JInitiateInfo initiateInfo = new JInitiateInfo();
        initiateInfo.setFirstUse(false);
//        initiateInfo.setGuideCount(FMAppConstants.GUIDE_CODE);
        mMain.getpControl().setJInitiateInfoAsync(initiateInfo);
//        //加载指定的视频文件
//        String uri = BizSystemMain.getInstance().pControlApp.getRawPath(R.raw.video);
//        videoView.setVideoPath(uri);
//        videoView.setVideoURI(Uri.parse(uri));
//        videoView.start();
    }

    private void initView() {
        videoView = findViewById(R.id.videoView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WzFramworkApplication.getmRouter().jumpToMain(GuideActivity.this, false);
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
