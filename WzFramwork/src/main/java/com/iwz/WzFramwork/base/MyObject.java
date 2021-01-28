package com.iwz.WzFramwork.base;

import android.util.Log;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/9/211:33
 * desc   : 所有类的基类
 */
public class MyObject {
    public void born() {
    }

    public void create() {
    }

    public void active() {
    }

    public void deactive() {
    }

    public void destroy() {
    }

    public void terminate() {
    }

    public static void d(String msg) {
        Log.d("MYWZ", msg);
    }

    public static void d(String tag, String msg) {
        Log.d("MYWZ:" + tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.d("MYWZ:" + tag, msg);
    }
}
