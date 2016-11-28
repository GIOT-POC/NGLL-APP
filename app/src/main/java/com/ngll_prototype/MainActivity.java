package com.ngll_prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngll_prototype.object.IMGCoordinate;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;


public class MainActivity extends AppCompatActivity implements ShowTrackerView {

    private final static String TAG = "MainActivity";
    CustomDrawableView cd;
    private ImageView image;
    private ImageView imageView;
    private int ImgInfo[];
    private TextView tvMac;
    private Button mbtnExe;
    private Button mbtnStop;

    ProgressDialog mProgressDialog;
    DisaplyPoint disaplyPoint;

    private Bitmap bitmapBG, bitmap;
    private Canvas canvas;
    private Paint paint;
    private int StatusBarHeight, ActionBarHeight;
    private JSONArray list;
    private Matrix matrix;

    private ArrayList<IMGCoordinate> IMGCoorlist = new ArrayList<>();

    private boolean firstExe = true;
    private static boolean goFlag;

    String query = "{\n" +
            " \"query\" : {\n" +
            "   \"match\" : {\n" +
            "     \"data.macAddr\" : {\n" +
            "       \"query\" : \"101a0a000024\",\n" +
            "       \"type\" : \"boolean\"\n" +
            "        }\n" +
            "     }\n" +
            "   }\n" +
            "  }";
    String query2 = "{\n"
            + "  \"query\":{\n"
            + "      \"match\":{\n"
            + "          \"data\": {\n"
            + "              \"macAddr\" : \"101a0a000024\" \n"
            + "                      }\n"
            + "                  }\n"
            + "              }\n"
            + "}";

    String query3 =
            "{\n" +
                    "  \"query\" : {\n" +
                    "    \"match\" : {\n" +
                    "      \"data.macAddr\" : {\n" +
                    "        \"query\" : \"101a0a000024\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.imageView);
        tvMac = (TextView) findViewById(R.id.tvmac);
        mbtnExe = (Button) findViewById(R.id.btnexe);
        mbtnStop = (Button) findViewById(R.id.btnstop);

