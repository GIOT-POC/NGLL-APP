package com.ngll_prototype;

public interface ShowTrackerView {

//    void drawIndoorMap (int x, int y, int floorNum);

    void drawIndoorMap (int x, int y, int floorNum, int anchorNum);

    void cleanDraw ();

    void showAllDot (int x, int y);
}
