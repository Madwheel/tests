package com.iwz.WzFramwork.mod.tool.common.system.control;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.mod.bus.event.BusEventMain;
import com.iwz.WzFramwork.mod.constants.FMAppConstants;
import com.iwz.WzFramwork.mod.core.env.CoreEnvMain;
import com.iwz.WzFramwork.mod.tool.common.file.ToolFileMain;
import com.iwz.WzFramwork.mod.tool.common.file.conf.FilePathType;
import com.iwz.WzFramwork.mod.tool.common.img.ToolImgMain;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;
import com.iwz.WzFramwork.mod.tool.common.system.model.JVersion;
import com.iwz.WzFramwork.mod.tool.permission.PermissionUtils;
import com.iwz.WzFramwork.mod.tool.permission.request.RequestPermissions;
import com.iwz.WzFramwork.mod.tool.permission.requestresult.IGetRequestPermissionsResult;
import com.iwz.WzFramwork.mod.tool.permission.requestresult.RequestPermissionsResultSetApp;
import com.iwz.WzFramwork.mod.tool.webview.model.EImageObtainOk;
import com.iwz.WzFramwork.mod.tool.webview.model.IScreenModeListener;
import com.iwz.WzFramwork.mod.tool.webview.model.IWebviewGoListener;
import com.iwz.WzFramwork.mod.tool.webview.view.ImageChoseDialog;
import com.iwz.WzFramwork.mod.tool.webview.view.MyWebview;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/12/99:46
 * desc   :
 */
public class ToolSystemControlApp extends ControlApp {
    private static ToolSystemControlApp mToolSystemControlApp = null;
    private ToolSystemMain mMain;
    private Typeface mTypeface;

    public static ToolSystemControlApp getInstance(ToolSystemMain main) {
        synchronized (ToolSystemControlApp.class) {
            if (mToolSystemControlApp == null) {
                mToolSystemControlApp = new ToolSystemControlApp(main);
            }
        }
        return mToolSystemControlApp;
    }

    protected ToolSystemControlApp(ToolSystemMain main) {
        super(main);
        mMain = main;
    }

    @Override
    public void born() {
        super.born();
        initModelData();
        mTypeface = mMain.confApi.getPingFang();
    }


    private void initModelData() {
        //1、VersionToModel
        JVersion jVersion = new JVersion();
        jVersion.setVersionName(getVersionName());
        jVersion.setVersionApi(mMain.servApi.getApiVersion());
        jVersion.setVersionSvn(String.valueOf(mMain.confApi.getmConf().getSvnversion()));
        StringBuilder sbApiVersion = new StringBuilder();
        sbApiVersion.append(jVersion.getVersionApi());
        if (CoreEnvMain.getInstance().isDev()) {
            sbApiVersion.append(".dev");
        }
        jVersion.setDisplayApiVersion(sbApiVersion.toString());
        StringBuilder sb = new StringBuilder();
        sb.append("Version:").append(getVersionName()).append(" API:").append(jVersion.getDisplayApiVersion());
        jVersion.setAppApiVersion(sb.toString());
        mMain.modelApi.setVersion(jVersion);
    }

    public boolean isNetworkAvalible() {
        return mMain.servApi.isNetworkAvalible(WzFramworkApplication.getmContext());
    }

