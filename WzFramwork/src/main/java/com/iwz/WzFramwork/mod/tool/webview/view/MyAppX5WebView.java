package com.iwz.WzFramwork.mod.tool.webview.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Toast;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.mod.bus.event.BusEventMain;
import com.iwz.WzFramwork.mod.bus.event.control.WZAppJsInterface;
import com.iwz.WzFramwork.mod.core.env.CoreEnvMain;
import com.iwz.WzFramwork.mod.tool.common.UrlParse;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;
import com.iwz.WzFramwork.mod.tool.url.ToolUrlMain;
import com.iwz.WzFramwork.mod.tool.webview.interfaces.IWebViewClient;
import com.iwz.WzFramwork.mod.tool.webview.model.EWebviewLoadFinish;
import com.iwz.WzFramwork.mod.tool.webview.model.EWebviewUrlChange;

import java.util.ArrayList;

public class MyAppX5WebView<onReceivedError> extends MyWebview {
    // 1 电话
    public final String TelHost = "tel:";
    // 2 短信
    public final String SmsHost = "sms:";
    // 3 邮件
    public final String MailtoHost = "mailto:";
    // 第一次加载的时候不拦截，加载之后如果再点击才拦截
    public boolean isIntercept = false;
    private String redirectUrl;
    private String originalUrl = "";
    private boolean isBack = false;
    private boolean mIsOpenNewView = false;
    private IWebViewErrorListener mWebViewErrorListener;
    private String newUrl = "";
    private boolean mIsReFresh = false;
    private IReloadListener mIReloadListener = null;
    private String mUrl = "";
    private IEventListener mIEventListener;

