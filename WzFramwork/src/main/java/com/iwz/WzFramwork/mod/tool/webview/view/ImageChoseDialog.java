package com.iwz.WzFramwork.mod.tool.webview.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.iwz.wzframwork.R;


/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/28
 */
public class ImageChoseDialog {
    private static ImageChoseDialog mHomeImageChoseDialog;
    private AlertDialog alertDialog;
    private TextView localPhoto;
    private TextView camera;
    private TextView quxiao;
    private OnClickListener mListener;

    public static ImageChoseDialog getInstance() {
        if (mHomeImageChoseDialog == null) {
            synchronized (ImageChoseDialog.class) {
                if (mHomeImageChoseDialog == null) {
                    mHomeImageChoseDialog = new ImageChoseDialog();
                }
            }
        }
        return mHomeImageChoseDialog;
    }

    public void showDialog(Activity activity) {
        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_takephoto, null);
        alertDialog.setContentView(view);
        initView(view);

        initEvent();
    }

    private void initView(View view) {
        // 相册
        localPhoto = view.findViewById(R.id.dialog_takephoto_album);
        // 照相机
        camera = view.findViewById(R.id.dialog_takephoto_camera);
        // 取消
        quxiao = view.findViewById(R.id.dialog_takephoto_cancel);
    }

    private void initEvent() {
        // 点击拍照
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCameraClick();
                }
                alertDialog.dismiss();
            }
        });

        // 点击相册
        localPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onPhotoClick();
                }
                alertDialog.dismiss();
            }
        });

        // 取消
        quxiao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCancelClick();
                }
                alertDialog.dismiss();
            }
        });
    }

    public interface OnClickListener {
        void onCameraClick();

        void onPhotoClick();

        void onCancelClick();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mListener = listener;
    }
}
