package com.iwz.WzFramwork.mod.audio.conf;

import com.iwz.WzFramwork.mod.audio.BizAudioMain;
import com.iwz.WzFramwork.base.api.ConfApi;
import com.iwz.WzFramwork.mod.tool.common.file.ToolFileMain;
import com.iwz.WzFramwork.mod.tool.common.file.conf.FilePathType;

import java.io.File;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/09/27
 */
public class BizAudioConf extends ConfApi {
    protected BizAudioConf(BizAudioMain main) {
        super(main);
    }

    private static BizAudioConf mToolAudioConf;

    public static BizAudioConf getInstance(BizAudioMain main) {
        if (mToolAudioConf == null) {
            synchronized (BizAudioConf.class) {
                if (mToolAudioConf == null) {
                    mToolAudioConf = new BizAudioConf(main);
                }
            }
        }
        return mToolAudioConf;
    }

    public String getRecordType() {///TODO:可配置
        return "amr";
    }

    public String getRoutPath() {
        String routPath = ToolFileMain.getInstance().mControl.getFilePath(FilePathType.DATA_FILE);
        routPath = ToolFileMain.getInstance().mControl.getFilePath(FilePathType.SD_CACHE);
        return routPath + "/audio" + "/record";
    }

    /**
     * 根据url获取文件本地存储位置
     *
     * @param url     音频文件服务器地址
     * @return
     */
    public String getFilePath( String url) {
        String fileName = "";
        if (url.contains("/")) {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        }
        String routPath = ToolFileMain.getInstance().mControl.getFilePath(FilePathType.DATA_FILE);
        routPath = ToolFileMain.getInstance().mControl.getFilePath(FilePathType.SD_CACHE);
        return routPath + File.separator + "audio" + File.separator + fileName;
    }
}
