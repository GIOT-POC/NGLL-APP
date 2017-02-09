package com.ngll_prototype.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Chris on 2016/10/26.
 */

public class TrackerInfoClass implements Serializable{
    private static final long serialVersionUID = -2132746372434281199L;

//    public TrackerInfoClass() {
//        super();
//        Data data = new Data();
//    }

    class Data {
        @SerializedName("SIGS")
        private double rssi;
        @SerializedName("AP_TS")
        private long AP_TS;
        @SerializedName("macAddr")
        private String mac;
        @SerializedName("frameCnt")
        private int frameCount;
        @SerializedName("snr")
        private double Snr;
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
//    @SerializedName("_source")
//    private TrackerInfoClass mTrackerInfoClass = new TrackerInfoClass();

    @SerializedName("data")
    private Data data = new Data();
    @SerializedName(value = "timestamp", alternate = {"@timestamp"})
    private String timestamp;
    @SerializedName("api_key")
    private String APIKey;
    @SerializedName("Gateway")
    private ArrayList<gatewatItem> mGatewatItemList;
    @SerializedName("raw")
    private String rawData;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public double getRssi() {
        return data.rssi;
    }

    public void setRssi(int rssi) {
        this.data.rssi = rssi;
    }

    public long getAP_TS() {
        return data.AP_TS;
    }

    public void setAP_TS(long AT_TS) {
        this.data.AP_TS = AT_TS;
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

    public double getSnr() {
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

    public ArrayList<gatewatItem> getGatewayList(){
        return mGatewatItemList;
    }

    public void setGatewatItemList(ArrayList<gatewatItem> gatewatItemList) {
        this.mGatewatItemList = gatewatItemList;
    }

    public class gatewatItem {
        @SerializedName("rssi")
        public double rssi;
        @SerializedName("snr")
        public float snr;
        @SerializedName("gatewayID")
        public String id;
    }
}