        setTitle("NGLL Prototype");
        setup();
//        Node node = nodeBuilder().node();
//        Client client = node.client();

//        try {
//            Settings settings = Settings.settingsBuilder()
//                    .put("", "").build();
//            TransportClient client = TransportClient.builder().settings(settings).build()
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("54.169.218.103"), 9200));
//            Log.d(TAG, "client:\t" + client.toString());
//            client.close();
////            GetRequestBuilder getRequestBuilder = client.prepareGet();
////            getRequestBuilder.setFields("");
////            GetResponse response = getRequestBuilder.execute().actionGet();
////            String name = response.getField("").getValue().toString();
////            Log.d(TAG, "name:\t" + name );
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }


    }

    private void setup() {

//        int count = 0;
        mbtnExe.setOnClickListener(new View.OnClickListener() {
            //            int count = 0;
            @Override
            public void onClick(View view) {
//                Log.d(TAG, "mbtnExe pressed");
//                int indoorCo[] ={8, count};
//                paint(indoorCo);
//                count  = count +1;
                disaplyPoint = new DisaplyPoint();
                disaplyPoint.execute("101a0a000024");
                goFlag = true;
                chgButtonST(mbtnExe, false);
            }
        });

        mbtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goFlag = false;
                chgButtonST(mbtnExe, true);
            }
        });
        tvMac.setText("101a0a000024");
    }

    private void init() {
//		RelativeLayout layout = (RelativeLayout) findViewById(R.id.root);
//		final DrawView view = new DrawView(this);
//		view.setMinimumHeight(700);
//		view.setMinimumWidth(1274);
//
//		//通知view组件重绘
//		view.invalidate();
//		layout.addView(view);

    }

    private void chgButtonST(Button btn, boolean state) {
        btn.setClickable(state);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged");

        if (firstExe) {
            firstExe = false;
            ImgInfo = getImageStartCoordinate(image); //get Image top, left, Height, wight
            Log.d(TAG, "ImgInfo Top:\t" + ImgInfo[0]);
            Log.d(TAG, "ImgInfo Left:\t" + ImgInfo[1]);
            Log.d(TAG, "ImgInfo Height:\t" + ImgInfo[2]);
            Log.d(TAG, "ImgInfo Wight:\t" + ImgInfo[3]);

            bitmapBG = BitmapFactory.decodeResource(getResources(), R.drawable.gemtek8b_block2);
            Log.d(TAG, "bitmapBG.getHeight():\t" + bitmapBG.getHeight() + "\tbitmapBG.getWidth():\t" + bitmapBG.getWidth());
//		View v = new MyCanvas(getApplicationContext());
            bitmap = Bitmap.createBitmap(bitmapBG.getWidth()/*width*/, bitmapBG.getHeight()/*height*/, Bitmap.Config.ARGB_8888); //create a bitmap size same as bitmapBG
            canvas = new Canvas(bitmap);
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(5);
            matrix = new Matrix();
            canvas.drawBitmap(bitmapBG, matrix, paint);

//		canvas.drawCircle(324, 330, 10, paint);
//		canvas.drawCircle(324, 168, 10, paint);
//		canvas.drawText("Sample Text", 10, 10, paint);
            StatusBarHeight = getStatusBarHeight();
            ActionBarHeight = getActionBarHeight();
            Log.d(TAG, "StatusBarHeight:\t" + StatusBarHeight + "\tActionBarHeight:\t" + ActionBarHeight);
            image.setImageBitmap(bitmap);
//        canvas.save();
            ImgCoordinateSet(ImgInfo);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();//取x軸
        int y = (int) event.getY();//取y軸
        int action = event.getAction();//取整數
        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                setTitle("按下:" + x + "," + y);
                break;
            case MotionEvent.ACTION_MOVE:
//                setTitle("平移:" + x + "," + y);
                break;
            case MotionEvent.ACTION_UP:
//                setTitle("彈起:" + x + "," + y);
                break;
        }

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Drawable drawable = imageView.getDrawable();
        Rect imageBounds = drawable.getBounds();

        int[] posXY = new int[2];
        imageView.getLocationOnScreen(posXY);

        //original height and width of the bitmap
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int intrinsicWidth = drawable.getIntrinsicWidth();

        //height and width of the visible (scaled) image
        int scaledHeight = imageBounds.height();
        int scaledWidth = imageBounds.width();

        //Find the ratio of the original image to the scaled image
        //Should normally be equal unless a disproportionate scaling
        //(e.g. fitXY) is used.
        float heightRatio = intrinsicHeight / scaledHeight;
        float widthRatio = intrinsicWidth / scaledWidth;

        //do whatever magic to get your touch point
        //MotionEvent event;

        //get the distance from the left and top of the image bounds
        float scaledImageOffsetX = event.getX() - imageBounds.left;
        float scaledImageOffsetY = event.getY() - imageBounds.top;


        Log.d(TAG, "scaledImageOffsetX:\n" + scaledImageOffsetX);
        Log.d(TAG, "scaledImageOffsetY:\n" + scaledImageOffsetY);


        //scale these distances according to the ratio of your scaling
        //For example, if the original image is 1.5x the size of the scaled
        //image, and your offset is (10, 20), your original image offset
        //values should be (15, 30).
        float originalImageOffsetX = scaledImageOffsetX * widthRatio;
        float originalImageOffsetY = scaledImageOffsetY * heightRatio;

        return super.onTouchEvent(event);
    }

    @Override
    public void drawIndoorMap(int x, int y, String gwlist) {

    }

    public class CustomDrawableView extends View {
        private ShapeDrawable mDrawable;
        private Paint mPaint;
        private int x = 200;
        private int y = 600;
        private int width = 100;
        private int height = 50;

        public CustomDrawableView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            mDrawable = new ShapeDrawable(new RectShape());
            mDrawable.setBounds(x, y, x + width, y + height);

            mPaint = mDrawable.getPaint();
            mPaint.setColor(Color.BLUE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            mDrawable.draw(canvas);
        }
    }

    public static int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

