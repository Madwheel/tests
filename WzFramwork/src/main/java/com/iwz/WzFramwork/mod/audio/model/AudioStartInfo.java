package com.iwz.WzFramwork.mod.audio.model;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/26
 */
public class AudioStartInfo {
    private String src;//音频文件本地地址
    private int palyType;//音频播放类型；0从头开始，1暂停后开始
    private String place;//语音的地方 main 是直播 talk 互动 secret是私聊
    private String msgId;//消息ID
    private String format;//音频文件格式

    public AudioStartInfo() {
        this.src = "";
        this.palyType = 0;
        this.place = "";
        this.msgId = "";
        this.format = "";
    }

    public String getSrc() {
        return src == null ? "" : src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getPalyType() {
        return palyType;
    }

    public void setPalyType(int palyType) {
        this.palyType = palyType;
    }

    public String getPlace() {
        return place == null ? "" : place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMsgId() {
        return msgId == null ? "" : msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getFormat() {
        return format == null ? "" : format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
