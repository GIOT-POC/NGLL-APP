package com.ngll_prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngll_prototype.presenter.DisplayPresenter;
import com.ngll_prototype.presenter.DisplayPresenterImpl;

public class ShowTrackerActivity extends AppCompatActivity implements ShowTrackerView, Handler.Callback {
    private final static String TAG = "MainActivityNew";

    private ImageView mImageView;
    private TextView mTvMac;
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

    String mac = "101a0a000024";

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
        setup();
    }

    private void setup() {
        mBtnExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Go Btn pressed");
                mDisplayPresenter.getNodePOS(mContext, mac);
                mBtnExe.setClickable(false);
            }
        });

        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDisplayPresenter.stopGetNodePOS();
                mBtnExe.setClickable(true);
            }
        });
    }

    private void sendMessage(int msg) {
        Log.d(TAG, "in sendMessage");
        mHandler.obtainMessage(msg).sendToTarget();
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
    public void drawIndoorMap(int x, int y) {
        Log.d(TAG, "drawIndoorMap, \tx:\t" + x + "\ty:\t" + y);
//        mImageView.buildDrawingCache();
        mCanvas.drawBitmap(mbitmapBG, mMatrix, mPaint);
        mCanvas.drawCircle(x, y, 7, mPaint);
        sendMessage(MSG_VIEW_INVALIDATE);
    }

    @Override
    public boolean handleMessage(Message message) {
        Log.d(TAG, "in handleMessage, what:\t" + message.what);
        switch (message.what){
            case MSG_VIEW_INVALIDATE:
                Log.d(TAG, "in MSG_VIEW_INVALIDATE");
                mImageView.invalidate();
                break;
        }
        return false;
    }
}