package com.intilery.android.sdk.obj;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class ContextData {
    String path;
    String referrerHost;
    String referrerPath;
}
