package com.iwz.WzFramwork.mod.tool.common.system.serv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.api.ServApi;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;

import java.io.File;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/12/99:53
 * desc   :
 */
public class ToolSystemServApi extends ServApi {
    private static ToolSystemServApi mToolSystemServApi = null;
    private ToolSystemMain mMain;

    public static ToolSystemServApi getInstance(ToolSystemMain main) {
        synchronized (ToolSystemServApi.class) {
            if (mToolSystemServApi == null) {
                mToolSystemServApi = new ToolSystemServApi(main);
            }
        }
        return mToolSystemServApi;
    }

    protected ToolSystemServApi(ToolSystemMain main) {
        super(main);
        this.mMain = main;
    }

    /**
     * 接口版本号 : d.x.y.z
     * d：安卓:a，
     * x:主板本号：1，
     * y:小版本号：奇数（测试接口）/偶数（正式接口）(每次递增)
     * z:SVN号：最后提交代码的版本号
     */
    public String getApiVersion() {
        StringBuilder sbApiVersion = new StringBuilder(mMain.confApi.getmConf().getApp());
        sbApiVersion.append(".1.");
        sbApiVersion.append(mMain.confApi.getmConf().getSmallversion());
        sbApiVersion.append(".");
        sbApiVersion.append(mMain.confApi.getmConf().getSvnversion());
        return sbApiVersion.toString();
    }

    /*
     * 获取版本名称
     */
    public String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 设置Intent的data和类型，并赋予目标程序临时的URI读写权限
     *
     * @param intent    意图
     * @param type      类型
     * @param fileUri   文件uri
     * @param writeAble 是否赋予可写URI的权限
     */
    public void setIntentDataAndType(
            Intent intent,
            String type,
            Uri fileUri,
            boolean writeAble) {
        //7.0以上进行适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(fileUri, type);
            //临时赋予读写Uri的权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(fileUri, type);
        }
    }

    /**
     * 从文件获得URI
     *
     * @param file 文件
     * @return 文件对应的URI
     */
    public Uri uriFromFile(File file) {
        Uri fileUri = null;
        try {
            //7.0以上进行适配
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String p = WzFramworkApplication.getmContext().getPackageName() + ".FileProvider";
                fileUri = FileProvider.getUriForFile(WzFramworkApplication.getmContext(), p, file);
            } else {
                fileUri = Uri.fromFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUri;
    }

    /**
     * 判断网络情况
     *
     * @param context 上下文
     * @return false 表示没有网络 true 表示有网络
     */
    public boolean isNetworkAvalible(Context context) {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();
            if (net_info != null) {
                for (NetworkInfo networkInfo : net_info) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //需要获得READ_PHONE_STATE权限，>=6.0，默认返回null
    @SuppressLint("MissingPermission")
    public String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获得设备的AndroidId
     *
     * @param context 上下文
     * @return 设备的AndroidId
     */
    public String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * 获得设备序列号（如：WTK7N16923005607）, 个别设备无法获取
     *
     * @return 设备序列号
     */
    public String getSERIAL() {
        try {
            return Build.SERIAL;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获得设备硬件uuid
     * 使用硬件信息，计算出一个随机数
     *
     * @return 设备硬件uuid
     */
    public String getDeviceUUID() {
        try {
            String dev = "3883756" +
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.HARDWARE.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.SERIAL.length() % 10;
            return new UUID(dev.hashCode(),
                    Build.SERIAL.hashCode()).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * 取SHA1
     *
     * @param data 数据
     * @return 对应的hash值
     */
    public byte[] getHashByString(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.reset();
            messageDigest.update(data.getBytes("UTF-8"));
            return messageDigest.digest();
        } catch (Exception e) {
            return "".getBytes();
        }
    }

    /**
     * 转16进制字符串
     *
     * @param data 数据
     * @return 16进制字符串
     */
    public String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        String stmp;
        for (int n = 0; n < data.length; n++) {
            stmp = (Integer.toHexString(data[n] & 0xFF));
            if (stmp.length() == 1)
                sb.append("0");
            sb.append(stmp);
        }
        return sb.toString().toUpperCase(Locale.CHINA);
    }

    public boolean isMIUI() {
        return Build.MANUFACTURER.toLowerCase().equals("xiaomi");
    }

    //判断是否是华为系统 官网提供
    public boolean isEMUI() {
        //emuiApiLevel>0 即华为系统
        int emuiApiLevel = 0;
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emuiApiLevel > 0;
    }

    public float ontailWindowSize(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format((float) heightPixel / widthPixel);
        return Float.valueOf(format);
    }


    public String getExternalFileDir(Context context) {
        String filePath = "";
        try {
            //如果外部存储可用，则使用外部存储
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && !Environment.isExternalStorageRemovable()) {
                File sdFile = context.getExternalFilesDir(null);
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

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public String formetFileSize(double fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format(fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format(fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    // 获取文件
    public long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 递归删除文件夹里的文件
     *
     * @param dir 文件夹对象
     */
    public boolean deleteDirWihtFile(File dir) {
        boolean delete = false;
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return true;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        delete = dir.delete();// 删除目录本身
        return delete;
    }


    /**
     * 获得当前日期的时间戳
     *
     * @return 距离1970年1月1日0点0分0秒的毫秒数，精确到100毫秒
     */
    public String getDateNowStamp() {
        Date dt = new Date();
        String time = dt.getTime() + "";//这就是距离1970年1月1日0点0分0秒的毫秒数
        if (time.length() > 10) {
            StringBuilder sb = new StringBuilder(time.substring(0, 10));
            sb.append(".");
            sb.append(time.substring(10, time.length()));
            return sb.toString();
        } else {
            return time;
        }
    }


    /**
     * 获得新服务器时间戳，例如：1234567890.1234
     *
     * @return 服务器时间戳
     */
    public String getNewWZST(float diff) {
        Date dt = new Date();
        float nowTime = dt.getTime() / 1000;
        return String.valueOf(nowTime + diff);
    }

    /**
     * 获得新服务器时间戳，例如：1234567890.1234
     *
     * @return 服务器时间戳
     */
    public String getCurrentWZST(double diff) {
        long surrentWZST = (long) (System.currentTimeMillis() / 1000 + diff / 1000);
        return String.valueOf(surrentWZST);
    }

    /**
     * 计算系统时间与本地时间差
     *
     * @param wzst 服务器时间(秒)
     * @return 时间差 ，例如：90.1234
     */
    public double getDateInterval(double wzst) {
        Date dt = new Date();
        double nowTime = (double) (dt.getTime()) / 1000;
        return wzst - nowTime;
    }
}
