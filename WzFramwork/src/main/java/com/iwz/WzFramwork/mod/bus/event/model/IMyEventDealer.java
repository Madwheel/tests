package com.iwz.WzFramwork.mod.bus.event.model;


import com.iwz.WzFramwork.base.interfaces.IMyEvent;

public interface IMyEventDealer {
    void onOccur(IMyEvent event);
}
