package com.ngll_prototype.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chris on 2016/12/13.
 */

public class SourceClass implements Serializable {
    private static final long serialVersionUID = -2132746362234281199L;

    @SerializedName("_source")
    private TrackerInfoClass mTrackerInfoClass = new TrackerInfoClass();

    public TrackerInfoClass getTrackerInfoClass() {
        return mTrackerInfoClass;
    }

    public void setTrackerInfoClass(TrackerInfoClass trackerInfoClass) {
        mTrackerInfoClass = trackerInfoClass;
    }
}
