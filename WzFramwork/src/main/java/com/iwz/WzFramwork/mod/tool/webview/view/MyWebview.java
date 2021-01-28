package com.iwz.WzFramwork.mod.tool.webview.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.MyObject;
import com.iwz.WzFramwork.mod.net.http.NetHttpMain;
import com.iwz.WzFramwork.mod.tool.permission.PermissionUtils;
import com.iwz.WzFramwork.mod.tool.permission.request.RequestPermissions;
import com.iwz.WzFramwork.mod.tool.permission.requestresult.RequestPermissionsResultSetApp;
import com.iwz.WzFramwork.mod.tool.webview.interfaces.IValueCallback;
import com.iwz.WzFramwork.mod.tool.webview.interfaces.IWebChromeClient;
import com.iwz.WzFramwork.mod.tool.webview.interfaces.IWebViewClient;
import com.iwz.WzFramwork.mod.tool.webview.interfaces.IX5WebViewHeadHook;
import com.iwz.wzframwork.R;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/10/1013:41
 * desc   :
 */

public class MyWebview extends FrameLayout {
    private View webviewRoot;
    private WebView web;
    private WebViewClient mClient;
    private File photoFile = null;

    public MyWebview(Context context) {
        super(context);
//        setBackgroundColor(85621);
        webviewRoot = LayoutInflater.from(context).inflate(R.layout.webview_main, this);
        initView();
        initSetting();
        this.setHorizontalScrollBarEnabled(false);//水平不显示
        this.setVerticalScrollBarEnabled(false); //垂直不显示

    }

