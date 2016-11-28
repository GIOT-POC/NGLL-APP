package com.ngll_prototype.object;

import java.util.HashMap;

/**
 * Created by Chris on 2016/11/24.
 */

public class IDUlist {
    HashMap mIDUList = new HashMap();

    public IDUlist() {
        mIDUList.put("00001c497b48dcdd" ,"A");
        mIDUList.put("00001c497b48dce6" ,"A");
        mIDUList.put("00001c497b48dbc0" ,"B");
        mIDUList.put("00001c497b48dbc1" ,"B");
        mIDUList.put("00001c497b48dc3e" ,"C");
        mIDUList.put("00001c497b48db93" ,"C");
        mIDUList.put("00001c497b48dbcf" ,"D");
        mIDUList.put("00001c497b48dbd5" ,"D");
    }

    public String mGetMark(String mac) {
        return (String) mIDUList.get(mac);
    }
}
