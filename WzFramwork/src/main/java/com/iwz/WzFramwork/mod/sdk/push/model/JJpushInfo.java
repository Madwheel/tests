package com.iwz.WzFramwork.mod.sdk.push.model;


import com.iwz.WzFramwork.base.JBase;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/03
 */
public class JJpushInfo extends JBase {
    private String url;
    private String type;
    private String title;
    private String summary;
    private String nid;
    private String win;
    private String transDate;

    public JJpushInfo() {
        this.url = "";
        this.type = "";
        this.title = "";
        this.summary = "";
        this.nid = "";
        this.win = "";
        this.transDate = "";
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary == null ? "" : summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNid() {
        return nid == null ? "" : nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getWin() {
        return win == null ? "" : win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getTransDate() {
        return transDate == null ? "" : transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }
}
