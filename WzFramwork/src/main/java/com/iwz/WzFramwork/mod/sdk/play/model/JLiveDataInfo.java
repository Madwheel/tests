package com.iwz.WzFramwork.mod.sdk.play.model;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/11/1714:59
 * desc   :
 */
public class JLiveDataInfo {
    private LiveInfo current;
    private List<LiveInfo> list;

    public JLiveDataInfo() {
        this.current = new LiveInfo();
        this.list = new ArrayList<>();
    }

    public LiveInfo getCurrent() {
        return current;
    }

    public void setCurrent(LiveInfo current) {
        this.current = current;
    }

    public List<LiveInfo> getList() {
        return list;
    }

    public void setList(List<LiveInfo> list) {
        this.list = list;
    }
}
