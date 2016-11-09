package com.ngll_prototype;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.ngll_prototype.object.TrackerInfoClass;

/**
 * Created by Chris on 2016/10/25.
 */

public class IndoorLocation {
    private final static String TAG = "IndoorLocation";
    TrackerInfoClass trackerInfoClass;

    /*
    * getPositionNum para Node JsonObject data
    *  return int[2], int[0] is floor number, int[1] is Anchor number.
    */
    public int[] getPositionNum(JsonObject NodeJson) {
        int POSNum[] = new int[2];
        Log.d(TAG, "NodeData:\n" +NodeJson);
//        Log.d(TAG, "" + NodeJson.get("hits").getAsJsonObject().get("hits").getAsJsonArray().get(0).getAsJsonObject().get("_source"));
//        Log.d(TAG, "" + NodeJson.get("hits").getAsJsonObject().get("hits").getAsJsonArray().get(0).getAsJsonObject().get("_source").getAsJsonObject().get("data"));

        //work around
//        for(int i =0; i< 50; i ++){
//            trackerInfoClass = ParseTrackerGSON(NodeJson.get("hits").getAsJsonObject().get("hits").getAsJsonArray().get(i).getAsJsonObject().get("_source").toString());
//            if (trackerInfoClass.getGPSN() > 0) {
//                Log.d(TAG, "GPSN:\t" + trackerInfoClass.getGPSN() + "\tTime:\t:" + trackerInfoClass.getTimestamp());
//                break;
//            }
//        }

        trackerInfoClass = ParseTrackerGSON(NodeJson.get("hits").getAsJsonObject().get("hits").getAsJsonArray().get(0).getAsJsonObject().get("_source").toString());
        if (trackerInfoClass == null)
            return null;

//        Log.d(TAG, "Long.parseLong(\"94FE26A8\", 16):\t" + Long.parseLong("94FE26A8", 16));
//        Log.d(TAG, "trackerInfoClass.getRawData():\t " + trackerInfoClass.getRawData());
        if (getStatus(trackerInfoClass.getRawData(), 6) == 1) {
            Log.d(TAG, "Indoor");

//            Log.d(TAG, "GPS N:\t" + trackerInfoClass.getGPSN());
//            Log.d(TAG, "GPS E:\t" + trackerInfoClass.getGPSE());
        if (trackerInfoClass.getGPSE() <= 0 || trackerInfoClass.getGPSN() <=0 ){
            return null;
        }
            POSNum[0] = getLast3num(trackerInfoClass.getGPSN()); // get Floor;
            POSNum[1] = getLast3num(trackerInfoClass.getGPSE()); // get Anchor Number

            Log.d(TAG, "get Floor:\t" + POSNum[0]);
            Log.d(TAG, "get Anchor Number:\t" + POSNum[1]);
        }
        return POSNum;
    }


    public TrackerInfoClass ParseTrackerGSON(String s) {
        Gson gson = new Gson();
        TrackerInfoClass result = gson.fromJson(s, TrackerInfoClass.class);
        return result;
    }

    public int getLast3num(double inNum) {
        String StrinNum = String.valueOf((long) (inNum * 1000000));
//        Log.d(TAG, "StrGPSN:\t" + StrGPSN);
        if (inNum <= 0)
            return 0;

        int num = Integer.valueOf(StrinNum.substring(7));
        return num;
    }

    //getNodeST( 'Hex String', integer)
    //raw: hex string, addr bit addr between 0 to 7, return integer 1 or 0
    public int getStatus(String raw, int bit) {
        String Status = raw.substring(0, 2);
        int HexST = Integer.parseInt(Status, 16);

        if ((HexST & (int) Math.pow(2, bit)) > 0)
            return 1;

        return 0;
    }


}
