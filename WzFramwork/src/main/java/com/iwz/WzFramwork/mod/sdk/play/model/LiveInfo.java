package com.iwz.WzFramwork.mod.sdk.play.model;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/11/2314:54
 * desc   :
 */
public class LiveInfo {
    private String liveUrl;
    private int liveId;
    private String pageUrl;
    private String posterUrl;

    public LiveInfo() {
        this.liveUrl = "";
        this.liveId = -1;
        this.pageUrl = "";
        this.posterUrl = "";
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
