package com.intilery.android.sdk.io;

import com.intilery.android.sdk.Intilery;
import com.intilery.android.sdk.obj.ClientData;
import com.intilery.android.sdk.obj.ContextData;
import com.intilery.android.sdk.obj.EventData;
import com.intilery.android.sdk.obj.IntileryEvent;
import com.intilery.android.sdk.obj.PropertyUpdate;
import com.intilery.android.sdk.obj.ViewData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import lombok.experimental.UtilityClass;

/**
 *
 */
@UtilityClass
public class Transformer {

    /**
     * This method transform the domain model of IntileryEvent to JSON.
     *
     * @param intileryEvent the event to transform to JSON
     * @return The IntileryEvent in it's JSON form, ready to send to the Intilery API
     */
    public static JSONObject event(IntileryEvent intileryEvent) {
        QuietJSONObject json = new QuietJSONObject();
        addUserAgent(json);
        addClientData(json, intileryEvent.getClientData());
        addContextData(json, intileryEvent.getContextData());
        addEventData(json, intileryEvent.getEventData());
        addViewData(json, intileryEvent.getViewData());
        addVisitData(json);
        return json;
    }

    /**
     * @param propertyUpdate Property to be
     * @return
     */
    public static IntileryEvent propertyUpdate(PropertyUpdate propertyUpdate) {
        return IntileryEvent.builder().eventData(propertyUpdateToEventData(propertyUpdate)).build();
    }

    /**
     *
     *
     * @param propertyUpdate
     * @return
     */
    private static EventData propertyUpdateToEventData(PropertyUpdate propertyUpdate) {
        return EventData.builder("Set Visitor Property")
                .data("Visitor", (Map) propertyUpdate.getProperties())
                .build();
    }

    /**
     * @param object
     * @param clientData
     */
    private static void addClientData(QuietJSONObject object, ClientData clientData) {
        if (clientData == null) return;
        QuietJSONObject jsonObject = new QuietJSONObject();
        if (clientData.getLocation() != null) {
            jsonObject.put("Longitude", clientData.getLocation().getLongitude());
            jsonObject.put("Latitude", clientData.getLocation().getLatitude());
        }
        object.put("Location", jsonObject);
    }

    /**
     * @param object
     * @param contextData
     */
    private static void addContextData(QuietJSONObject object, ContextData contextData) {
        if (contextData == null) return;
        QuietJSONObject jsonObject = new QuietJSONObject();
        jsonObject.put("Host", Intilery.i().getConfig().getAppName());
        jsonObject.put("Path", contextData.getPath());
        jsonObject.put("ReferrerHost", contextData.getReferrerHost());
        jsonObject.put("ReferrerPath", contextData.getReferrerPath());
        object.put("Context", jsonObject);
    }

    /**
     * @param object
     * @param eventData
     */
    private static void addEventData(QuietJSONObject object, EventData eventData) {
        if (eventData == null) return;
        object.put("EventAction", eventData.getAction());
        object.put("EventName", eventData.getName());
        __specialEventData(object, new QuietJSONObject(eventData.getData()));
    }

    /**
     * @param rootJSON
     * @param eventData
     */
    private static void __specialEventData(QuietJSONObject rootJSON, QuietJSONObject eventData) {
        if (rootJSON.optJSONObject("EventData") == null) {
            rootJSON.put("EventData", eventData);
        } else {
            add(rootJSON.optJSONObject("EventData"), eventData);
        }
    }

    /**
     * @param addTo
     * @param addThese
     */
    private static void add(JSONObject addTo, JSONObject... addThese) {
        for (JSONObject object : addThese) {
            Iterator<String> it = object.keys();
            while (it.hasNext()) {
                String key = it.next();
                try {
                    addTo.put(key, object.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param object
     * @param viewData
     */
    private static void addViewData(QuietJSONObject object, ViewData viewData) {
        if (viewData == null) return;
        QuietJSONObject view = new QuietJSONObject();
        view.put("Title", viewData.getTitle());
        view.put("PageDepth", viewData.getPageDepth());
        view.put("Language", viewData.getLanguage());
        view.put("Encoding", viewData.getEncoding());
        view.put("QueryString", viewData.getQueryString());
        view.put("ScreenX", viewData.getScreenX());
        view.put("ScreenY", viewData.getScreenY());
        object.put("View", view);
    }

    /**
     * @param object
     */
    private static void addVisitData(QuietJSONObject object) {
        QuietJSONObject jsonObject = new QuietJSONObject();
        jsonObject.put("VisitorID", Intilery.i().getUserInfo().uuid());
        //jsonObject.put("InitialVisit", Intilery.i().getUserInfo().firstuseTimestamp());
        object.put("Visit", jsonObject);
    }

    /**
     * @param object
     */
    private static void addUserAgent(QuietJSONObject object) {
        object.put("UserAgent", Intilery.i().getConfig().getUserAgent());

    }

    /**
     *
     */
    private static class QuietJSONObject extends JSONObject {

        /**
         *
         */
        protected QuietJSONObject() {
            super();
        }

        /**
         * @param map
         */
        protected QuietJSONObject(Map map) {
            super(map);
        }

        /**
         * @param name
         * @param value
         * @return
         */
        @Override
        public JSONObject put(String name, Object value) {
            try {
                return super.put(name, value);
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * @param name
         * @param value
         * @return
         */
        @Override
        public JSONObject put(String name, double value) {
            try {
                return super.put(name, value);
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * @param name
         * @param value
         * @return
         */
        @Override
        public JSONObject put(String name, long value) {
            try {
                return super.put(name, value);
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * @param name
         * @param value
         * @return
         */
        @Override
        public JSONObject put(String name, int value) {
            try {
                return super.put(name, value);
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
            return this;
        }
    }
}
