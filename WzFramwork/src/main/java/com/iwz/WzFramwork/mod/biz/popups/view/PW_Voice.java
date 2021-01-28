package com.iwz.WzFramwork.mod.biz.popups.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.iwz.wzframwork.R;


/**
 * 类名： PW_Search_KeyWords.java
 * 作者：小辉
 * 时间：2017/9/30.
 * 描述：语音播放无声时弹窗
 */
public class PW_Voice extends PopupWindow {
    private Context context;
    View pwSearchView;

    public PW_Voice(Context context) {
        super(context);
        this.context = context;
        //初始化页面
        initView();
//        setAnimationStyle(R.style.popwindow_anim);
        //不加会出现黑色边框
        setBackgroundDrawable(new BitmapDrawable());
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(pwSearchView);
    }

    //初始化页面
    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pwSearchView = inflater.inflate(R.layout.pw_voice, null);
        pwSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    dismiss();
                }
                return false;
            }
        });
    }

    public void cancle() {
        dismiss();
    }
}