//        for(int i =0; i < f.length; i++){
//            Log.d(TAG, "f:" + f[i]);
//        }

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;


        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        Log.d(TAG, "origW:\t" + origW + "\norigH:\t" + origH);
        Log.d(TAG, "actW:\t" + actW + "\nactH:\t" + actH);
        Log.d(TAG, "imgViewW:\t" + imgViewW + "\nimgViewH:\t" + imgViewH);


        int top = (int) (imgViewH - actH) / 2;
        int left = (int) (imgViewW - actW) / 2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    public int[] getImageStartCoordinate(ImageView image) {
        int[] intXY = new int[2];
//        ImageView image = (ImageView)findViewById(R.id.imageView);
        Drawable drawable = image.getDrawable();
        Rect imageBounds = drawable.getBounds();
        image.getLocationOnScreen(intXY);
//        imageView.getLocationInWindow(intXY);

        final int origW = drawable.getIntrinsicWidth();
        final int origH = drawable.getIntrinsicHeight();
//        drawable.get
        final int[] ret = new int[4];
        //intXY[0] = x, [1] = y, start top & left
        ret[0] = intXY[0];
        ret[1] = intXY[1];
        ret[2] = origH;
        ret[3] = origW;
        return ret;
    }


    /*
    * calculation pic's coordinate with number save by Json
    */
    public void ImgCoordinateSet(int[] ImgInfo) {
        IMGCoordinate icl;
        JSONObject obj = new JSONObject();
        JSONObject obj2 = new JSONObject();
        list = new JSONArray();

        int toScaleH = 5;
        int toScaleW = 6;

        int spacingH = ImgInfo[2] / toScaleH;
        int spacingW = ImgInfo[3] / toScaleW;
        Log.d(TAG, "spacingH:\t" + spacingH + "\tspacingW:\t" + spacingW);
//        int x = ImgInfo[0];
//        int y = ImgInfo[1];
        int x = 0;
        int y = 0;
        int count = 0;
        Log.d(TAG, "Start x:\t" + x + "\ty:\t" + y);
        for (int i = 1; i < toScaleH; i++) {
//            x = ImgInfo[0];
            x = 0;
            y = y + spacingH;
            for (int j = 1; j < toScaleW; j++) {
                try {

                    x = x + spacingW;
                    obj.put("x", x);
                    obj.put("y", y);

                    obj2.put(Integer.toString(count), obj);
//                  Log.d(TAG, "No." + count +"\tX:\t" + x + "\tY:\t" + y);
//					Log.d(TAG, "obj2:\t" + obj2.toString());
//                    canvas.drawCircle(x, y -StatusBarHeight - ActionBarHeight, 5, paint);
                    canvas.drawCircle(x, y, 5, paint);

                    list.put(obj2);
                    obj = new JSONObject();
                    obj2 = new JSONObject();
                    icl = new IMGCoordinate(count, x, y);

                    IMGCoorlist.add(icl);
                    count = count + 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
        Log.d(TAG, "IMGCoorlist.length:\t" + IMGCoorlist.size());
        for (int i = 0; i < IMGCoorlist.size(); i++) {

            Log.d(TAG, "I:\t" + i + "\tcount:\t" + IMGCoorlist.get(i).getNumber() + "\tX:\t" + IMGCoorlist.get(i).getX()
                    + "\tY:\t" + IMGCoorlist.get(i).getY());
        }

    }

    class DisaplyPoint extends AsyncTask<String, Integer, int[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            blockUI(getString(R.string.loading));
        }

        @Override
        protected int[] doInBackground(String... strings) {
            int coordinate[];
            while (goFlag) {


                try {
                    JestClientFactory factory = new JestClientFactory();
                    factory.setDroidClientConfig(new DroidClientConfig.Builder(getString(R.string.elasticsvr))
                            .multiThreaded(true)
                            .build());

                    JestClient client = factory.getObject();

                    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                    searchSourceBuilder.sort("@timestamp", SortOrder.DESC);
                    searchSourceBuilder.size(50);
                    searchSourceBuilder.query(QueryBuilders.matchQuery("data.macAddr", strings[0])); // generate DSL format

                    // newer search rule, desc find latest data & GPS_N not 0
                    SearchSourceBuilder searchSourceBuilderTry = new SearchSourceBuilder();
                    searchSourceBuilderTry.sort("@timestamp", SortOrder.DESC);
                    searchSourceBuilderTry.size(1);
                    searchSourceBuilderTry.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("data.macAddr", strings[0])).mustNot(QueryBuilders.termQuery("data.GPS_N", "0")));

                    Search search = new Search.Builder(searchSourceBuilderTry.toString()).addIndex("client_report").addType("asset_tracker").build();
                    SearchResult searchResult = client.execute(search);

//                    Log.d(TAG, "searchSourceBuilder:\t" + searchSourceBuilder.toString());
//                    Log.d(TAG, "Total:\t" + searchResult.getTotal());
//                    Log.d(TAG, "searchResult:\t" + searchResult.getJsonString());

                    IndoorLocation indoorLocation = new IndoorLocation();
                    coordinate = indoorLocation.getPositionNum(searchResult.getJsonObject());


                    //get specific document
//                    Get get = new Get.Builder("client_report", "AVe2_o5uJozORed8MBm_").type("asset_tracker").build();
//                    JestResult result = client.execute(get);
//                    Log.d(TAG, "Getting Document:\t" + result.getJsonString());

                    if (coordinate != null) {
                        publishProgress(coordinate[1]);
                    }

                    client.shutdownClient();

                    Thread.sleep(5000);

                } catch (UnknownError e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            paint(values[0]);
        }

        @Override
        protected void onPostExecute(int result[]) {
            super.onPostExecute(result);

//            Log.d(TAG, "result[]:\t" + result[0] + "\t, " + result[1] );
//            paint(result[1]);
//            blockUI(null);
        }
    }

    public void paint(int anchorNUM) {
        if (anchorNUM + 1 > IMGCoorlist.size()) {
            return;
        }
        canvas.drawBitmap(bitmapBG, matrix, paint); // clear screen

        int x = IMGCoorlist.get(anchorNUM).getX();
//        int y = IMGCoorlist.get(coordinate[1]).getY() - StatusBarHeight - ActionBarHeight;
        int y = IMGCoorlist.get(anchorNUM).getY();

//        Log.d(TAG,"paint, input para coordinate[] :\t" + "\t[0]:\t" + coordinate[0] + "\t[1]:\t" + coordinate[1]);
        Log.d(TAG, "paint, x:\t" + x);
        Log.d(TAG, "paint, y:\t" + y);

        canvas.drawCircle(x, y, 7, paint);

        image.invalidate();
    }

    /*
    * get Status bar  Height
    */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /*
    * get Action bar  Height
    */
    public int getActionBarHeight() {
        int result = 0;
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return result;
    }

    private void blockUI(final String message) {
        if (message != null) {
            mProgressDialog = ProgressDialog.show(this, "", message, true);
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }


}