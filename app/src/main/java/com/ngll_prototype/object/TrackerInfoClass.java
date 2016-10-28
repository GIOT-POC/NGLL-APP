package com.ngll_prototype.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chris on 2016/10/26.
 */

public class TrackerInfoClass implements Serializable{
    private static final long serialVersionUID = -2132746372434281199L;

    class Data {
        @SerializedName("SIGS")
        private int rssi;
        @SerializedName("AT_TS")
        private long AT_TS;
        @SerializedName("macAddr")
        private String mac;
        @SerializedName("frameCnt")
        private int frameCount;
        @SerializedName("snr")
        private int Snr;
        @SerializedName("GWID")
        private String GWid;
        @SerializedName("TEMP")
        private double Temperature;
        @SerializedName("BATL")
        private int BATL;
        @SerializedName("GPS_N")
        private double GPSN;
        @SerializedName("GPS_E")
        private double GPSE;
        @SerializedName("GPS_STA")
        private int GPS_STA;
        @SerializedName("MOTION")
        private int Motion;
    }

    @SerializedName("data")
    private Data data;
    @SerializedName(value = "timestamp", alternate = {"@timestamp"})
    private String timestamp;
    @SerializedName("api_key")
    private String APIKey;
    @SerializedName("raw")

    private String rawData;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public int getRssi() {
        return data.rssi;
    }

    public void setRssi(int rssi) {
        this.data.rssi = rssi;
    }

    public long getAT_TS() {
        return data.AT_TS;
    }

    public void setAT_TS(long AT_TS) {
        this.data.AT_TS = AT_TS;
    }

    public String getMac() {
        return data.mac;
    }

    public void setMac(String mac) {
        this.data.mac = mac;
    }

    public int getFrameCount() {
        return data.frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.data.frameCount = frameCount;
    }

    public int getSnr() {
        return data.Snr;
    }

    public void setSnr(int snr) {
        data.Snr = snr;
    }

    public String getGWid() {
        return data.GWid;
    }

    public void setGWid(String GWid) {
        this.data.GWid = GWid;
    }

    public double getTemperature() {
        return data.Temperature;
    }

    public void setTemperature(double temperature) {
        data.Temperature = temperature;
    }

    public int getBATL() {
        return data.BATL;
    }

    public void setBATL(int BATL) {
        this.data.BATL = BATL;
    }

    public double getGPSN() {
        return data.GPSN;
    }

    public void setGPSN(double GPSN) {
        this.data.GPSN = GPSN;
    }

    public double getGPSE() {
        return data.GPSE;
    }

    public void setGPSE(double GPSE) {
        this.data.GPSE = GPSE;
    }

    public int getGPS_STA() {
        return data.GPS_STA;
    }

    public void setGPS_STA(int GPS_STA) {
        this.data.GPS_STA = GPS_STA;
    }

    public int getMotion() {
        return data.Motion;
    }

    public void setMotion(int motion) {
        data.Motion = motion;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
}