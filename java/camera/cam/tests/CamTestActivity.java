package camera.cam.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import camera.cam.tests.Preview;
import camera.cam.R;

public class CamTestActivity extends Activity {
    private static final String TAG = "CamTestActivity";
    Preview preview;
    Button buttonClick;
    Camera camera;
    Activity act;
    Context ctx;
    boolean haveTouch;
    Rect touchArea;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);

        preview = new Preview(this, (SurfaceView)findViewById(R.id.surfaceView));
        preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);

        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){
            try{
                camera = Camera.open(0);
                camera.startPreview();
                preview.setCamera(camera);
            } catch (RuntimeException ex){
               // Toast.makeText(ctx,"camera not found", Toast.LENGTH_LONG).show();
            }
        }
        Camera.Parameters params = camera.getParameters();
        if(params.getSupportedFocusModes() != null && params.getSupportedFlashModes().contains(Camera.Parameters.FOCUS_MODE_MACRO)){
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        }

        if (params.getSupportedFlashModes() != null && params.getSupportedFlashModes().contains(Camera.Parameters.FLASH_MODE_AUTO)){
            params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);

        }


        camera.setParameters(params);
        /*preview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                camera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });*/
        Toast.makeText(ctx, "take photo help", Toast.LENGTH_LONG).show();

        		buttonClick = (Button) findViewById(R.id.btnCapture);

        		buttonClick.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        camera.autoFocus(myAutoFocusCallback);

                        camera.takePicture(shutterCallback, rawCallback, jpegCallback);

        //camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        }
        });

        }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();
            float touchMajor = event.getTouchMajor();
            float touchMinor = event.getTouchMinor();

            Rect touchRect = new Rect(
                    (int)(x - touchMajor/2),
                    (int)(y - touchMinor/2),
                    (int)(x + touchMajor/2),
                    (int)(y + touchMinor/2));

            this.touchFocus(touchRect);

        }


        return true;
    }
    public void touchFocus(final Rect tfocusRect){

        //Convert from View's width and height to +/- 1000
        final Rect targetFocusRect = new Rect(
                tfocusRect.left * 2000/preview.getWidth() - 1000,
                tfocusRect.top * 2000/preview.getHeight() - 1000,
                tfocusRect.right * 2000/preview.getWidth() - 1000,
                tfocusRect.bottom * 2000/preview.getHeight() - 1000);

        final List<Camera.Area> focusList = new ArrayList<Camera.Area>();
        Camera.Area focusArea = new Camera.Area(targetFocusRect, 1000);
        focusList.add(focusArea);

        Camera.Parameters para = camera.getParameters();
        para.setFocusAreas(focusList);
        para.setMeteringAreas(focusList);
        camera.setParameters(para);

        camera.autoFocus(myAutoFocusCallback);

        this.setHaveTouch(true, tfocusRect);
        preview.invalidate();

        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    public void setHaveTouch(boolean t, Rect tArea){
        haveTouch = t;
        touchArea = tArea;
    }


    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback(){

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            // TODO Auto-generated method stub
            if (arg0){
                camera.cancelAutoFocus();
            }

            float focusDistances[] = new float[3];
            arg1.getParameters().getFocusDistances(focusDistances);
            System.out.println(focusDistances[0]);
            System.out.println( focusDistances[1]);
            System.out.println(focusDistances[2]);
            System.out.println("Optimal Focus Distance(meters): "
                    + focusDistances[Camera.Parameters.FOCUS_DISTANCE_OPTIMAL_INDEX]);

        }};

    ShutterCallback myShutterCallback = new ShutterCallback(){

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }};

    PictureCallback myPictureCallback_RAW = new PictureCallback(){

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub

        }};
@Override
protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){
            try{
                camera = Camera.open(0);
                camera.startPreview();
                preview.setCamera(camera);
            } catch (RuntimeException ex){
                Toast.makeText(ctx,"camera not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        if(camera != null) {
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    private void resetCam() {
        camera.startPreview();
        preview.setCamera(camera);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveImageTask().execute(data);
            resetCam();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

    }
}