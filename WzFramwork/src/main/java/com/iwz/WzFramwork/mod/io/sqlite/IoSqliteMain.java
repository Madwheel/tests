package com.iwz.WzFramwork.mod.io.sqlite;


import com.iwz.WzFramwork.base.main.ModMain;
import com.iwz.WzFramwork.mod.ModType;

public class IoSqliteMain extends ModMain {
    public String getModName() {
        return "IoSqliteMain";
    }

    @Override
    public ModType getModType() {
        return ModType.MODE_IO;
    }

    private static IoSqliteMain instance = new IoSqliteMain();

    private IoSqliteMain() {
    }

    public static IoSqliteMain getInstance() {
        return instance;
    }

    public void create() {
        super.create();
    }
}
