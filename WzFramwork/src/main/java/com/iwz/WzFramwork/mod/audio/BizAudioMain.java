package com.iwz.WzFramwork.mod.audio;

import com.iwz.WzFramwork.mod.audio.conf.BizAudioConf;
import com.iwz.WzFramwork.mod.audio.control.BizAudioControlApp;
import com.iwz.WzFramwork.mod.audio.serv.BizAudioPlayServ;
import com.iwz.WzFramwork.mod.audio.serv.BizAudioRecordServ;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/09/26
 */
public class BizAudioMain extends ModMain {
    private static BizAudioMain mToolAudioMain;

    public static BizAudioMain getInstance() {
        if (mToolAudioMain == null) {
            synchronized (BizAudioMain.class) {
                if (mToolAudioMain == null) {
                    mToolAudioMain = new BizAudioMain();
                }
            }
        }
        return mToolAudioMain;
    }

    @Override
    public String getModName() {
        return "BizAudioMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_BIZ;
    }

    public BizAudioConf mConf;
    public BizAudioRecordServ mRecordServ;
    public BizAudioPlayServ mPlayServ;
    public BizAudioControlApp mControl;

    @Override
    public void born() {
        super.born();
        mConf = BizAudioConf.getInstance(this);
        mRecordServ = BizAudioRecordServ.getInstance(this);
        mPlayServ = BizAudioPlayServ.getInstance(this);
        mControl = BizAudioControlApp.getInstance(this);
        mControl.born();
    }
    public BizAudioControlApp getpControl(){
        return mControl;
    }
}
