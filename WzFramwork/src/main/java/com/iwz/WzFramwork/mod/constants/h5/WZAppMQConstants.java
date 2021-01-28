package com.iwz.WzFramwork.mod.constants.h5;

/**
 * 描述：APP与h5端数据交互相关常量（WZAppMQ模块）
 * 作者：小辉
 * 时间：2019/09/26
 */
public class WZAppMQConstants {
    /**
     * 强制停止录音
     */
    public static final String VOICE_BREAK_RECORD = "voice.breakRecord";
    /**
     * 播放录音结束
     */
    public static final String VOICE_PLAY_END = "voice.playEnd";
    /**
     * 自定义海报图片上传 (由于iOS 11.4.1 wkwebview下 h5调用iOS相机黑屏，需要改用命令传图片给h5生成海报)。
     */
    public static final String POSTER_IMAGE_UPLOAD = "poster.imageUpload";

    /**
     * 第三方登录
     */
    public static final String OAUTH_LOGIN_RESULT = "oauth.login.result";

    /**
     * 设置清理缓存信息（玄商拍客APP从1.0.118开始支持）
     */
    public static final String CACHE_APPCLEAR = "cache.appClearInfo";
}
