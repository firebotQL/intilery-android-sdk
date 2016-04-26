package com.intilery.android.sdk.io;

import com.intilery.android.sdk.Intilery;
import com.intilery.android.sdk.obj.IntileryEvent;
import com.intilery.android.sdk.obj.Properties;
import com.intilery.android.sdk.obj.PropertyUpdate;
import com.intilery.android.sdk.obj.RequestResult;

public interface IntileryIO {

    /**
     * Send an event to the Intilery Server
     * @param event The Event to send to the server
     * @param callback When the the request succeeds, fails or gives up then then this Receiver will be called.
     */
    void track(IntileryEvent event, RequestResultReceiver callback);

    /**
     * Send an event to the Intilery Server
     * @param event The Event to send to the server
     */
    void track(IntileryEvent event);

    void setVisitorProperties(PropertyUpdate update);

    void setVisitorProperties(PropertyUpdate update, RequestResultReceiver callback);

    void getVisitorProperties(PropertyReceiver callback, RequestResultReceiver receiver, String... propertiesToGet);

    void getVisitorProperties(PropertyReceiver callback, String... propertiesToGet);

    final class Paths {
        public static final String EVENT = "/event";
    }

    interface RequestResultReceiver {
        void receive(RequestResult requestResult);
    }

    interface PropertyReceiver {
        void receive(Properties properties);
    }
}
