package com.lol.vitalmonitor.crashhandler;

/**
 * To get request and response body
 * but integrating in project might be complex
 * as retrofit has multiple usage ways
 */
public interface CustomInterceptorNotifier {
    void requestBody(String request);
    void responseBody(String response);
}
