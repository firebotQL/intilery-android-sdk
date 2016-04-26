package com.intilery.android.sdk.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Value;

@Value
public class EventData {
    String action;
    String name;
    Map<String, Map<String, Object>> data;

    EventData(String action, String name, Map<String, Map<String, Object>> data) {
        this.action = action;
        this.name = name;
        this.data = data;
    }

    public static EventDataBuilder builder(String action, String name) {
        return new EventDataBuilder(action, name);
    }

    public static EventDataBuilder builder(String action) {
        return new EventDataBuilder(action, action);
    }

    public static class EventDataBuilder { //From Lombok's @Builder
        private final String action;
        private final String name;
        private ArrayList<String> data$key;
        private ArrayList<Map<String, Object>> data$value;

        EventDataBuilder(String action, String name) {
            this.action = action;
            this.name = name;
        }

        public EventDataBuilder data(String dataKey, Map<String, Object> dataValue) {
            if (this.data$key == null) {
                this.data$key = new ArrayList<>();
                this.data$value = new ArrayList<>();
            }
            this.data$key.add(dataKey);
            this.data$value.add(dataValue);
            return this;
        }

        public EventDataBuilder data(Map<String, Map<String, Object>> data) {
            if (this.data$key == null) {
                this.data$key = new ArrayList<>();
                this.data$value = new ArrayList<>();
            }
            for (Map.Entry<String, Map<String, Object>> $lombokEntry : data.entrySet()) {
                this.data$key.add($lombokEntry.getKey());
                this.data$value.add($lombokEntry.getValue());
            }
            return this;
        }

        public EventData build() {
            return new EventData(action, name, new HashMap<String, Map<String, Object>>() {{
                int _count = null == data$key ? 0 : data$key.size();
                for (int _i = 0; _i < _count; _i++) {
                    put(data$key.get(_i), data$value.get(_i));
                }
            }});
        }

        public String toString() {
            return "com.intilery.android.sdk.obj.EventData.EventDataBuilder(action=" + this.action + ", name=" + this.name + ", data$key=" + this.data$key + ", data$value=" + this.data$value + ")";
        }
    }
}
