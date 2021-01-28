package com.iwz.WzFramwork.mod.biz.popups.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.iwz.WzFramwork.mod.biz.collect.serv.MyOnClickListener;
import com.iwz.WzFramwork.partern.glide.GlideUtil;
import com.iwz.wzframwork.R;

/**
 * 描述：统一弹窗
 * 作者：小辉
 * 时间：2020/02/04
 */
public class UnifiedPopupWindow extends PopupWindow {
    private Activity mActivity;
    private View popView;
    private RelativeLayout rl_unified;//弹窗关闭周围蒙层
    private ImageView iv_unifiedpop_body;//统一弹窗中心图片
    private ImageView iv_unifiedpop_close;//统一弹窗关闭按钮
    private IUnifiedPopClickListener mListener;//统一弹窗监听对象让人让

    public UnifiedPopupWindow(Activity mActivity) {
        this.mActivity = mActivity;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.unifiedpopup_pop, null);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(popView);
        // 设置可以获取焦点
        setFocusable(true);
        initView();
    }

    private void initView() {
        rl_unified = popView.findViewById(R.id.rl_unified);
        iv_unifiedpop_body = popView.findViewById(R.id.iv_unifiedpop_body);
        iv_unifiedpop_close = popView.findViewById(R.id.iv_unifiedpop_close);
    }

    public void setData(int defaultImg, String imgUrl, final IUnifiedPopClickListener listener) {
        this.mListener = listener;
        GlideUtil.getInstance().loadImg(mActivity, imgUrl, defaultImg, defaultImg, iv_unifiedpop_body);
        iv_unifiedpop_body.setOnClickListener(new MyOnClickListener(UnifiedPopupWindow.class, "统一弹窗图片", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onBodyClick();
                }
            }
        }));
        iv_unifiedpop_close.setOnClickListener(new MyOnClickListener(UnifiedPopupWindow.class, "关闭", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCloseClick();
                }
            }
        }));
        rl_unified.setOnClickListener(new MyOnClickListener(UnifiedPopupWindow.class, "蒙层", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClose();
                }
            }
        }));
    }

    @Override
    public void dismiss() {
        if (mListener != null) {
            mListener.onClose();
        }
    }

    public void close() {
        super.dismiss();
    }

    public interface IUnifiedPopClickListener {

        void onBodyClick();

        void onCloseClick();

        void onClose();
    }
}