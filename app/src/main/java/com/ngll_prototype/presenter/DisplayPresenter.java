package com.ngll_prototype.presenter;

import android.content.Context;
import android.widget.ImageView;


public interface DisplayPresenter {

    void getNodePOS(Context context, String mac);

    void stopGetNodePOS();

    void setIMGCoordinate(Context context, ImageView imageView);

    void showAllDot(Context context);

    void cleanAllDot(Context context);

    String getPreviousGWiNFO();

    String getCurrentGWiNFO();

    void onDestroy();
}
