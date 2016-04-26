package com.intilery.android.sdk.obj;

import java.util.Map;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class PropertyUpdate {
    @Singular
    Map<String, String> properties;
}
