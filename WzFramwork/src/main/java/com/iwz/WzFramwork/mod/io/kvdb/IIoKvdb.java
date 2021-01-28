package com.iwz.WzFramwork.mod.io.kvdb;

import com.snappydb.SnappydbException;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/9/2917:33
 * desc   :
 */
public interface IIoKvdb {
    void put(String key, Object object) throws Exception;

    <T> T getObject(String key, Class<T> className) throws Exception;
}