    public String getExternalFileDir() {
        String filePath = "";
        try {
            //如果外部存储可用，则使用外部存储
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && !Environment.isExternalStorageRemovable()) {
                File sdFile = WzFramworkApplication.getmContext().getExternalFilesDir(null);
                if (sdFile != null) {
                    filePath = sdFile.getPath();
                }
                if (filePath == null || filePath.length() == 0) {
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                }
                if (filePath == null || filePath.length() == 0) {
                    filePath = Environment.getDownloadCacheDirectory().getAbsolutePath();
                }
                if (filePath == null || filePath.length() == 0) {
                    filePath = Environment.getRootDirectory().getAbsolutePath();
                }
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return filePath;
    }

    private String getVersionName() {
        return mMain.servApi.getVersionName(WzFramworkApplication.getmContext());
    }

    public JVersion getVersion() {
        return mMain.modelApi.getVersion();
    }

    private File file;

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent, Activity activity) {
        String filePath = ToolFileMain.getInstance().mControl.getFilePath(FilePathType.SD_CACHE);
        if (filePath == null || filePath.length() == 0) {
            Toast.makeText(activity, "未检测到SD卡，无法做头像处理！", Toast.LENGTH_SHORT).show();
            return;
        }
        File outputFile = new File(filePath, "tupian_out.jpg");//裁切后输出的图片
        try {
            //如果不存在，创建文件
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
        } catch (Exception ex) {
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == FMAppConstants.REQUEST_CODE_ZHAOPIAN) {
                //相册选择图片完毕，进行图片裁切
                if (intent == null || intent.getData() == null) {
                    return;
                }
                Uri zhaopianFileUri = intent.getData();
                outputImg(activity, zhaopianFileUri, outputFile);
            } else if (requestCode == FMAppConstants.REQUEST_CODE_PAIZHAO) {
                Uri paizhaoFileUri = uriFromFile(file);
                outputImg(activity, paizhaoFileUri, outputFile);
            } else if (requestCode == FMAppConstants.REQUEST_OUTPUTIMG) {
                try {
                    Uri uri = Uri.fromFile(outputFile);
                    Bitmap bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
                    EImageObtainOk eImageObtainOk = new EImageObtainOk();
                    eImageObtainOk.setUrl(ToolImgMain.getInstance().mServ.bitmapToBase64(bitmap));
                    BusEventMain.getInstance().publish(eImageObtainOk);
                } catch (Exception ex) {

                }
            }
        }
    }

    /**
     * 打开图片裁切
     */
    public void outputImg(Activity activity, Uri uri, File outputFile) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        mMain.servApi.setIntentDataAndType(intent, "image/*", uri, true);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        Uri outputUri = Uri.fromFile(outputFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, FMAppConstants.REQUEST_OUTPUTIMG);
    }

    public Uri uriFromFile(File file) {
        return mMain.servApi.uriFromFile(file);
    }

    /**
     * 设置屏幕状态栏状态
     *
     * @param activity
     * @param mode     0：默认状态，1：全屏状态
     */
    public void setScreenMode(Activity activity, int mode, String statusBarColor, IScreenModeListener listener) {
        if (mode == 0) {//默认状态
            if (listener != null) {
                listener.normalScreenMode();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                // 跟app同颜色的状态栏
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //自定义状态栏背景
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        } else {//设置全屏
            if (listener != null) {
                listener.fullScreenMode(statusBarColor);
            }
            if (TextUtils.isEmpty(statusBarColor)) {
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
    }

    /**
     * 2、选择照片
     *
     * @param activity
     */
    public void chooseLocalFile(final Activity activity) {
        String filePath = ToolFileMain.getInstance().mControl.getFilePath(FilePathType.SD_CACHE);
        if (filePath == null || filePath.length() == 0) {
            Toast.makeText(activity, "未检测到SD卡，无法做头像处理！", Toast.LENGTH_SHORT).show();
            return;
        }
        file = new File(filePath, "paizhao.jpg");
        RequestPermissionsResultSetApp.getInstance().getRequestPermissionsResult(null);
        ImageChoseDialog.getInstance().showDialog(activity);
        ImageChoseDialog.getInstance().setOnClickListener(new ImageChoseDialog.OnClickListener() {
            @Override
            public void onCameraClick() { // 点击拍照
                RequestPermissionsResultSetApp.getInstance().getRequestPermissionsResult(new IGetRequestPermissionsResult() {
                    @Override
                    public void callBack(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults) {
                        List<String> deniedPermission = new ArrayList<>();
                        for (int i = 0; i < permissions.length; i++) {
                            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                                deniedPermission.add(permissions[i]);
                            }
                        }
                        //已全部授权
                        if (deniedPermission.size() == 0) {
                            paizhao(activity, file);
                        }
                    }
                });
                //判断是否有相机权限
                if (!RequestPermissions.getInstance().requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PermissionUtils.ResultCode1)) {
                    return;
                }
                paizhao(activity, file);
            }

            @Override
            public void onPhotoClick() {// 点击相册
                zhaopian(activity);
            }

            @Override
            public void onCancelClick() { // 取消

            }
        });
    }

    /**
     * 打开相机拍照
     */
    public void paizhao(Activity activity, File outputFile) {
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = ToolSystemMain.getInstance().getControlApp().uriFromFile(outputFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, FMAppConstants.REQUEST_CODE_PAIZHAO);
    }

    /**
     * 打开相册
     */
    public void zhaopian(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        activity.startActivityForResult(intent, FMAppConstants.REQUEST_CODE_ZHAOPIAN);
    }

    /**
     * 5、webview前进后退
     *
     * @param count
     * @param webView
     * @param listener
     */
    public void goBackOrForward(int count, MyWebview webView, IWebviewGoListener listener) {
        int currentIndex = webView.getCurrentIndex();
        if (count < 0 && count + currentIndex < 0) {
            if (listener != null) {
                listener.webviewClose();
            }
        } else if (count == 0) {
            webView.reload();
        } else {
            webView.goBackOrForward(count);
        }
    }

    /**
     * 设置屏幕方向
     *
     * @param activity
     * @param orientation 0：横屏，1：竖屏
     */
    public void setScreenOrient(Activity activity, int orientation) {
        if (orientation == 0) {//横屏
            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else if (orientation == 1) {//竖屏
            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    /**
     * 设置屏幕的背景透明度
     */
    public void setBackgroundAlpha(final Activity activity, final float bgAlpha) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {//是主线程
            setBackground(activity, bgAlpha);
        } else {//非主线程
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setBackground(activity, bgAlpha);
                }
            });
        }
    }

    private void setBackground(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        if (bgAlpha == 1) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        activity.getWindow().setAttributes(lp);
    }

    // 两次点击间隔不能少于1000ms
    private final int FAST_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0l;

    public boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME) {
            flag = false;
            lastClickTime = currentClickTime;
        }
        return flag;
    }

    /**
     * 设置TextView字体样式
     *
     * @param textView 文本控件
     */
    public void setFontStyle(TextView textView) {
        textView.setTypeface(mTypeface);
    }

    /**
     * 设置TextView字体样式
     *
     * @param texts 文本控件
     */
    public void setFontStyle(TextView[] texts) {
        for (int i = 0; i < texts.length; i++) {
            texts[i].setTypeface(mTypeface);
        }
    }

    public void setFontColor(TextView textView, int[] colors) {
        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0, textView.getPaint().getTextSize(), colors, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(mLinearGradient);
        textView.invalidate();
    }

    /**
     * 设置TextView字体样式、字体大小（sp）
     *
     * @param textView 文本控件
     * @param textSize 文本大小（sp）
     */
    public void setFontStyle(TextView textView, int textSize) {
        textView.setTypeface(mTypeface);
        // 设置文本大小
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    /**
     * 设置TextView字体样式、字体大小（sp）、颜色
     *
     * @param textView 文本控件
     * @param textSize 文本大小（sp）
     * @param color    文本颜色
     */
    public void setFontStyle(TextView textView, int textSize, int color) {
        textView.setTypeface(mTypeface);
        // 设置文本大小
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        // 设置文本颜色
        textView.setTextColor(color);
    }

    /**
     * 设置TextView字体样式
     *
     * @param editText 文本输入控件
     */
    public void setFontStyle(EditText editText) {
        editText.setTypeface(mTypeface);
    }

    /**
     * 设置EditText字体样式、字体大小（sp）
     *
     * @param editText 文本输入控件
     * @param textSize 文本大小（sp）
     */
    public void setFontStyle(EditText editText, int textSize) {
        editText.setTypeface(mTypeface);
        //设置文本大小
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    /**
     * 设置TextView字体样式、字体大小（sp）、颜色
     *
     * @param editText 文本输入控件
     * @param textSize 文本大小（sp）
     * @param color    文本颜色
     */
    public void setFontStyle(EditText editText, int textSize, int color) {
        editText.setTypeface(mTypeface);
        //设置文本大小
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        //设置文本颜色
        editText.setTextColor(color);
    }

    /**
     * 设置Button字体样式
     *
     * @param btn 文本输入控件
     */
    public void setFontStyle(Button btn) {
        btn.setTypeface(mTypeface);
    }

    /**
     * 设置Button字体样式、字体大小（sp）
     *
     * @param btn      文本输入控件
     * @param textSize 文本大小（sp）
     */
    public void setFontStyle(Button btn, int textSize) {
        btn.setTypeface(mTypeface);
        //设置文本大小
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    /**
     * 设置Button字体样式、字体大小（sp）、颜色
     *
     * @param btn      文本输入控件
     * @param textSize 文本大小（sp）
     * @param color    文本颜色
     */
    public void setFontStyle(Button btn, int textSize, int color) {
        btn.setTypeface(mTypeface);
        //设置文本大小
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        //设置文本颜色
        btn.setTextColor(color);
    }

    public void setTextViewStyles(TextView text, String startColor, String endColor) {
        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0, text.getPaint().getTextSize(), Color.parseColor(startColor), Color.parseColor(endColor), Shader.TileMode.CLAMP);
        text.getPaint().setShader(mLinearGradient);
        text.invalidate();
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue（DisplayMetrics类中属性density）
     * @return
     */
    public float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f);
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return 转为像素后的值
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public float px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return 转为像素后的值
     */
    public int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    public boolean isNormalWindow(Activity activity) {
        float size = ontailWindowSize(activity);
        if (Math.abs(size - 1.78) <= Math.abs(size - 2.17)) {
            return true;
        } else {
            return false;
        }
    }

    public float ontailWindowSize(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format((float) heightPixel / widthPixel);
        Float aFloat = Float.valueOf(format);
        return aFloat;
    }

    /**
     * 比较两个list是否相同
     *
     * @param arraylist1
     * @param arraylist2
     * @return
     */
    public boolean compareList(List<String> arraylist1, List<String> arraylist2) {
        int size = arraylist1.size();
        if (size == arraylist2.size()) {
            for (int i = 0; i < size; i++) {
                if (arraylist1.get(i).equals(arraylist2.get(i))) {
                    if (i == size - 1) {
                        return true;
                    }
                    continue;
                }

            }
        }
        return false;
    }

    public String getAndroidId(Context context) {
        return mMain.servApi.getAndroidId(context);
    }

    /**
     * 获得手机平台信息：设备品牌、手机的型号 设备名称
     */
    public String getPhonePlat() {
        StringBuilder sb = new StringBuilder();
        sb.append(android.os.Build.BRAND);
        sb.append(",");
        sb.append(android.os.Build.MODEL);
        return sb.toString();
    }

    /**
     * 获得设备硬件标识
     *
     * @param context 上下文
     * @return 设备硬件标识
     */
    public String getDeviceId(Context context) {
        StringBuilder sbDeviceId = new StringBuilder();

        //获得设备默认IMEI（>=6.0 需要ReadPhoneState权限）
        String imei = mMain.servApi.getIMEI(context);
        //获得AndroidId（无需权限）
        String androidid = mMain.servApi.getAndroidId(context);
        //获得设备序列号（无需权限）
        String serial = mMain.servApi.getSERIAL();
        //获得硬件uuid（根据硬件相关属性，生成uuid）（无需权限）
        String uuid = mMain.servApi.getDeviceUUID().replace("-", "");
        //获得硬件uuid（根据硬件相关属性，生成uuid）（无需权限）
        String packageName = context.getPackageName();

        //追加imei
        if (imei != null && imei.length() > 0) {
            sbDeviceId.append(imei);
            sbDeviceId.append("|");
        }
        //追加androidid
        if (androidid != null && androidid.length() > 0) {
            sbDeviceId.append(androidid);
            sbDeviceId.append("|");
        }
        //追加serial
        if (serial != null && serial.length() > 0) {
            sbDeviceId.append(serial);
            sbDeviceId.append("|");
        }
        //追加硬件uuid
        if (uuid != null && uuid.length() > 0) {
            sbDeviceId.append(uuid);
        }
        //追加硬件uuid
        if (packageName != null && packageName.length() > 0) {
            sbDeviceId.append(packageName);
        }
        //生成SHA1，统一DeviceId长度
        if (sbDeviceId.length() > 0) {
            try {
                byte[] hash = mMain.servApi.getHashByString(sbDeviceId.toString());
                String sha1 = mMain.servApi.bytesToHex(hash);
                if (sha1 != null && sha1.length() > 0) {
                    //返回最终的DeviceId
                    return sha1;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //如果以上硬件标识数据均无法获得，
        //则DeviceId默认使用系统随机数，这样保证DeviceId不为空
        return UUID.randomUUID().toString().replace("-", "");
    }

    //判断是否是华为系统 官网提供
    public boolean isEMUI() {
        return mMain.servApi.isEMUI();
    }

    public boolean isMIUI() {
        return mMain.servApi.isMIUI();
    }

    public String getExternalFileDir(Context context) {
        return mMain.servApi.getExternalFileDir(context);
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public String formetFileSize(double fileS) {
        return mMain.servApi.formetFileSize(fileS);
    }

    // 获取文件
    public long getFolderSize(File file) {
        return mMain.servApi.getFolderSize(file);
    }

    /**
     * 递归删除文件夹里的文件
     *
     * @param dir 文件夹对象
     */
    public boolean deleteDirWihtFile(File dir) {
        return mMain.servApi.deleteDirWihtFile(dir);
    }

    /**
     * 获得当前日期的时间戳
     *
     * @return 距离1970年1月1日0点0分0秒的毫秒数，精确到100毫秒
     */
    public String getDateNowStamp() {
        return mMain.servApi.getDateNowStamp();
    }

    /**
     * 计算系统时间与本地时间差
     *
     * @param wzst 服务器时间(秒)
     * @return 时间差 ，例如：90.1234
     */
    public double getDateInterval(double wzst) {
        return mMain.servApi.getDateInterval(wzst);
    }

    /**
     * 获得新服务器时间戳，例如：1234567890.1234
     *
     * @return 服务器时间戳
     */
    public String getCurrentWZST(double diff) {
        return mMain.servApi.getCurrentWZST(diff);
    }

    //屏幕适配方案与该方法冲突，字体大小不变设置不起效
    public void setDefaultFontSize(Resources resources) {//适配8.0
        // 加载系统默认设置，字体不随用户设置变化
//        android.content.res.Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
    }
}
