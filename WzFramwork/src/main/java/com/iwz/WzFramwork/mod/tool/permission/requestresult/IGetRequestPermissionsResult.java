package com.iwz.WzFramwork.mod.tool.permission.requestresult;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * 描述：
 * 作者：小辉
 * 时间：2018/09/21
 */

public interface IGetRequestPermissionsResult {
    void  callBack(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults);
}
