package com.iwz.WzFramwork.mod.tool.time;

import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;
import com.iwz.WzFramwork.mod.tool.time.serv.ToolTimeServApi;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/22
 */
public class ToolTimeMain extends ModMain {
    private ToolTimeServApi mServ;

    @Override
    public String getModName() {
        return "ToolTimeMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_TOOL;
    }

    private static ToolTimeMain mToolTimeMain;

    public static ToolTimeMain getInstance() {
        if (mToolTimeMain == null) {
            synchronized (ToolTimeMain.class) {
                if (mToolTimeMain == null) {
                    mToolTimeMain = new ToolTimeMain();
                }
            }
        }
        return mToolTimeMain;
    }

    @Override
    public void born() {
        super.born();
        mServ = ToolTimeServApi.getInstance(this);
    }

    public String getNowDateShort() {
        return mServ.getNowDateShort();
    }

    //计算日期之间相隔几天：
    public long compareDataToNow(String date) {
        return mServ.compareDataToNow(date);
    }
    //计算日期之间相隔几天：
    public String getYYMMDDTime(String date) {
        return mServ.getYYMMDDTime(date);
    }

    public int getTimeCompareSize(String startTime, String endTime) {
        return mServ.getTimeCompareSize(startTime, endTime);
    }
    public String getCuttentDay(Long time) {
        return mServ.getCuttentDay(time);
    }
    public String getMMDDHHmmTime(String time) {
        return mServ.getMMDDHHmmTime(time);
    }

    public int getSeconds(String time) {
        return mServ.getDdSeconds(time);
    }
    public String dateToStr(String time) {
        return mServ.dateToStr(time);
    }

    public String getUserDate(String format) {
        return mServ.getUserDate(format);
    }

    public int getSsSeconds(String time) {
        return mServ.getSsSeconds(time);
    }

    public long getTimeRange(String startTime, String endTime) {
        return mServ.getTimeRange(startTime, endTime);
    }
    public String getMDHmTime(long time) {
        return mServ.getMDHmTime(time);
    }
    public String getTime() {
        return mServ.getTime();
    }
}
