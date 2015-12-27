package camera.cam;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import camera.cam.buttons.AddButton;
import camera.cam.buttons.SettingsButton;
import camera.cam.buttons.UnitButton;
import camera.cam.dialogs.BaseDialog;
import camera.cam.dialogs.PointerDialog;
import camera.cam.dialogs.UnitDialog;
import camera.cam.interfaces.NoticeDialogListener;

public class DisplayAct extends Activity implements OnTouchListener, NoticeDialogListener {

    public static final String PREFS_NAME = "MyPrefsFile";
    private CustomView customView;
    public Bitmap bitmap;
    public List<Pointer> pointerList;
    public CustomView observer;

    private int baseType;
    private int unitType;
    private String unit;

    private GestureDetector gestureDetector;

    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display);
        customView = (CustomView)findViewById(R.id.image);
        customView.setActivity(this);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        baseType = settings.getInt("baseType", 0);
        unitType = settings.getInt("unitType", 0);
        unit = settings.getString("unit", "cm");

        UnitButton unitBtn = (UnitButton)findViewById(R.id.unit_button);
        unitBtn.setActivity(this);
        unitBtn.setOnClickListener();

        AddButton addBtn = (AddButton)findViewById(R.id.add_button);
        addBtn.setActivity(this);
        addBtn.setOnClickListener();

        SettingsButton setBtn = (SettingsButton)findViewById(R.id.settings_button);
        setBtn.setActivity(this);
        setBtn.setOnClickListener();

        Bundle bundle = getIntent().getExtras();
        Uri selectedImage = (Uri) bundle.get("imageUri");
        getContentResolver().notifyChange(selectedImage, null);

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        customView.setOnTouchListener(this);
        observer = customView;

        pointerList = new ArrayList<>();
        pointerList.add(new BasePointer(50, 250, 150, 250, this, 0, observer,baseType));
        pointerList.add(new Pointer(50, 50, 150, 50, this, 1, observer));
        //pointerList.add(new Pointer(50, 350, 150, 350, this, 2, observer));
        //pointerList.add(new Pointer(50, 350, 150, 250, this, 2, observer));

        try {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

            int size = 400;
            bitmap = decodeUri(selectedImage, size);
            bitmap = resize(bitmap,width,height);

        } catch (Exception e) {
            System.out.println("Problem!");
        }
    }

    public String getUnit() {
        return this.unit;
    }

    public int getUnitType() {
        return this.unitType;
    }

    public int getBaseType() {
        return this.baseType;
    }

    @Override
    protected void onPause() {
        super.onPause();
        customView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        customView.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("baseType", baseType);
        editor.putInt("unitType",unitType);
        editor.putString("unit", unit);

        // Commit the edits!
        editor.commit();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (gestureDetector.onTouchEvent(event)) {
            // double tap
            Toast.makeText(getApplicationContext(), "Double tap",
                    Toast.LENGTH_LONG).show();

            for (int i = 0; i < pointerList.size(); i++) {
                boolean m = pointerList.get(i).checkMoving(event);
                if( m == true) {
                    index = i;
                    PointerDialog dialogFragment = new PointerDialog();
                    dialogFragment.show(this.getFragmentManager(), "DialogFragment");
                    break;
                }
            }


            return true;
        } else {
            // your code for move and drag
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    for (Pointer p : pointerList) {
                        boolean m = p.checkMoving(event);
                        if( m == true) {
                            break;
                        }
                    }

                    break;

                case MotionEvent.ACTION_MOVE:
                    for (Pointer p : pointerList) {
                        p.move(event);
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    for (Pointer p : pointerList) {
                        p.stopMoving(event);
                    }
                    break;
            }
        }


        return true;
    }

    private Bitmap decodeUri(Uri selectedImage, int required_size) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        //final int REQUIRED_SIZE = 400;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < required_size || height_tmp / 2 < required_size) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;

        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        if(dialog instanceof BaseDialog) {
            this.baseType = ((BaseDialog) (dialog)).getSelectedItem();
            pointerList.set(0, new BasePointer(pointerList.get(0).getPoint1X(), pointerList.get(0).getPoint1Y(),
                    pointerList.get(0).getPoint2X(), pointerList.get(0).getPoint2Y(),
                    this, 0, observer, baseType));
        }

        if(dialog instanceof UnitDialog) {
            Resources res = getResources();
            String[] units = res.getStringArray(R.array.string_array_units);
            this.unitType = ((UnitDialog) (dialog)).getSelectedItem();
            this.unit = units[unitType];
        }

        if(dialog instanceof PointerDialog) {
            int selected = ((PointerDialog) (dialog)).getSelectedItem();

            switch (selected) {
                case 0 : pointerList.get(index).rotateBitmap();
                    break;

                case 1 : if(index > 1 && index == pointerList.size() - 1)
                            pointerList.remove(index);
                    break;

            }
        }
        observer.update(0,0,0,0,0);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            return true;
        }
    }
}
