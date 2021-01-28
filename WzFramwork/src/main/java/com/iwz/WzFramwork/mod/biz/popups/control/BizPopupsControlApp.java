package com.iwz.WzFramwork.mod.biz.popups.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.CommonRes;
import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.base.interfaces.IResCallback;
import com.iwz.WzFramwork.mod.biz.popups.view.PW_Voice;
import com.iwz.WzFramwork.mod.biz.popups.BizPopupsMain;
import com.iwz.WzFramwork.mod.biz.popups.model.DialogItemInfo;
import com.iwz.WzFramwork.mod.biz.popups.view.CommonTipsPW;
import com.iwz.WzFramwork.mod.biz.popups.view.UnifiedPopupWindow;
import com.iwz.WzFramwork.mod.constants.FMAppConstants;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;
import com.iwz.wzframwork.R;

import java.io.IOException;

/**
 * 描述：
 * 作者：小辉
 * 时间：2020/02/04
 */
public class BizPopupsControlApp extends ControlApp {
    private static BizPopupsControlApp mInstance;
    private BizPopupsMain mMain;
    private UnifiedPopupWindow mPop;
    private PW_Voice pw_voice;

    protected BizPopupsControlApp(BizPopupsMain main) {
        super(main);
        this.mMain = main;
    }

    public static BizPopupsControlApp getInstance(BizPopupsMain main) {
        if (mInstance == null) {
            synchronized (BizPopupsControlApp.class) {
                if (mInstance == null) {
                    mInstance = new BizPopupsControlApp(main);
                }
            }
        }
        return mInstance;
    }

    /**
     * @param page 页面 1：发现页 2：我的页 3：斗k首页 4：商城首页；5：情报首页；6：玄商首页
     */
    public void obtainUnifiedPopFetch(int plat, final int page) {
        mMain.pServ.obtainUnifiedPopFetch(plat, page, new IResCallback<DialogItemInfo>() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onFinish(CommonRes<DialogItemInfo> res) {
                if (res.isOk()) {
                    DialogItemInfo resObj = res.getResObj();
                    mMain.pModel.setDialogItemInfo(resObj);
                    Intent intent = new Intent();
                    intent.setAction(FMAppConstants.BIZ_UNIFIED_POP_SHOW_ACTION);
                    WzFramworkApplication.getmContext().sendBroadcast(intent);
                }
            }
        });
    }

    public void showUnifiedPop(final Activity activity, int defaultImg) {
        DialogItemInfo dialogItemInfo = mMain.pModel.getDialogItemInfo();
        final int id = dialogItemInfo.getDialogItem().getId();
        int imgType = dialogItemInfo.getDialogItem().getImgType();//暂时无用，目前只有一种图片类型
        String imgUrl = dialogItemInfo.getDialogItem().getImgUrl();
        final String jumpUrl = dialogItemInfo.getDialogItem().getJumpUrl();
        if (TextUtils.isEmpty(jumpUrl)) {
            return;
        }
        if (mPop == null) {
            mPop = new UnifiedPopupWindow(activity);
        }
        mPop.setFocusable(true);
        try {
            if (activity != null) {
                //17版本以上，判断activity是否已经销毁
                if (Build.VERSION.SDK_INT > 17 && activity.isDestroyed()) {
                    return;
                }
                mPop.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        ToolSystemMain.getInstance().getControlApp().setBackgroundAlpha(activity, 1f);
                    }
                });
                ToolSystemMain.getInstance().getControlApp().setBackgroundAlpha(activity, 0.5f);
            }
        } catch (Exception ex) {
        }
        mPop.setData(defaultImg, imgUrl, new UnifiedPopupWindow.IUnifiedPopClickListener() {
            @Override
            public void onBodyClick() {
                mMain.pServ.unifiedPopActiondo(id, 2);
                WzFramworkApplication.getmRouter().startWebview(jumpUrl, null, false, false);
                mPop.close();
            }

            @Override
            public void onCloseClick() {
                mMain.pServ.unifiedPopActiondo(id, 1);
                mPop.close();
            }

            @Override
            public void onClose() {
                mPop.close();
            }
        });
    }


    public CommonTipsPW obtainTipsPW(CommonTipsPW commonTipsPW, final Activity activity) {
        if (commonTipsPW == null) {
            commonTipsPW = new CommonTipsPW(activity);
            commonTipsPW.setFocusable(true);
        }
        //显示弹框
        commonTipsPW.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        //设置黑色遮罩
        ToolSystemMain.getInstance().getControlApp().setBackgroundAlpha(activity, 0.5f);
        //关闭弹框时，隐藏黑色遮罩
        commonTipsPW.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ToolSystemMain.getInstance().getControlApp().setBackgroundAlpha(activity, 1f);
            }
        });
        return commonTipsPW;
    }

    public void showPWVoice(Activity context) {
        if (pw_voice == null) {
            pw_voice = new PW_Voice(context);
        }
        pw_voice.setFocusable(true);
        pw_voice.showAtLocation(context.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public void hidePWVoice() {
        if (pw_voice != null) {
            pw_voice.cancle();
        }
    }

    /**
     * 将Toast封装在一个方法中，在其它地方使用时直接输入要弹出的内容即可
     */
    public void ToastMessage(Activity context) {
        //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
        LayoutInflater inflater = context.getLayoutInflater();//调用Activity的getLayoutInflater()
        View view = inflater.inflate(R.layout.toast_custom, null);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravitytoast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件
        toast.show();
    }
}