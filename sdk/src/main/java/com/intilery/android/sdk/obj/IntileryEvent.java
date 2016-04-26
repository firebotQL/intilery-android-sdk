package com.intilery.android.sdk.obj;

import lombok.Value;
import lombok.Builder;

@Value @Builder
public class IntileryEvent {
    EventData eventData;
    ViewData viewData;
    ContextData contextData;
    ClientData clientData;
}
