package com.iwz.WzFramwork.mod.constants.h5;

/**
 * 描述：APP与h5端数据交互相关常量（WZApp模块）
 * 作者：小辉
 * 时间：2019/09/26
 */
public class WZAppConstants {
    /**
     * 开始录音
     */
    public static final String VOICE_START_RECORD = "voice.startRecord";
    /**
     * 暂停录音
     */
    public static final String VOICE_PAUSE_RECORD = "voice.pauseRecord";
    /**
     * 继续录音（暂停录音之后，要继续录音则调用此命令）
     */
    public static final String VOICE_RESUME_RECORD = "voice.resumeRecord";
    /**
     * 停止录音
     */
    public static final String VOICE_STOP_RECORD = "voice.stopRecord";
    /**
     * 播放录音
     */
    public static final String VOICE_PALY_VOICE = "voice.playVoice";
    /**
     * 暂停播放录音
     */
    public static final String VOICE_PAUSE_VOICE = "voice.pauseVoice";
    /**
     * 停止播放录音
     */
    public static final String VOICE_STOP_VOICE = "voice.stopVoice";
    /**
     * 获取当前配置信息
     */
    public static final String DO_GETCONFIG = "do.getConfig";
    /**
     * 获取当前APP的缓存信息（玄商拍客APP从1.0.118开始支持）
     */
    public static final String DO_GETCACHEINFO = "do.getCacheInfo";
    /**
     * 清理APP的缓存数据（玄商拍客APP从1.0.118开始支持）///1.0.119开始支持Call协议
     */
    public static final String DO_CLEARCACHE = "do.clearCache";
    /**
     * 用户选择本地文件
     */
    public static final String FS_CHOOSE_LOCALFILE = "fs.chooseLocalFile";
    /**
     * 页面信息更新
     */
    public static final String PAGEINFO_UPDATE = "webPageInfo.changed";
    /**
     * 更新当前账号信息(登录、退出)
     */
    public static final String MYINFO_UPDATE = "webMyInfo.changed";
    /**
     * Web端「退出」成功，同步信息给App
     */
    public static final String WEBLOGOUT_SUCCESS = "webLogout.success";
    /**
     * Web端「登录」成功，同步信息给App
     */
    public static final String WEBLOGIN_SUCCESS = "webLogin.success";
    /**
     * Web端「注册」成功，同步信息给App
     */
    public static final String WEBREGISTER_SUCCESS = "webRegister.success";
    /**
     * 第三方登录
     */
    public static final String OAUTH_LOGIN = "oauth.login";

    /**
     * 打开分享弹窗
     */
    public static final String WZAPP_WEBVIEW_SHOWPOPUPBUTTONS = "webview.showPopupButtons";
    /**
     * 设置弹框按钮
     */
    public static final String WZAPP_WEBVIEW_SETPOPUPBUTTONS = "webview.setPopupButtons";

    /**
     * H5调用APP分享到指定的平台
     */
    public static final String DO_SHARE_TO = "do.shareTo";
    /**
     * H5设置APP分享弹窗项（url改变则失效）
     */
    public static final String WEBVIEW_BOTTOMSHEETS_SHOW = "webview.bottomSheets.show";
    /**
     * H5设置APP分享弹窗项（只起一次作用,或url改变失效）
     */
    public static final String WEBVIEW_BOTTOMSHEETS_SET = "webview.bottomSheets.set";
    /**
     * 异步操作-开始（如：网络请求、耗时写操作）
     */
    public static final String WZAPP_ACTION_BEGIN = "action.begin";
    /**
     * 设置导航右侧按钮
     */
    public static final String WZAPP_TOPNAV_SETRIGHTBUTTON = "topNav.setRightButton";
    /**
     * 设置导航条标题
     */
    public static final String WZAPP_TOPNAV_SETTITLE = "topNav.setTitle";
    /**
     * 修改顶导航背景色
     */
    public static final String WZAPP_TOPNAV_SETBGCOLOR = "topNav.setBgColor";
    /**
     * 修改顶导航显示隐藏
     */
    public static final String WZAPP_TOPNAV_SETHIDDEN = "topNav.setHidden";
    /**
     * App的webview中发起回到APP首页命令（关闭所有webview）///1.0.119开始支持Call协议
     */
    public static final String WZAPP_ROOTER_GOAPPHOME = "rooter.goAppHome";
    /**
     * App的webview中发起跳转至直播页面
     */
    public static final String WZAPP_ROOTER_GOALIVE = "router.goLive";
    /**
     * App的webview中将直播数据传递给APP
     */
    public static final String WZAPP_LIVE_SETDATA = "live.setData";
    /**
     * App的webview中发起设置显隐app首页tabbar///1.0.119开始支持Call协议
     */
    public static final String WZAPP_ROOTER_SETAPPTABBAR = "rooter.setAppTabbar";
    /**
     * Web端通知APP打开微信小程序///1.0.119开始支持Call协议
     */
    public static final String WZAPP_ROOTER_OPENMINIPROGRAM = "rooter.openMiniProgram";
    /**
     * App的webview中发起检测当前APP版本命令///1.0.119开始支持Call协议
     */
    public static final String WZAPP_DO_CHECKAPPVERSION = "do.checkAppVersion";
    /**
     * App的webview中发起给我们评分吧命令///1.0.119开始支持Call协议
     */
    public static final String WZAPP_DO_APPSCORE = "do.appScore";
    /**
     * 设置页面展示类型
     */
    public static final String DO_SETSCREENMODE = "do.setScreenMode";
    /**
     * web页面横屏展示
     */
    public static final String DO_LANDSCAPE = "do.landscape";
    /**
     * web页面取消横屏
     */
    public static final String DO_UNLANDSCAPE = "do.unLandscape";
    /**
     * 页面前进或后退
     */
    public static final String HISTORY_GO = "history.go";
}