package com.iwz.WzFramwork.base.interfaces;


import com.iwz.WzFramwork.base.CommonRes;

import java.io.IOException;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/9/213:22
 * desc   :
 */
public interface IResCallback<T> {
    void onFailure(IOException e);

    void onFinish(CommonRes<T> res);
}
