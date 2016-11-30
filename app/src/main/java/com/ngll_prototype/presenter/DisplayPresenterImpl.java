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
import com.ngll_prototype.object.TrackerInfoClass;

import java.util.ArrayList;

public class DisplayPresenterImpl implements DisplayPresenter, NodeDataFetch.OnNodeDataFetchListener {
    private final static String TAG = "DisplayPresenterImpl";

    private ShowTrackerView mMainView;
    private NodeDataFetch mNodeDataFetch;
    private ArrayList<IMGCoordinate> mIMGCoordinatesList = new ArrayList<>();
    private static boolean mGetExE;
    private static IDUlist mIDUlsist;

    public DisplayPresenterImpl(ShowTrackerView mainView) {
        this.mMainView = mainView;
        this.mNodeDataFetch = new NodeDataFetchImpl();
        mIDUlsist = new IDUlist();
    }

    @Override
    public void onSucess(TrackerInfoClass TrackObj) {
        int floor = getLast3num(TrackObj.getGPSN());
        int anchor = getLast3num(TrackObj.getGPSE());

        Log.d(TAG, "onSucess, getStatus:\t" + getStatus(TrackObj.getRawData(), 6));
        Log.d(TAG, "anchor:\t" + anchor + "\nmIMGCoordinatesList.size():\t" + mIMGCoordinatesList.size());
        if (getStatus(TrackObj.getRawData(), 6) == 1) {
            if (anchor + 1 <= mIMGCoordinatesList.size())
                if (mMainView != null) {

                    //draw node position in the view
                    mMainView.drawIndoorMap(
                            mIMGCoordinatesList.get(anchor).getX(),
                            mIMGCoordinatesList.get(anchor).getY(),
                            reFormatGwList(TrackObj.getGatewayList()));
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
        int toScaleW = 6;

        int spacingH = origH / toScaleH;
        int spacingW = origW / toScaleW;

        int x, y = 0;
        int count = 0;
        Log.d(TAG, "spacingH:\t" + spacingH + "\tspacingW:\t" + spacingW);

        for (int i = 1; i < toScaleH; i++) {

            x = 0;
            y = y + spacingH;
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

    public String reFormatGwList(ArrayList<TrackerInfoClass.gatewatItem> gatewatItemArrayList) {
        String gwlist=null;
        for (TrackerInfoClass.gatewatItem item: gatewatItemArrayList) {

//            Log.d(TAG, "gwid:" + item.id + "\tmark\t" + mIDUlsist.mGetMark(item.id));
            if (mIDUlsist.mGetMark(item.id) != null){
                gwlist = "GWid: " + mIDUlsist.mGetMark(item.id)
                        + "  rssi: " + item.rssi
                        + "  snr: " +item.snr
                        + "\n";
            }
        }
        return gwlist;
    }
}
