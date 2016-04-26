package com.intilery.android.sdk.obj;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class ViewData {
    String title;
    int pageDepth;
    String encoding;
    String language;
    boolean javaEnabled;
    String queryString;
    int screenX;
    int screenY;
}
