package com.intilery.android.sdk.io;


import android.text.TextUtils;

import com.intilery.android.sdk.Intilery;
import com.intilery.android.sdk.obj.IntileryEvent;
import com.intilery.android.sdk.obj.Properties;
import com.intilery.android.sdk.obj.PropertyUpdate;
import com.intilery.android.sdk.obj.RequestResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HTTPManager implements IntileryIO {

    @Override
    public void track(IntileryEvent event, RequestResultReceiver callback) {
        new IntileryRequest(Intilery.i().getConfig().getUrl()+Paths.EVENT, Transformer.event(event), callback);
    }

    @Override
    public void track(IntileryEvent event) {
        track(event, new RequestResultReceiver(){ @Override public void receive(RequestResult requestResult) {} });
    }

    @Override
    public void setVisitorProperties(PropertyUpdate update) {
        setVisitorProperties(update, new RequestResultReceiver(){@Override public void receive(RequestResult requestResult){}});
    }

    @Override
    public void setVisitorProperties(PropertyUpdate update, RequestResultReceiver callback) {
        track(Transformer.propertyUpdate(update), callback);
    }

    @Override
    public void getVisitorProperties(final PropertyReceiver callback, final RequestResultReceiver receiver, String... propertiesToGet) {
        String propertyString = TextUtils.join(",", propertiesToGet);
        try {
            new IntileryRequest("GET", Intilery.i().getConfig().getUrl() + "/visitor/" + Intilery.i().getUserInfo().uuid() + "/properties?properties="+ URLEncoder.encode(propertyString, "utf-8"), null, new RequestResultReceiver() {
                @Override
                public void receive(RequestResult requestResult) {
                    try {
                        JSONObject obj = new JSONObject(requestResult.getLastSuccessfulResponse());
                        Map<String, String> properties = new HashMap<>();
                        String key;
                        Iterator<String> it = obj.keys();
                        while(it.hasNext()){
                            key = it.next();
                            properties.put(key, obj.getJSONObject(key).getString("value"));
                        }
                        try {
                            callback.receive(new Properties(properties));
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    receiver.receive(requestResult);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getVisitorProperties(PropertyReceiver callback, String... propertiesToGet) {
        getVisitorProperties(callback, new RequestResultReceiver(){@Override public void receive(RequestResult requestResult){}}, propertiesToGet);
    }

}
