package com.iwz.WzFramwork.mod.sdk.push.interfaces;

import android.content.Context;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/10/1316:03
 * desc   :
 */
public interface ISdkPushListener {

    void launchByMiId(String miId);

    void pushClickToUri(Context context, String uri);
}
