package com.intilery.android.sdk;

import android.os.Bundle;

import com.intilery.android.sdk.io.HTTPManager;
import com.intilery.android.sdk.io.IntileryIO;
import com.intilery.android.sdk.obj.PropertyUpdate;

import java.util.HashMap;

import lombok.Getter;

public class Intilery {

    private static Intilery instance;

    public static Intilery i() {
        return instance;
    }

    @Getter
    private final IntileryConfig config;
    @Getter
    private final IntileryIO io;

    @Getter
    private final IntileryUserInfo userInfo;

    public Intilery(IntileryConfig config) {
        instance = this;
        this.config = config;
        if (getConfig().getIntileryToken() == null) throw new AssertionError("No token given");
        if (getConfig().getAppName() == null) throw new AssertionError("No app name given");
        if (getConfig().getRootContext() == null) throw new AssertionError("No context given");
        if (getConfig().getUrl() == null) throw new AssertionError("No URL given");
        if (getConfig().getUrl().charAt(getConfig().getUrl().length() - 1) == '/') throw new AssertionError("URL ends with a slash!");
        io = new HTTPManager();
        userInfo = new IntileryUserInfo(getConfig().getRootContext());
    }

    public void gcmHook(String thing, Bundle bundle) {
        throw new UnsupportedOperationException("GCM Hook isn't yet implemented, track your own event for now!");
    }

}
