package com.iwz.WzFramwork.mod.tool.common.file.control;

import android.os.Environment;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.app.ControlApp;
import com.iwz.WzFramwork.mod.tool.common.file.ToolFileMain;
import com.iwz.WzFramwork.mod.tool.common.file.conf.FilePathType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/09/26
 */
public class FileConrtolApp extends ControlApp {

    protected FileConrtolApp(ToolFileMain main) {
        super(main);
    }

    private static FileConrtolApp mFileConrtolApp;

    public static FileConrtolApp getInstance(ToolFileMain main) {
        if (mFileConrtolApp == null) {
            synchronized (FileConrtolApp.class) {
                if (mFileConrtolApp == null) {
                    mFileConrtolApp = new FileConrtolApp(main);
                }
            }
        }
        return mFileConrtolApp;
    }

    /**
     * 判断手机SD卡是否可用
     *
     * @return 如果可用返回true，反之，返回false!
     */
    private boolean isCanUseSdCard() {
        try {
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String getFilePath(FilePathType type) {
        try {
            if (isCanUseSdCard()) {
                //获得SD卡目录/mnt/sdcard（获取的是手机外置sd卡的路径）
                if (type == FilePathType.SD_ROOT) {
                    return Environment.getExternalStorageDirectory().getPath();
                }
                //用于获取APP的在SD卡中的file目录/mnt/sdcard/Android/data/<application package>/file
                if (type == FilePathType.SD_FILE) {
                    return WzFramworkApplication.getmContext().getExternalCacheDir().getPath();
                }
                //用于获取APP的在SD卡中的cache目录/mnt/sdcard/Android/data/<application package>/cache
                if (type == FilePathType.SD_CACHE) {
                    return WzFramworkApplication.getmContext().getExternalFilesDir(null).getPath();
                }
            }
            //用于获取APP的files目录 /data/data/<application package>/files
            if (type == FilePathType.DATA_FILE) {
                return WzFramworkApplication.getmContext().getFilesDir().getPath();
            }
            //用于获取APP的cache目录 /data/data/<application package>/cache目录
            if (type == FilePathType.DATA_CACHE) {
                return WzFramworkApplication.getmContext().getCacheDir().getPath();
            }
            //使用DATA_FILE兜底
            return WzFramworkApplication.getmContext().getFilesDir().getPath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public File getFile(String filePath) {
        if (filePath == null || filePath.length() == 0) {
            return null;
        }
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    float size = 0;

    public float getFileSize(String filePath) {
        if (filePath == null || filePath.length() == 0) {
            return 0;
        }
        try {
            File file = new File(filePath);
            if (file.exists()) {
                size = 0;
                return getSize(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private float getSize(File file) {
        try {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                for (File f : children) {
                    size += getSize(f);
                }
                return size;
            }
            //如果是文件则直接返回其大小
            else {
                return (float) file.length();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return size;
    }

    /**
     * 文件大小单位转换
     *
     * @param size
     * @return
     */
    public String setFileSize(long size) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));

        if (f < 1.0) {
            float f2 = ((float) size / (float) (1024));

            return df.format(new Float(f2).doubleValue()) + "KB";

        } else {
            return df.format(new Float(f).doubleValue()) + "M";
        }
    }

    public File newFile(String filePath, String fileName) {
        if (filePath == null || filePath.length() == 0 || fileName == null || fileName.length() == 0) {
            return null;
        }
        try {
            //判断目录是否存在，如果不存在，递归创建目录
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //组织文件路径
            StringBuilder sbFile = new StringBuilder(filePath);
            if (!filePath.endsWith("/")) {
                sbFile.append("/");
            }
            sbFile.append(fileName);

            //创建文件并返回文件对象
            File file = new File(sbFile.toString());
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void removeFile(String filePath) {
        if (filePath == null || filePath.length() == 0) {
            return;
        }
        try {
            File file = new File(filePath);
            if (file.exists()) {
                removeFile(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                removeFile(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    void removeFile(File file) {
        //如果是文件直接删除
        if (file.isFile()) {
            file.delete();
            return;
        }
        //如果是目录，递归判断，如果是空目录，直接删除，如果是文件，遍历删除
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                removeFile(f);
            }
            file.delete();
        }
    }

    public void copyFile(String filePath, String newDirPath) {
        if (filePath == null || filePath.length() == 0) {
            return;
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            //判断目录是否存在，如果不存在，则创建
            File newDir = new File(newDirPath);
            if (!newDir.exists()) {
                newDir.mkdirs();
            }
            //创建目标文件
            File newFile = newFile(newDirPath, file.getName());
            InputStream is = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[4096];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();// 刷新缓冲区
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyDir(String dirPath, String newDirPath) {
        if (dirPath == null || dirPath.length() == 0 || newDirPath == null || newDirPath.length() == 0) {
            return;
        }
        try {
            File file = new File(dirPath);
            if (!file.exists() && !file.isDirectory()) {
                return;
            }
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return;
            }
            File newFile = new File(newDirPath);
            newFile.mkdirs();
            for (File fileTemp : childFile) {
                if (fileTemp.isDirectory()) {
                    copyDir(fileTemp.getPath(), newDirPath + "/" + fileTemp.getName());
                } else {
                    copyFile(fileTemp.getPath(), newDirPath);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveFile(String filePath, String newDirPath) {
        if (filePath == null || filePath.length() == 0 || newDirPath == null || newDirPath.length() == 0) {
            return;
        }
        try {
            //拷贝文件
            copyFile(filePath, newDirPath);
            //删除原文件
            removeFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveDir(String dirPath, String newDirPath) {
        if (dirPath == null || dirPath.length() == 0 || newDirPath == null || newDirPath.length() == 0) {
            return;
        }
        try {
            //拷贝目录
            copyDir(dirPath, newDirPath);
            //删除目录
            removeFile(dirPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
