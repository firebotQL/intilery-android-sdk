package com.intilery.android.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.intilery.android.sdk.obj.ContextData;
import com.intilery.android.sdk.obj.EventData;
import com.intilery.android.sdk.obj.IntileryEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IntileryUserInfo {

    private final String ID_SAVE_NAME = "userID";

    private String currentID;
    private final SharedPreferences preferences;

    protected IntileryUserInfo(Context context) {
        preferences = context.getSharedPreferences("_IntileryInternal", 0);
        currentID = preferences.getString(ID_SAVE_NAME, "NONE");
        if(currentID.equals("NONE")) reset();
    }

    public String uuid () {
        return currentID;
    }

    public void reset() {
        currentID = UUID.randomUUID().toString();
        preferences.edit().putString(ID_SAVE_NAME, currentID).apply();
        final Map<String, Object> appCode = new HashMap<>();
        appCode.put("appCode", Intilery.i().getConfig().getAppName());
        appCode.put("deviceID", Intilery.i().getConfig().getGcmToken());
        appCode.put("deviceType", "android");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                while(Intilery.i().getUserInfo() == null) {}
                Intilery.i().getIo().track(IntileryEvent.builder().eventData(
                        EventData.builder("Set Device Id").data("Register App", appCode).build()
                ).contextData(
                        ContextData.builder().path("/_intilery/userinfo_reset").build()
                )
                        .build()
                );
                return null;
            }
        }.execute();
    }

}
