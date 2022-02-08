package com.lol.vitalmonitor.model;

import java.util.Objects;

public class NetworkCallData {

    public NetworkCallData(Long lastAPIHitTimeStamp, String lastAPIHitRequest, String lastAPIHitResponse) {
        this.lastAPIHitTimeStamp = lastAPIHitTimeStamp;
        this.lastAPIHitRequest = lastAPIHitRequest;
        this.lastAPIHitResponse = lastAPIHitResponse;
    }

    private Long lastAPIHitTimeStamp;
    private String lastAPIHitRequest;
    private String lastAPIHitResponse;

    public Long getLastAPIHitTimeStamp() {
        return lastAPIHitTimeStamp;
    }

    public void setLastAPIHitTimeStamp(Long lastAPIHitTimeStamp) {
        this.lastAPIHitTimeStamp = lastAPIHitTimeStamp;
    }

    public String getLastAPIHitRequest() {
        return lastAPIHitRequest;
    }

    public void setLastAPIHitRequest(String lastAPIHitRequest) {
        this.lastAPIHitRequest = lastAPIHitRequest;
    }

    public String getLastAPIHitResponse() {
        return lastAPIHitResponse;
    }

    public void setLastAPIHitResponse(String lastAPIHitResponse) {
        this.lastAPIHitResponse = lastAPIHitResponse;
    }

    @Override
    public String toString() {
        return "NetworkCallData{" +
                "lastAPIHitTimeStamp=" + lastAPIHitTimeStamp +
                ", lastAPIHitRequest='" + lastAPIHitRequest + '\'' +
                ", lastAPIHitResponse='" + lastAPIHitResponse + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetworkCallData that = (NetworkCallData) o;
        return Objects.equals(lastAPIHitTimeStamp, that.lastAPIHitTimeStamp) &&
                Objects.equals(lastAPIHitRequest, that.lastAPIHitRequest) &&
                Objects.equals(lastAPIHitResponse, that.lastAPIHitResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastAPIHitTimeStamp, lastAPIHitRequest, lastAPIHitResponse);
    }
}
