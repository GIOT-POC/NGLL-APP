package com.ngll_prototype.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.ngll_prototype.ShowTrackerView;
import com.ngll_prototype.model.NodeDataFetch;
import com.ngll_prototype.model.NodeDataFetchImpl;
import com.ngll_prototype.object.IDUlist;
import com.ngll_prototype.object.IMGCoordinate;
import com.ngll_prototype.object.SourceClass;
import com.ngll_prototype.object.TrackerInfoClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayPresenterImpl implements DisplayPresenter, NodeDataFetch.OnNodeDataFetchListener {
    private final static String TAG = "DisplayPresenterImpl";

    private ShowTrackerView mMainView;
    private NodeDataFetch mNodeDataFetch;
    private ArrayList<IMGCoordinate> mIMGCoordinatesList = new ArrayList<>();
    private static boolean mGetExE;
    private static IDUlist mIDUlsist;
    private TrackerInfoClass mPreviousTrackObj = null,
            mCurrentTrackObj = null,
            mTempTrackObj = null;

    public DisplayPresenterImpl(ShowTrackerView mainView) {
        this.mMainView = mainView;
        this.mNodeDataFetch = new NodeDataFetchImpl();
        mIDUlsist = new IDUlist();
    }

    @Override
    public void onSucess(ArrayList<SourceClass> SourceList) {
        //try GPSECountImpl
        TrackerInfoClass TrackObj = GPSECountImpl(SourceList);
//        TrackerInfoClass TrackObj = SourceList.get(0).getTrackerInfoClass();

        int floor = getLast3num(TrackObj.getGPSN());
        int anchor = getLast3num(TrackObj.getGPSE());
        mCurrentTrackObj = TrackObj;
        Log.d(TAG, "onSucess, getStatus:\t" + getStatus(TrackObj.getRawData(), 6));
        Log.d(TAG, "anchor:\t" + anchor + "\nmIMGCoordinatesList.size():\t" + mIMGCoordinatesList.size());
        if (getStatus(TrackObj.getRawData(), 6) == 1) {
            if (anchor + 1 <= mIMGCoordinatesList.size())
                if (mMainView != null) {

                    //draw node position in the view
                    mMainView.drawIndoorMap(
                            mIMGCoordinatesList.get(anchor).getX(),
                            mIMGCoordinatesList.get(anchor).getY());
                }
        }


    }

    @Override
    public void onError() {
        Log.d(TAG, "onError: no result return");
    }

    @Override
    public void getNodePOS(final Context context, final String mac) {
        mGetExE = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mGetExE) {
                    try {
                        mNodeDataFetch.getNodeData(context, mac, DisplayPresenterImpl.this); // get node date
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @Override
    public void stopGetNodePOS() {
        mGetExE = false;
    }

    @Override
    public void setIMGCoordinate(Context context, ImageView imageView) {
        //generate map coordinate
        IMGCoordinate imgCoordinateTmp;
        Drawable drawable = imageView.getDrawable();

        final int origH = drawable.getIntrinsicHeight();
        final int origW = drawable.getIntrinsicWidth();

        int toScaleH = 5;
//        int toScaleH = 7;
        int toScaleW = 6;
//        int toScaleW = 7;


        int spacingH = origH / toScaleH;
        int spacingW = origW / toScaleW;
//        int spacingW = (origW+5) / toScaleW; // for TICC extend width

        int x, y = 0;
        int count = 0;
        Log.d(TAG, "spacingH:\t" + spacingH + "\tspacingW:\t" + spacingW);
        int ticc201bH = 5;
        int ticc201bW = 5;
        for (int i = 1; i < toScaleH; i++) {

//            if (i+1 == toScaleH || i+2 == toScaleH) {//extend last rows distance
//                Log.d(TAG, "i\t" + i);
////                spacingW = spacingW + ticc201bW;
//                spacingH  = ticc201bH + spacingH;
//            }
            x = 0;
            y = y + spacingH;
            Log.d(TAG, "y:\t" + y);
            for (int j = 1; j < toScaleW; j++) {
                try {
                    x = x + spacingW;
                    imgCoordinateTmp = new IMGCoordinate(count, x, y);
                    mIMGCoordinatesList.add(imgCoordinateTmp);
                    count = count + 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
        Log.d(TAG, "IMGCoorlist.length:\t" + mIMGCoordinatesList.size());
//        for (int i = 0; i < mIMGCoordinatesList.size(); i++) {
//
//            Log.d(TAG, "I:\t" + i + "\tcount:\t" + mIMGCoordinatesList.get(i).getNumber() + "\tX:\t" + mIMGCoordinatesList.get(i).getX()
//                    + "\tY:\t" + mIMGCoordinatesList.get(i).getY());
//        }

    }

    @Override
    public String getPreviousGWiNFO() {
        if (mPreviousTrackObj == null) { // first time execute previous is empty
            mPreviousTrackObj = new TrackerInfoClass();
            mTempTrackObj = new TrackerInfoClass();
            mTempTrackObj.setAP_TS(0);
//            Log.d(TAG, "Previous GWsINFO:\t" + mPreviousGWinfo);
            Log.d(TAG, "mPreviousTrackObj is null");
            return "";
        } else {
            String list = "";
            if (mTempTrackObj != null) {

                if (mTempTrackObj.getAP_TS() == mCurrentTrackObj.getAP_TS()) { //current data was not change, event get from server.
                    Log.d(TAG, "Temp time equal Current time");
                    return reFormatGwList(mPreviousTrackObj.getGatewayList());
                } else {
                    Log.d(TAG, "Temp time unequal Current time, set mPreviousTrackObj from temp");
                    mPreviousTrackObj = mTempTrackObj; // current data was update, then transfer temp to previous
                    list = reFormatGwList(mPreviousTrackObj.getGatewayList());
                }
            }
            if (mTempTrackObj.getAP_TS() < mCurrentTrackObj.getAP_TS()) { // new current data update to temp.
                Log.d(TAG, "temp time small than current time");
                mTempTrackObj = mCurrentTrackObj;
            }
            return list;
        }


    }

    @Override
    public String getCurrentGWiNFO() {
        if(mCurrentTrackObj == null){
            return "";
        }
        return reFormatGwList(mCurrentTrackObj.getGatewayList());
    }

    @Override
    public void showAllDot(Context context) {
        for (int i =0; i < mIMGCoordinatesList.size(); i++){
            mMainView.showAllDot(
                    mIMGCoordinatesList.get(i).getX(),
                    mIMGCoordinatesList.get(i).getY());
        }
    }

    @Override
    public void cleanAllDot(Context context) {
        mMainView.cleanDraw();
    }

    @Override
    public void onDestroy() {
        mMainView = null;
    }

    public int getLast3num(double inNum) {
        String StrinNum = String.valueOf((long) (inNum * 1000000));
//        Log.d(TAG, "StrGPSN:\t" + StrinNum);
        if (inNum <= 0)
            return -1;

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

    public String reFormatGwList(ArrayList<TrackerInfoClass.gatewatItem> gatewatItemArrayList) {
        String GWList= "";
        String tempString;

        if(gatewatItemArrayList == null) return "";

        for (TrackerInfoClass.gatewatItem item: gatewatItemArrayList) {

//            Log.d(TAG, "gwid:" + item.id + "\tmark\t" + mIDUlsist.mGetMark(item.id));
            if (mIDUlsist.mGetMark(item.id) != null){
                Log.d(TAG, "GWID:\t" + item.id + "\tRSSI:\t" + item.rssi + "\t SNR:\t" + item.snr);
                tempString = "GWid: " + mIDUlsist.mGetMark(item.id)
                        + "  rssi: " + item.rssi
                        + "  snr: " +item.snr
                        + "\n";
                GWList = GWList + tempString;
            }
        }
//        Log.d(TAG, "GWList:\n" + GWList);
        return GWList;
    }

    class CountClass {
        int count, addr;
        double GPSE;
        public CountClass(double gpse, int count, int addr) {
            this.GPSE = gpse;
            this.count = count;
            this.addr = addr;
        }

        private void increase (){
            count = count +1;
        }
    }

    //return the coordinate appears most times
    private TrackerInfoClass GPSECountImpl(ArrayList<SourceClass> list) {
        Log.d(TAG, "list.size" + list.size());
        Map<Double, CountClass>  map = new HashMap<>();

        //first start exec
        double douGPSE = list.get(0).getTrackerInfoClass().getGPSE();
        CountClass temp = new CountClass(douGPSE, 1, 0);
        map.put(douGPSE, temp); //adding to container

        for (int i = 1; i < list.size(); i++) {
            douGPSE = list.get(i).getTrackerInfoClass().getGPSE();
            if ((list.get(i - 1).getTrackerInfoClass().getAP_TS() - list.get(i).getTrackerInfoClass().getAP_TS())/1000 > 5) { //previous and next AP time difference small than 5s
//                Log.d(TAG, "AP_TS difference small than 5s\t" + "previous:\t" + list.get(i - 1).getTrackerInfoClass().getAP_TS() +"\t-\tcurrent\t" + list.get(i - 1).getTrackerInfoClass().getAP_TS() + "\t=\t" + (list.get(i - 1).getTrackerInfoClass().getAP_TS() - list.get(i).getTrackerInfoClass().getAP_TS()));
                continue;
            } else if (map.containsKey(douGPSE)) { // GPS_E exist, that count + 1
//                Log.d(TAG, "GPS_E repeat");
                map.get(douGPSE).increase();
            } else {
                // newer GPS_E
//                Log.d(TAG, "new GPS_E");
//                int GPSE = getLast3num(list.get(i).getTrackerInfoClass().getGPSE()); // fetch last number of GPSE
                douGPSE = list.get(i).getTrackerInfoClass().getGPSE();
                temp = new CountClass(douGPSE, 1, i);
                map.put(douGPSE, temp);
            }

        }

        Log.d(TAG, "sort result");
        List<Map.Entry<Double, CountClass>> sortlist = new ArrayList<>(map.entrySet());
        Collections.sort(sortlist, new Comparator<Map.Entry<Double, CountClass>>() {
            @Override
            public int compare(Map.Entry<Double, CountClass> entry1, Map.Entry<Double, CountClass> entry2) {
                return entry1.getValue().count - entry2.getValue().count;
            }
        });

        Log.d(TAG, "Had max counts GPS_E " + list.get(sortlist.get(sortlist.size()-1).getValue().addr).getTrackerInfoClass().getGPSE());
        Log.d(TAG, "count" + sortlist.get(sortlist.size()-1).getValue().count);

        return list.get(sortlist.get(sortlist.size()-1).getValue().addr).getTrackerInfoClass();
    }

}
