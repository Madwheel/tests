package com.iwz.WzFramwork.mod.tool.permission.requestresult;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;


import com.iwz.WzFramwork.mod.tool.permission.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类：RequestPermissionsResult
 * 处理权限申请的结果，如果未允许，提示用户,并跳转至设置APP权限页面
 * 作者： qxc
 * 日期：2018/2/8.
 */
public class RequestPermissionsResultSetApp implements IRequestPermissionsResult {
    private static RequestPermissionsResultSetApp requestPermissionsResult;
    private IGetRequestPermissionsResult iGetRequestPermissionsResult;

    public static RequestPermissionsResultSetApp getInstance() {
        if (requestPermissionsResult == null) {
            requestPermissionsResult = new RequestPermissionsResultSetApp();
        }
        return requestPermissionsResult;
    }

    @Override
    public boolean doRequestPermissionsResult(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (iGetRequestPermissionsResult != null) {
            iGetRequestPermissionsResult.callBack(activity, permissions, grantResults);
        }
        List<String> deniedPermission = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermission.add(permissions[i]);
            }
        }

        //已全部授权
        if (deniedPermission.size() == 0) {
            return true;
        }
        //引导用户去授权
        else {
            String name = PermissionUtils.getInstance().getPermissionNames(deniedPermission);
            SetPermissions.openAppDetails(activity, name);
        }
        return false;
    }
    @Override
    public void getRequestPermissionsResult(IGetRequestPermissionsResult iGetRequestPermissionsResult) {
        this.iGetRequestPermissionsResult = iGetRequestPermissionsResult;
    }
}
