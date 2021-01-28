package com.iwz.WzFramwork.mod;
//CMS详细介绍、垃圾回收算法、CMS、LMK：low memory kill

import android.content.Context;
import android.os.Handler;

import com.iwz.WzFramwork.WzFramworkApplication;
import com.iwz.WzFramwork.base.EAppPhase;
import com.iwz.WzFramwork.base.interfaces.IAppStatus;
import com.iwz.WzFramwork.base.interfaces.IImageRecognit;
import com.iwz.WzFramwork.base.interfaces.IShare;
import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.bus.event.BusEventMain;
import com.iwz.WzFramwork.base.interfaces.IRouter;
import com.iwz.WzFramwork.mod.core.config.CoreConfigMain;
import com.iwz.WzFramwork.mod.core.env.CoreEnvMain;
import com.iwz.WzFramwork.mod.core.signal.CoreSignalMain;
import com.iwz.WzFramwork.mod.io.file.IoFileMain;
import com.iwz.WzFramwork.mod.io.kvdb.IIoKvdb;
import com.iwz.WzFramwork.mod.io.kvdb.IoKvdbMain;
import com.iwz.WzFramwork.mod.io.sqlite.IoSqliteMain;
import com.iwz.WzFramwork.mod.net.core.NetCoreMain;
import com.iwz.WzFramwork.mod.net.http.NetHttpMain;
import com.iwz.WzFramwork.mod.net.http.interfaces.INetHttp;
import com.iwz.WzFramwork.mod.net.mqtt.NetMqttMain;
import com.iwz.WzFramwork.mod.sdk.push.SdkPushMain;
import com.iwz.WzFramwork.mod.tool.common.file.ToolFileMain;
import com.iwz.WzFramwork.mod.tool.common.img.ToolImgMain;
import com.iwz.WzFramwork.mod.tool.common.system.ToolSystemMain;
import com.iwz.WzFramwork.mod.tool.format.ToolFormatMain;
import com.iwz.WzFramwork.mod.tool.time.ToolTimeMain;
import com.iwz.WzFramwork.mod.tool.url.ToolUrlMain;
import com.iwz.WzFramwork.mod.tool.webview.MyWebviewMain;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMgr {
    public List<List<ModMain>> getAllMods() {
        if (allMods == null) {
            allMods = new ArrayList<>();
            for (int i = 0; i < ModType.values().length; i++) {
                allMods.add(new ArrayList<ModMain>());
            }
        }
        //core
        addMod(CoreEnvMain.getInstance());
        addMod(CoreConfigMain.getInstance());
        addMod(CoreSignalMain.getInstance());
        //bus
        addMod(BusEventMain.getInstance());
        //tool
        addMod(ToolSystemMain.getInstance());
        addMod(ToolFileMain.getInstance());
        addMod(ToolUrlMain.getInstance());
        addMod(ToolFormatMain.getInstance());
        addMod(ToolImgMain.getInstance());
        addMod(ToolTimeMain.getInstance());
        addMod(MyWebviewMain.getInstance());
        //io
        addMod(IoFileMain.getInstance());
        addMod(IoSqliteMain.getInstance());
        addMod(IoKvdbMain.getInstance());
        //net
        addMod(NetCoreMain.getInstance());
        addMod(NetHttpMain.getInstance());
        addMod(NetMqttMain.getInstance());
        //sdk
        addMod(SdkPushMain.getInstance());
        //biz
        initMoreMods();
        return allMods;
    }

    private List<List<ModMain>> allMods;
    public MyHandlerThread mThread;

    protected abstract void initMoreMods();

    protected void addMod(ModMain modMain) {
        ModType modType = modMain.getModType();
        List<ModMain> modMains = allMods.get(modType.ordinal());
        modMains.add(modMain);
    }

    private void getMods(List<ModMain> mods) {
        if (mods != null && mods.size() > 0) {
            for (int i = 0; i < mods.size(); i++) {
                addMod(mods.get(i));
            }
        }
    }

    public void born() {
        System.out.println("born ");
        for (List<ModMain> mods : allMods) {
            for (ModMain modMain : mods) {
                modMain.born();
            }
        }
    }

    public void create() {
        System.out.println("create ");
        for (List<ModMain> mods : allMods) {
            for (ModMain modMain : mods) {
                modMain.create();
            }
        }
    }

    public void active() {
        for (List<ModMain> mods : allMods) {
            for (ModMain modMain : mods) {
                modMain.active();
            }
        }
    }

    public void phase(EAppPhase phase) {
        for (List<ModMain> mods : allMods) {
            for (ModMain modMain : mods) {
                modMain.phase(phase);
            }
        }
    }

    public void deactive() {
        for (List<ModMain> mods : allMods) {
            for (int i = mods.size() - 1; i >= 0; i--) {
                mods.get(i).deactive();
            }
        }
    }

    public void destroy() {
        for (List<ModMain> mods : allMods) {
            for (int i = mods.size() - 1; i >= 0; i--) {
                mods.get(i).destroy();
            }
        }
    }

    public void terminate() {
        for (List<ModMain> mods : allMods) {
            for (int i = mods.size() - 1; i >= 0; i--) {
                mods.get(i).terminate();
            }
        }
    }

    public void loop(Context context, String mode, String channel) {
        mThread = new MyHandlerThread("BaseMgr");
        mThread.start();
        WzFramworkApplication.setChannel(context, mode, channel, mThread);
    }

    public void setAgency(IRouter router, IImageRecognit imageRecognit, IAppStatus appStatus, IShare share, INetHttp netHttp, IIoKvdb ioKvdb) {
        WzFramworkApplication.setAgency(router, imageRecognit, appStatus, share,netHttp,ioKvdb);
    }

    public Handler getHandler() {
        return mThread.getHandler();
    }
}
