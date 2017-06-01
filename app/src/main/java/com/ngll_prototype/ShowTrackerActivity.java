package com.ngll_prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngll_prototype.presenter.DisplayPresenter;
import com.ngll_prototype.presenter.DisplayPresenterImpl;

public class ShowTrackerActivity extends AppCompatActivity implements ShowTrackerView, Handler.Callback {
    private final static String TAG = "MainActivityNew";

    private ImageView mImageView;
    private TextView mTvMac, mTvGwInfo, mTvPreGWinfo, mTvFloorNum, mTvAnchorNum;
    private Button mBtnExe;
    private Button mBtnStop;

    private DisplayPresenter mDisplayPresenter;
    ProgressDialog mProgressDialog;

    private Bitmap mbitmapBG, mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private int mStatusBarHeight, mActionBarHeight;
    private Matrix mMatrix;

    private boolean firstExe = true;
    private Context mContext;
    private Handler mHandler;
    private final static int MSG_VIEW_INVALIDATE = 0x1;
    private final static int MSG_VIEW_INVALIDATE_ALL_DOT = 0x2;

    String mac = "101a0a000025";
//    String mac = "101a0a000024";
    private static int press_count;
    private static boolean displaydot=false;
    int intfloor=-99;
    int intanchor= -99;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mHandler = new Handler(this);
        mDisplayPresenter = new DisplayPresenterImpl(this);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mTvMac = (TextView) findViewById(R.id.tvmac);
        mTvMac.setText(mac);
        mBtnExe = (Button) findViewById(R.id.btnexe);
        mBtnStop = (Button) findViewById(R.id.btnstop);
        mTvGwInfo = (TextView) findViewById(R.id.tvgwinfo);
        mTvPreGWinfo = (TextView) findViewById(R.id.tvpregwinfo);
        mTvFloorNum = (TextView) findViewById(R.id.tvfloorNum);
        mTvAnchorNum = (TextView) findViewById(R.id.tvanchorNum);
        setup();
        toolbar();
    }

    private void setup() {
        mBtnExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Go Btn pressed");
                mDisplayPresenter.getNodePOS(mContext, mac);
                mBtnExe.setClickable(false);
                mBtnExe.setEnabled(false);
            }
        });

        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                mDisplayPresenter.stopGetNodePOS();
                mBtnExe.setClickable(true);
                mBtnExe.setEnabled(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_list, menu);

        return true;
    }

    private void toolbar() {
        final Toolbar mainToolBar = (Toolbar) findViewById(R.id.topmost_toolbar);
        setSupportActionBar(mainToolBar);
        mainToolBar.setTitle(getString(R.string.app_name));
//        mainToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.menu.toolbar_list:
//
//                        break;
//                }
//                return false;
//            }
//        });

        mainToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                press_count  +=1;
                if (press_count ==5 && !displaydot) {
                    Log.d(TAG, "count\t" + press_count);
                    Log.d(TAG, "reset count");
                    mDisplayPresenter.showAllDot(ShowTrackerActivity.this);
                    displaydot = true;
                    press_count = 0;
                } else if(press_count == 2 && displaydot){
                    mDisplayPresenter.cleanAllDot(ShowTrackerActivity.this);
                    press_count = 0;
                    displaydot = false;
                }
            }
        });
    }

    private void initDraw() {
        Drawable drawable = mImageView.getDrawable();

        final int origH = drawable.getIntrinsicHeight();
        final int origW = drawable.getIntrinsicWidth();

        mImageView.buildDrawingCache();
        mbitmapBG = mImageView.getDrawingCache();

        mBitmap = Bitmap.createBitmap(origW/*width*/, origH/*height*/, Bitmap.Config.ARGB_8888); //create a bitmap size same as bitmapBG
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        mMatrix = new Matrix();
        mCanvas.drawBitmap(mbitmapBG, mMatrix, mPaint);
        mImageView.setImageBitmap(mBitmap);

        mDisplayPresenter.setIMGCoordinate(this, mImageView);
    }

    private void blockUI(final String message) {
        if (message != null) {
            mProgressDialog = ProgressDialog.show(this, "", message, true);
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisplayPresenter.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (firstExe) {
            firstExe = false;
            initDraw();
        }
    }

    @Override
    public void showAllDot(int x, int y) {
        mCanvas.drawCircle(x, y, 7, mPaint);
        sendMessage(MSG_VIEW_INVALIDATE_ALL_DOT);
    }

    @Override
    public void cleanDraw() {
        mCanvas.drawBitmap(mbitmapBG, mMatrix, mPaint); // clear screen
        sendMessage(MSG_VIEW_INVALIDATE_ALL_DOT);
    }

    @Override
    public void drawIndoorMap(int x, int y, int floorNum, int anchorNum) {
        Log.d(TAG, "drawIndoorMap, \tx:\t" + x + "\ty:\t" + y + "\tfloor:\t" + floorNum);
//        mImageView.buildDrawingCache();
        mCanvas.drawBitmap(mbitmapBG, mMatrix, mPaint); // clear screen
        mCanvas.drawCircle(x, y, 10, mPaint);
        intfloor = floorNum;
        intanchor = anchorNum;
        sendMessage(MSG_VIEW_INVALIDATE);
    }

    private void sendMessage(int msg) {
//        Log.d(TAG, "in sendMessage");
//        Message.obtain(mHandler, msg).sendToTarget();
        mHandler.obtainMessage(msg).sendToTarget();
    }

    @Override
    public boolean handleMessage(Message message) {
//        Log.d(TAG, "in handleMessage, what:\t" + message.what);
        switch (message.what){
            case MSG_VIEW_INVALIDATE:
                Log.d(TAG, "in MSG_VIEW_INVALIDATE");
//                mTvGwInfo.setText("");
//                mTvGwInfo.setText(mDisplayPresenter.getCurrentGWiNFO());
//                mTvPreGWinfo.setText(mDisplayPresenter.getPreviousGWiNFO());
                mTvFloorNum.setText(Integer.toString(intfloor));
                mTvAnchorNum.setText(Integer.toString(intanchor));
                mImageView.invalidate();
                break;

            case MSG_VIEW_INVALIDATE_ALL_DOT:
//                Log.d(TAG, "MSG_VIEW_INVALIDATE_ALL_DOT");
                mTvGwInfo.setText("");
                mImageView.invalidate();
                break;
        }
        return false;
    }
}
