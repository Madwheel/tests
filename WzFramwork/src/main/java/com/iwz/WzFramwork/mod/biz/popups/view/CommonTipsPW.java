package com.iwz.WzFramwork.mod.biz.popups.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iwz.WzFramwork.mod.biz.collect.serv.MyOnClickListener;
import com.iwz.wzframwork.R;


/**
 * 描述：协议弹窗
 * 作者：小辉
 * 时间：2019/11/01
 */
public class CommonTipsPW extends PopupWindow {
    private View mRootView;
    private ImageView iv_hometips_pw_close;
    private TextView tv_hometips_pw_title;
    private TextView tv_hometips_pw_dec;
    private RelativeLayout ll_hometips_pw;
    private TextView tv_hometips_pw_confirm;
    private TextView tv_hometips_pw_cancel;
    private ITipsListener mITipsListener;
    private boolean canDismiss;

    public CommonTipsPW(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = inflater.inflate(R.layout.popwin_hometips, null);
        setBackgroundDrawable(new BitmapDrawable());
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(mRootView);
        initView();
        initEvent();
    }

    public void setData(String title, SpannableString dec, String cancel, String confirm, boolean isShowClose, boolean canDismiss, ITipsListener iTipsListener) {
        if (isShowClose) {
            iv_hometips_pw_close.setVisibility(View.VISIBLE);
        } else {
            iv_hometips_pw_close.setVisibility(View.GONE);
        }
        tv_hometips_pw_dec.setMovementMethod(LinkMovementMethod.getInstance());
        tv_hometips_pw_dec.setText(dec);
        setData(title, cancel, confirm, canDismiss, iTipsListener);
    }

    public void setData(String title, String dec, String cancel, String confirm, boolean canDismiss, ITipsListener iTipsListener) {
        tv_hometips_pw_dec.setText("\u3000\u3000" + dec);
        setData(title, cancel, confirm, canDismiss, iTipsListener);
    }

    private void setData(String title, String cancel, String confirm, boolean canDismiss, ITipsListener iTipsListener) {
        this.canDismiss = canDismiss;
        this.mITipsListener = iTipsListener;
        if (TextUtils.isEmpty(title)) {
            tv_hometips_pw_title.setVisibility(View.GONE);
            iv_hometips_pw_close.setVisibility(View.GONE);
        }
        tv_hometips_pw_title.setText(title);
        if (!TextUtils.isEmpty(cancel)) {
            tv_hometips_pw_cancel.setVisibility(View.VISIBLE);
            tv_hometips_pw_cancel.setText(cancel);
        } else {
            tv_hometips_pw_cancel.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(confirm)) {
            tv_hometips_pw_confirm.setText(confirm);
            tv_hometips_pw_confirm.setVisibility(View.VISIBLE);
        } else {
            tv_hometips_pw_confirm.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(confirm) && TextUtils.isEmpty(cancel)) {
            ll_hometips_pw.setVisibility(View.GONE);
        } else {
            ll_hometips_pw.setVisibility(View.VISIBLE);
        }
        ///TODO:
//        BizSystemMain.getInstance().pControlApp.setFontStyle(new TextView[]{tv_hometips_pw_title
//                , tv_hometips_pw_dec, tv_hometips_pw_cancel, tv_hometips_pw_confirm});
    }

    private void initEvent() {
        iv_hometips_pw_close.setOnClickListener(new MyOnClickListener(CommonTipsPW.class, "关闭", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        }));
        tv_hometips_pw_cancel.setOnClickListener(new MyOnClickListener(CommonTipsPW.class, "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mITipsListener != null) {
                    mITipsListener.onCancel();
                }
            }
        }));
        tv_hometips_pw_confirm.setOnClickListener(new MyOnClickListener(CommonTipsPW.class, tv_hometips_pw_confirm.getText().toString(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mITipsListener != null) {
                    mITipsListener.onConfirm();
                }
            }
        }));

    }

    private void initView() {
        iv_hometips_pw_close = mRootView.findViewById(R.id.iv_hometips_pw_close);
        tv_hometips_pw_title = mRootView.findViewById(R.id.tv_hometips_pw_title);
        tv_hometips_pw_dec = mRootView.findViewById(R.id.tv_hometips_pw_dec);
        ll_hometips_pw = mRootView.findViewById(R.id.ll_hometips_pw);
        tv_hometips_pw_cancel = mRootView.findViewById(R.id.tv_hometips_pw_cancel);
        tv_hometips_pw_confirm = mRootView.findViewById(R.id.tv_hometips_pw_confirm);
    }

    public interface ITipsListener {
        void onCancel();

        void onConfirm();
    }

    @Override
    public void dismiss() {
        if (canDismiss) {
            super.dismiss();
        }
    }

    public void close() {
        super.dismiss();
    }
}
