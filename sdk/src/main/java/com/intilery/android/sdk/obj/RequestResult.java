package com.intilery.android.sdk.obj;

import lombok.Builder;
import lombok.Value;

@Builder @Value
public class RequestResult {
    boolean success; // 2xx response?
    int code; // Exact HTTP code
    String response; // Server response - should only be used in debug - don't show to user
    String internalResponse; // Response from Intilery SDK / network provider - should only be used in debug - don't show to user
    String lastSuccessfulResponse; // When you are expecting a json response, use this to get response, if it was received.
}