    public MyWebview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setBackgroundColor(85621);
        webviewRoot = LayoutInflater.from(context).inflate(R.layout.webview_main, this);
        initView();
        initSetting();
        this.setHorizontalScrollBarEnabled(false);//水平不显示
        this.setVerticalScrollBarEnabled(false); //垂直不显示
    }

    private void initSetting() {
        mClient = new WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                MyObject.d("shouldOverrideUrlLoading", url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
            }
        };
        web.setBackgroundColor(0);
        web.getBackground().setAlpha(0);
        web.setWebViewClient(mClient);
        web.setWebChromeClient(new WebChromeClient());
        initWebViewSettings();
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void initWebViewSettings() {
        WebSettings webSetting = web.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        //webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setSavePassword(false);//隐藏h5端登录时出现“记住此密码”弹窗
        //webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }

    private void initView() {
        web = webviewRoot.findViewById(R.id.web);
    }

    private static List<IX5WebViewHeadHook> mHeadHook = new ArrayList<>();

    public static void addHeadHook(IX5WebViewHeadHook hook) {
        mHeadHook.add(hook);
    }

    public void loadUrl(String url) {
        String urlItem = NetHttpMain.getInstance().pServApi.getHost(url);
        HashMap<String, String> extraHeaders = new HashMap<>();
        for (IX5WebViewHeadHook hook : mHeadHook) {
            extraHeaders = hook.postExtraHeaders(urlItem, extraHeaders);
        }
        MyObject.d("webview", extraHeaders.toString());
        MyObject.d("webview", url);
        web.loadUrl(url, extraHeaders);
    }

    public void addJavascriptInterface(Object object, String key) {
        web.addJavascriptInterface(object, key);
    }

    public String getUserAgentString() {
        return web.getSettings().getUserAgentString();
    }

    public void setUserAgent(String userAgent) {
        web.getSettings().setUserAgent(userAgent);
    }

    public void setWebViewClient(final IWebViewClient client) {
        mClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (client.shouldOverrideUrlLoading(view, url)) {
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }

            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                client.onPageFinished(url);
                super.onPageFinished(webView, url);
            }

            @Override
            public void doUpdateVisitedHistory(WebView webView, String s, boolean b) {
                super.doUpdateVisitedHistory(webView, s, b);
                client.doUpdateVisitedHistory(s, b);
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
                client.onPageStarted(url, bitmap);
                super.onPageStarted(webView, url, bitmap);
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                client.onReceivedError(webResourceRequest.isForMainFrame());
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }
        };
        web.setWebViewClient(mClient);
    }

    public boolean shouldOverrideUrlLoading(View view, String url) {
        return mClient.shouldOverrideUrlLoading((WebView) view, url);
    }

    public void justLoadUrl(String url) {
        web.loadUrl(url);
    }

    public void clearHistory() {
        web.clearHistory();
    }

    public boolean isWebview(View view) {
        return view instanceof WebView;
    }

    public int getType(View view) {
        return ((MyWebview) view).web.getHitTestResult().getType();
    }

    public String getExtra(View view) {
        return ((MyWebview) view).web.getHitTestResult().getExtra();
    }

    public static class HitTestResult {
        public static final int UNKNOWN_TYPE = 0;
        /**
         * @deprecated
         */
        @Deprecated
        public static final int ANCHOR_TYPE = 1;
        public static final int PHONE_TYPE = 2;
        public static final int GEO_TYPE = 3;
        public static final int EMAIL_TYPE = 4;
        public static final int IMAGE_TYPE = 5;
        /**
         * @deprecated
         */
        @Deprecated
        public static final int IMAGE_ANCHOR_TYPE = 6;
        public static final int SRC_ANCHOR_TYPE = 7;
        public static final int SRC_IMAGE_ANCHOR_TYPE = 8;
        public static final int EDIT_TEXT_TYPE = 9;
    }

    public boolean isX5WebViewLoadSucess() {
        return web.getX5WebViewExtension() != null;
    }

    public String getUrl() {
        return web.getUrl();
    }

    public void reload() {
        web.reload();
    }

    public void evaluateJavascript(String js, final IValueCallback callback) {
        web.evaluateJavascript(js, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                callback.onReceiveValue(s);
            }
        });
    }

    public void setWebChromeClient(final Activity activity, final String filePath, final IWebChromeClient webChromeClient) {
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String wvTitle) {
                webChromeClient.onReceivedTitle(wvTitle);
                super.onReceivedTitle(view, wvTitle);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                webChromeClient.onProgressChanged(view.getTitle(), newProgress);
                super.onProgressChanged(view, newProgress);
            }

            //扩展浏览器上传文件
            //3.0++版本
            public void openFileChooser(final ValueCallback<Uri> uploadMsg, String acceptType) {
                if (uploadMsg == null) {
                    return;
                }
                openFileChooserImpl(activity, uploadMsg, webChromeClient);
            }

            //3.0--版本
            public void openFileChooser(final ValueCallback<Uri> uploadMsg) {
                openFileChooserImpl(activity, uploadMsg, webChromeClient);
            }

            public void openFileChooser(final ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooserImpl(activity, uploadMsg, webChromeClient);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, final ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                openFileChooseImpleForAndroid(activity, filePath, fileChooserParams.getMode(), fileChooserParams.getAcceptTypes()[0], fileChooserParams.createIntent(), filePathCallback, webChromeClient);
                return true;
            }

            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                customViewCallback.onCustomViewHidden();
            }
        });
    }

    public String getTitle() {
        return web.getTitle();
    }

    public void destroy() {
        web.destroy();
    }

    public boolean canGoBack() {
        return web.canGoBack();
    }

    public void goBack() {
        web.goBack();
    }

    public int getCurrentIndex() {
        return web.copyBackForwardList().getCurrentIndex();
    }

    public void goBackOrForward(int position) {
        web.goBackOrForward(position);
    }

    public void onPause() {
        web.onPause();
    }

    public void onResume() {
        web.onResume();
    }

    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
    public ValueCallback<Uri> mUploadMessage;

    private void openFileChooserImpl(Activity activity, ValueCallback<Uri> uploadMsg, IWebChromeClient webChromeClient) {
        mUploadMessage = uploadMsg;
        //目前只是需要相册选择
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        webChromeClient.startActivity(Intent.createChooser(intent, "选择操作"), FILECHOOSER_RESULTCODE);
    }

    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    private void openFileChooseImpleForAndroid(Activity activity, String filePath, int mode, String acceptTypes, Intent openFileIntent, ValueCallback<Uri[]> filePathCallback, IWebChromeClient webChromeClient) {
        mUploadMessageForAndroid5 = filePathCallback;
        //判断是否有相机权限
        if (!RequestPermissions.getInstance().requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionUtils.ResultCode1)) {
            if (mUploadMessageForAndroid5 != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
                mUploadMessageForAndroid5 = null;
                return;
            }
        }
        RequestPermissionsResultSetApp.getInstance().getRequestPermissionsResult(null);
        if (mode == WebChromeClient.FileChooserParams.MODE_OPEN_MULTIPLE) {//表示有多种选择方式
            //获取直接打开的intent
            Intent intent = getModeOpenIntent(openFileIntent, acceptTypes);
            Intent chooseIntent = null;
            if (acceptTypes.startsWith("video")) {
                //获取摄像机的intent
                chooseIntent = getVideoIntent();
            } else {
                //获取相机的intent
                chooseIntent = getCameraIntent(filePath);
            }
            //获取选择器的intent
            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, intent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "选择操作");
            if (chooseIntent != null) {
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{chooseIntent});
            }
            webChromeClient.startActivity(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
        } else {//(mode == MODE_OPEN)：表示直接打开
            if (acceptTypes.equals("video/*")) {
                //获取摄像机的intent
                Intent intent = getVideoIntent();
                webChromeClient.startActivity(intent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
            } else {
                Intent intent = getModeOpenIntent(openFileIntent, acceptTypes);
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, intent);
                webChromeClient.startActivity(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
            }
        }
    }

    //获取摄像机的intent
    private Intent getVideoIntent() {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //保存路径
        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/");
        //分辨率0最低，1最高
        videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        return videoIntent;
    }

    /**
     * 获取相机的intent
     *
     * @return
     */
    @NonNull
    private Intent getCameraIntent(String filePath) {
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        photoFile = new File(filePath, fileName);
        Uri uri = uriFromFile(photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return cameraIntent;
    }

    /**
     * 从文件获得URI
     *
     * @param file 文件
     * @return 文件对应的URI
     */
    public Uri uriFromFile(File file) {
        Uri fileUri;
        //7.0以上进行适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String p = WzFramworkApplication.getmContext().getPackageName() + ".FileProvider";
            fileUri = FileProvider.getUriForFile(
                    WzFramworkApplication.getmContext(),
                    p,
                    file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    /**
     * @param intent      //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);或者Intent intent = fileChooserParams.createIntent();
     * @param acceptTypes
     * @return
     */
    @NonNull
    private Intent getModeOpenIntent(Intent intent, String acceptTypes) {
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (TextUtils.isEmpty(acceptTypes)) {
            intent.setType("*/*");
        } else {
            intent.setType(acceptTypes);
        }
        return intent;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
            return;
        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                if (photoFile == null) {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
                } else {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{uriFromFile(photoFile)});
                    photoFile = null;
                }
            }
            mUploadMessageForAndroid5 = null;
            return;
        }
    }
}
