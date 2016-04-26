package com.intilery.android.sdk;

import android.content.Context;

import lombok.Value;
import lombok.Builder;

@Value @Builder
public class IntileryConfig {
    String url;
    String intileryToken;
    String gcmToken;
    String appName;
    String userAgent;
    Context rootContext;
}
