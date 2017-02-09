package com.ngll_prototype.model;

import android.content.Context;

import com.ngll_prototype.object.SourceClass;

import java.util.ArrayList;

public interface NodeDataFetch {

    interface OnNodeDataFetchListener {
//        void onSucess(TrackerInfoClass TrackObj);
        void onSucess(ArrayList<SourceClass> SourceList);

        void onError();
    }

    void getNodeData (Context context, String mac, OnNodeDataFetchListener listener);
}
