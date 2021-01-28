package com.iwz.WzFramwork.mod.biz.popups.model;


import com.iwz.WzFramwork.base.JBase;

/**
 * 描述：
 * 作者：小辉
 * 时间：2020/02/04
 */
public class DialogItemInfo extends JBase {
    private DialogItem dialogItem;

    public DialogItemInfo() {
        this.dialogItem = new DialogItem();
    }

    public DialogItem getDialogItem() {
        return dialogItem;
    }

    public void setDialogItem(DialogItem dialogItem) {
        this.dialogItem = dialogItem;
    }
}
