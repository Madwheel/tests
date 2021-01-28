package com.iwz.WzFramwork.mod.net.core.model;

import java.io.IOException;

public interface INetCallback {
    void onFailure(IOException e);
    void onResponse(NetRes response) ;
}
