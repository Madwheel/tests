package com.iwz.WzFramwork.mod.biz.popups.model;

import com.iwz.WzFramwork.base.api.ModelApi;
import com.iwz.WzFramwork.mod.biz.popups.BizPopupsMain;

public class BizPopupsModel extends ModelApi {
    private static BizPopupsModel mInstance;
    private BizPopupsMain mMain;
    private DialogItemInfo mInfo;

    protected BizPopupsModel(BizPopupsMain main) {
        super(main);
        mMain = main;
    }

    public static BizPopupsModel getInstance(BizPopupsMain main) {
        if (mInstance == null) {
            synchronized (BizPopupsModel.class) {
                if (mInstance == null) {
                    mInstance = new BizPopupsModel(main);
                }
            }
        }
        return mInstance;
    }

    @Override
    public void born() {
        super.born();
        mInfo = new DialogItemInfo();
    }

    public void setDialogItemInfo(DialogItemInfo info) {
        mInfo = info;
    }

    public DialogItemInfo getDialogItemInfo() {
        return mInfo;
    }
}
