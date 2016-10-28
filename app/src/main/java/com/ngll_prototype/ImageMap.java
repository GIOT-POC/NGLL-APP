package com.ngll_prototype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
/**
 * Created by Chris on 2016/10/17.
 */
public class ImageMap extends ImageView {
    // by default, this is true
    private boolean mFitImageToScreen=true;


    /*
     * Bitmap handling
    */
    Bitmap mImage;
    Bitmap mOriginal;

    // Info about the bitmap (sizes, scroll bounds)
    // initial size
    int mImageHeight;
    int mImageWidth;
    float mAspect;
    // minimum height/width for the image
    int mMinWidth=-1;
    int mMinHeight=-1;
    // maximum height/width for the image
    int mMaxWidth=-1;
    int mMaxHeight=-1;

    // the position of the top left corner relative to the view
    int mScrollTop;
    int mScrollLeft;

    // view height and width
    int mViewHeight=-1;
    int mViewWidth=-1;

    // mMaxSize controls the maximum zoom size as a multiplier of the initial size.
    // Allowing size to go too large may result in memory problems.
    //  set this to 1.0f to disable resizing
    // by default, this is 1.5f
    private static final float defaultMaxSize = 1.5f;
    private float mMaxSize = 1.5f;


    public ImageMap(Context context) {
        super(context);
    }
    //possible to reduce memory consumption
    protected BitmapFactory.Options options;

    @Override
    public void setImageBitmap(Bitmap bm)
    {
        if (mImage==mOriginal)
        {
            mOriginal=null;
        }
        else
        {
            mOriginal.recycle();
            mOriginal=null;
        }
        if (mImage != null)
        {
            mImage.recycle();
            mImage=null;
        }
        mImage = bm;
        mOriginal = bm;
        mImageHeight = mImage.getHeight();
        mImageWidth = mImage.getWidth();
        mAspect = (float)mImageWidth / mImageHeight;
        setInitialImageBounds();
    }

    @Override
    public void setImageResource(int resId)
    {
        final String imageKey = String.valueOf(resId);
        BitmapHelper bitmapHelper = BitmapHelper.getInstance();
        Bitmap bitmap = bitmapHelper.getBitmapFromMemCache(imageKey);

        // 1 is the default setting, powers of 2 used to decrease image quality (and memory consumption)
        // TODO: enable variable inSampleSize for low-memory devices
        options = new BitmapFactory.Options();
        options.inSampleSize = 1;

        if (bitmap == null)
        {
            bitmap = BitmapFactory.decodeResource(getResources(), resId, options);
            bitmapHelper.addBitmapToMemoryCache(imageKey, bitmap);
        }
        setImageBitmap(bitmap);
    }

    /*
        setImageDrawable() is called by Android when the android:src attribute is set.
        To avoid this and use the more flexible setImageResource(),
        it is advised to omit the android:src attribute and call setImageResource() directly from code.
     */
    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            setImageBitmap(bd.getBitmap());
        }
    }


    /**
     * set the initial bounds of the image
     */
    void setInitialImageBounds()
    {
        if (mFitImageToScreen)
        {
            setInitialImageBoundsFitImage();
        }
        else
        {
//            setInitialImageBoundsFillScreen();
        }
    }

    /**
     * setInitialImageBoundsFitImage sets the initial image size to match the
     * screen size.  aspect ratio may be broken
     */
    void setInitialImageBoundsFitImage()
    {
        if (mImage != null)
        {
            if (mViewWidth > 0)
            {
                mMinHeight = mViewHeight;
                mMinWidth = mViewWidth;
                mMaxWidth = (int)(mMinWidth * mMaxSize);
                mMaxHeight = (int)(mMinHeight * mMaxSize);

                mScrollTop = 0;
                mScrollLeft = 0;
//                scaleBitmap(mMinWidth, mMinHeight);
            }
        }
    }

}
