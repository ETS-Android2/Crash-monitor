package com.lol.vitalmonitor.model;

import java.util.ArrayList;
import java.util.Objects;

public class CrashPayload {

    public CrashPayload(String type, Long crashTimeStamp, String crashStackTrace, String crashCause, ArrayList<NetworkCallData> networkCallData) {
        this.type = type;
        this.crashTimeStamp = crashTimeStamp;
        this.crashStackTrace = crashStackTrace;
        this.crashCause = crashCause;
        this.networkCallData = networkCallData;
    }

    private String type;
    private Long crashTimeStamp;
    private String crashStackTrace;
    private String crashCause;
    private ArrayList<NetworkCallData> networkCallData;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCrashTimeStamp() {
        return crashTimeStamp;
    }

    public void setCrashTimeStamp(Long crashTimeStamp) {
        this.crashTimeStamp = crashTimeStamp;
    }

    public String getCrashStackTrace() {
        return crashStackTrace;
    }

    public void setCrashStackTrace(String crashStackTrace) {
        this.crashStackTrace = crashStackTrace;
    }

    public String getCrashCause() {
        return crashCause;
    }

    public void setCrashCause(String crashCause) {
        this.crashCause = crashCause;
    }

    public ArrayList<NetworkCallData> getNetworkCallData() {
        return networkCallData;
    }

    public void setNetworkCallData(ArrayList<NetworkCallData> networkCallData) {
        this.networkCallData = networkCallData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrashPayload that = (CrashPayload) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(crashTimeStamp, that.crashTimeStamp) &&
                Objects.equals(crashStackTrace, that.crashStackTrace) &&
                Objects.equals(crashCause, that.crashCause) &&
                Objects.equals(networkCallData, that.networkCallData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, crashTimeStamp, crashStackTrace, crashCause, networkCallData);
    }

    @Override
    public String toString() {
        return "CrashPayload{" +
                "type='" + type + '\'' +
                ", crashTimeStamp='" + crashTimeStamp + '\'' +
                ", crashStackTrace='" + crashStackTrace + '\'' +
                ", crashCause='" + crashCause + '\'' +
                ", networkCallData=" + networkCallData +
                '}';
    }
}
