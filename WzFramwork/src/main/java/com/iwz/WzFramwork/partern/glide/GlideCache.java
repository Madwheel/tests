package com.iwz.WzFramwork.partern.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.module.GlideModule;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;

import java.io.File;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/9/2915:31
 * desc   :
 */
public class GlideCache implements GlideModule {
    @Override
    public void applyOptions(final Context context, GlideBuilder glideBuilder) {
        //设置图片的显示格式RGB_565(ARGB_8888指图片大小为32bit,RGB_565图片较小，加载速度快)
//        glideBuilder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
        //设置磁盘缓存目录（和创建的缓存目录相同）
        String path = ToolSystemMain.getInstance().getControlApp().getExternalFileDir();
        //缓存目录跟api在同一个根目录下
        StringBuilder sbFilePath = new StringBuilder(path);
        sbFilePath.append("/GlideCache");
        final String downloadDirectoryPath = sbFilePath.toString();
        //设置缓存的大小为100M
        final int cacheSize = 104857600;
        glideBuilder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                //创建文件夹，将图片资源存到这里
                File cacheLocation = new File(downloadDirectoryPath);
                cacheLocation.mkdirs();
                return DiskLruCacheWrapper.get(cacheLocation, cacheSize);
            }
        });
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}

