package com.iwz.WzFramwork.mod.biz.adv.serv;


import com.iwz.WzFramwork.base.CommonRes;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1515:35
 * desc   :
 */
public interface IResParseCallback<T> {
    void onFinish(CommonRes<T> res);
}
