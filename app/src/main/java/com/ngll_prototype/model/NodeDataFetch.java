package com.ngll_prototype.model;

import android.content.Context;

import com.ngll_prototype.object.TrackerInfoClass;

public interface NodeDataFetch {

    interface OnNodeDataFetchListener {
        void onSucess(TrackerInfoClass TrackObj);

        void onError();
    }

    void getNodeData (Context context, String mac, OnNodeDataFetchListener listener);
}