    public MyAppX5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        this.addJavascriptInterface(new WZAppJsInterface(this), "WZApp");
        initAppWebViewSettings();
        initEvent();
    }

    public void setEventListener(IEventListener listener) {
        this.mIEventListener = listener;
    }

    public interface IEventListener {
        void identifyPic(String url);

        void startActivity(Intent intent);

        void openBrowser(String url);

        void showCommonWebview(String url);

        void cancleDownLoad();
    }

    public void onBackClick() {
        isBack = true;
    }

    private void initEvent() {
        //webview长按点击，直接OnLongClick无法监听到
        this.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                String realImgUrl = "";
                int type = getType(v);
                if (type == HitTestResult.IMAGE_TYPE || type == HitTestResult.IMAGE_ANCHOR_TYPE || type == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {//判断被点击的类型为图片
                    String imgUrl = getExtra(v);
                    if (type == HitTestResult.IMAGE_ANCHOR_TYPE || type == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                        if (imgUrl.contains("?")) {
                            realImgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
                        } else {
                            realImgUrl = imgUrl;
                        }
                    } else {
                        realImgUrl = imgUrl;
                    }
                    if (mIEventListener != null) {
                        mIEventListener.identifyPic(realImgUrl);
                    }
                }
            }
        });
    }

    public void isOpenNewView(boolean isOpenNewView) {
        this.mIsOpenNewView = isOpenNewView;
    }

    public interface IWebViewErrorListener {
        void onReceivedError();

        void onReceivedError(int i, String s, String s1);

        void onPageStarted(String url, Bitmap bitmap);

        void onPageFinished(String url);
    }

    public void setOnWebViewErrorListener(IWebViewErrorListener webViewErrorListener) {
        this.mWebViewErrorListener = webViewErrorListener;
    }

    public void initAppWebViewSettings() {
//        super.initWebViewSettings();
        IWebViewClient webViewClient = new IWebViewClient() {
            public String currentUrl;

            @Override
            public boolean shouldOverrideUrlLoading(View view, String url) {
                //1、特殊情况
                if (url.startsWith(TelHost) || url.startsWith(SmsHost) || url.startsWith(MailtoHost)) {
                    specialRedirect(url);
                    return true;
                }
                this.currentUrl = url;
                //2、如果是第一次加载URL，不拦截
                if (!isIntercept) {
                    originalUrl = url;
                    checkUrlChange(url);
                    return false;
                }
                //启动浏览器
                if (url.startsWith("weixin://") || url.startsWith("alipays://") || url.startsWith("mqqapi://")) {
                    if (mIEventListener != null) {
                        mIEventListener.openBrowser(url);
                    }
                    return true;
                }
                //h5中标记启动新页面
                if (url.startsWith("newtab:")) {
                    url = url.substring(7, url.length());
                    //弹出新页面
                    if (mIEventListener != null) {
                        mIEventListener.showCommonWebview(url);
                    }
                    return true;
                }
                //拦截URL，做相应处理
                //1.根据URL白名单跳转指定页面（新闻详情、股票详情、研报详情、公告详情、概念、地域、行业、.pdf、.doc等文件）
                if (url.contains(".")) {
                    String urlTemp = url.toLowerCase();
                    if (urlTemp.contains(".pdf") || urlTemp.contains(".doc") || urlTemp.contains(".docx") || urlTemp.contains(".wps")
                            || urlTemp.contains(".xls") || urlTemp.contains(".ppt") || urlTemp.contains(".txt") || urlTemp.contains(".rar")
                            || urlTemp.contains(".zip") || urlTemp.contains(".mp3") || urlTemp.contains(".mp4") || urlTemp.contains(".apk")) {
                        //.pdf、.doc等文件
                        if (mIEventListener != null) {
                            mIEventListener.openBrowser(url);
                        }
                        return true;
                    }
                }
                if (mIsOpenNewView) {
                    if (mIEventListener != null) {
                        mIEventListener.showCommonWebview(url);
                    }
                    return true;
                }
                checkUrlChange(url);
                //如果与原始URL（originalUrl）域名一样，在当前页面里直接刷新
                String urlHostAndPath = UrlParse.getUrlHostAndPath(url);
                if (urlHostAndPath.length() > 0 && originalUrl.contains(urlHostAndPath)) {
                    return false;
                }
                return false;
            }

            @Override
            public void onPageFinished(String url) {
                //页面加载完成后加载下面的javascript，修改页面中所有用target="_blank"标记的url（在url前加标记为“newtab”）
                //这里要注意一下那个js的注入方法，不要在最后面放那个替换的方法，不然会出错
//                webView.loadUrl("javascript: var allLinks = document.getElementsByTagName('a'); if (allLinks) {var i;for (i=0; i<allLinks.length; i++) {var link = allLinks[i];var target = link.getAttribute('target'); if (target && target == '_blank') {link.href = 'newtab:'+link.href;link.setAttribute('target','_self');}}}");
                justLoadUrl("javascript:var allLinks = document.getElementsByTagName('a');javascript:var allLinks = document.getElementsByTagName('a');(function(links){function set(link) {if(link && link.target === '_blank'){var href = link.href || '';if(/^https?:/i.test(href) || !/^([\\w-\\.])+:/.test(href)){href = 'newtab:' + href;link.href = href;}}}for(var i=0;i < links.length;i++) {set(links[i])}})(document.getElementsByTagName('A'))");
                isIntercept = true;
                BusEventMain.getInstance().publish(new EWebviewLoadFinish());
                if (mIEventListener != null) {
                    mIEventListener.cancleDownLoad();
                }
            }

            @Override
            public void doUpdateVisitedHistory(String s, boolean b) {
                if (!TextUtils.isEmpty(newUrl) && newUrl.equals(ToolUrlMain.getInstance().toURLDecoder(s))) {

                } else {
                    EWebviewUrlChange eWebviewUrlChange = new EWebviewUrlChange();
                    eWebviewUrlChange.setUrl(s);
                    BusEventMain.getInstance().publish(eWebviewUrlChange);
                }
                newUrl = ToolUrlMain.getInstance().toURLDecoder(s);
                if (isClearHistory) {
                    clearHistory();
                    originalUrl = s;
                    isClearHistory = false;
                }
            }

            @Override
            public void onPageStarted(String url, Bitmap bitmap) {
                this.currentUrl = url;
            }

            @Override
            public void onReceivedError(boolean isForMainFrame) {
                if (isForMainFrame) {//是否是为 main frame创建
//                    webView.loadUrl("about:blank");// 避免出现默认的错误界面
                    if (!isBack) {
                        justLoadUrl(getNoNetUrl());// 加载自定义错误页面
                    }
                    isBack = false;
                }
            }

            private String getNoNetUrl() {
                return "file:///android_asset/page/nonet.html";
            }
        };
        setWebViewClient(webViewClient);
        String userAgent = getUserAgentString()
                + ";XuanShang(android)/" + ToolSystemMain.getInstance().getControlApp().getVersion().getVersionName()
                + "," + ToolSystemMain.getInstance().getControlApp().getVersion().getVersionApi()
                + "," + CoreEnvMain.getInstance().getEnvName() + (isX5WebViewLoadSucess() ? ",wk" : ",x5") + ",0;";
        setUserAgent(userAgent);
    }

    private void checkUrlChange(String url) {
        if (!TextUtils.isEmpty(newUrl) && newUrl.equals(ToolUrlMain.getInstance().toURLDecoder(url))) {

        } else {
            EWebviewUrlChange eWebviewUrlChange = new EWebviewUrlChange();
            eWebviewUrlChange.setUrl(url);
            eWebviewUrlChange.setOldUrl(newUrl);
            BusEventMain.getInstance().publish(eWebviewUrlChange);
        }
        newUrl = ToolUrlMain.getInstance().toURLDecoder(url);
    }

    /**
     * 特殊情况页面重定向
     *
     * @param url 页面链接
     */
    private void specialRedirect(String url) {
        Intent intent = null;
        //支持打电话
        if (url.startsWith(TelHost)) {
            intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
        }
        //支持发短信
        if (url.startsWith(SmsHost)) {
            intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
        }
        //支持发邮件
        if (url.startsWith(MailtoHost)) {
            intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(url));
        }
        if (intent != null) {
            if (mIEventListener != null) {
                mIEventListener.startActivity(intent);
            }
        }
    }

    /**
     * 重定向URL（登录完成、注册完成等）
     */
    private static boolean isClearHistory = false;


    public void RedirectUrl() {
        isClearHistory = true;
        isIntercept = false;
        loadUrl(redirectUrl);
        redirectUrl = "";
    }

    public void reload() {
        if (mIsReFresh && mIReloadListener != null) {
            String url = this.getUrl();
            if (url.equals(mUrl)) {
                mIReloadListener.reload();
            } else {
                super.reload();
            }
        } else {
            super.reload();
        }
    }

    public void setData(String url, boolean isReFresh, IReloadListener iReloadListener) {
        this.mUrl = url;
        this.mIsReFresh = isReFresh;
        this.mIReloadListener = iReloadListener;
    }

    /**
     * 打开浏览器：1、.pdf、.doc等文件；2、启动浏览器；3、其他网页中包含支付信息时调起支付页面
     *
     * @param url 页面url
     * @return
     */
    public void openBrowser(String url, Activity activity) {
        try {
            // 使用系统浏览器下载/打开pdf等文件
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(WzFramworkApplication.getmContext(), "未识别到浏览器", Toast.LENGTH_SHORT).show();
        }
    }

    //显示公共webview页面
    public void showCommonWebview(String url, Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra("url", url);
        intent.putExtra("title", "");
        activity.startActivity(intent);
    }

    public void cancleDownLoad(final Activity activity) {
        if (activity == null) {
            return;
        }
        (activity).getWindow().getDecorView().addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ArrayList<View> outView = new ArrayList<View>();
                (activity).getWindow().getDecorView().findViewsWithText(outView, "QQ浏览器", View.FIND_VIEWS_WITH_TEXT);
                int size = outView.size();
                if (outView != null && outView.size() > 0) {
                    outView.get(0).setVisibility(View.GONE);
                }
            }
        });
    }
}
