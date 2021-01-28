package com.iwz.WzFramwork.mod.sdk.play.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iwz.wzframwork.R;


/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/11/1010:11
 * desc   :
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout rl_video;
    SurfaceView video_view;
    ImageView iv_bg;
    FrameLayout fl_video;
    RelativeLayout rl_reload;
    Button bt_reload;
    ImageView iv_close;
    FrameLayout web_view;
    TextView web_error;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        rl_video = itemView.findViewById(R.id.rl_video);
        video_view = itemView.findViewById(R.id.video_view);
        iv_bg = itemView.findViewById(R.id.iv_bg);
        fl_video = itemView.findViewById(R.id.fl_video);
        rl_reload = itemView.findViewById(R.id.rl_reload);
        bt_reload = itemView.findViewById(R.id.bt_reload);
        iv_close = itemView.findViewById(R.id.iv_close);
        web_view = itemView.findViewById(R.id.web_view);
        web_error = itemView.findViewById(R.id.web_error);
    }
}
