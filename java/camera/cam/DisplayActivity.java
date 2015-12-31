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

import camera.cam.buttons.myImageButton;
import camera.cam.dialogs.ChooseBaseDialog;
import camera.cam.dialogs.NewBaseDialog;
import camera.cam.dialogs.PointerOptionsDialog;
import camera.cam.dialogs.ChooseUnitDialog;
import camera.cam.interfaces.NoticeDialogListener;
import camera.cam.listeners.AddListener;
import camera.cam.listeners.AddNewBaseListener;
import camera.cam.listeners.SettingsListener;
import camera.cam.listeners.UnitListener;
import camera.cam.listeners.ZoomListener;

public class DisplayActivity extends Activity implements OnTouchListener, NoticeDialogListener {

	public static final String PREFS_NAME = "MyPrefsFile";
	private CustomView customView;
	public Bitmap bitmap;
	public List<Pointer> pointerList;
	public CustomView observer;

	private int baseType;
	private int unitType;
	private String unit;

	private boolean zoomed;

	private GestureDetector gestureDetector;

	private int index = 1;

	public String[] basesNames = {"Credit card(longer side)",
			                      "Credit card(shorter side)",
			                      "A4 sheet(longer side)",
								  "A4 sheet(shorter side)"};
	public float[] basesValues = {8.56F,
								  5.398F,
								29.7F,
								21.0F};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		customView = (CustomView)findViewById(R.id.image);
		customView.setActivity(this);
		//SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		//settings.edit().clear().commit();
		// Restore preferences
		//SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		/*baseType = settings.getInt("baseType", 0);
		unitType = settings.getInt("unitType", 0);
		unit = settings.getString("unit", "cm");*/

		/*Calculations.saveBaseNameArray(basesNames, "names", this);
		Calculations.saveBaseValueArray(basesValues, "values", this);

		this.basesNames = Calculations.loadBaseNameArray("names",this);
		this.basesValues = Calculations.loadBaseValueArray("values",this);*/

		this.zoomed = false;

		myImageButton unitBtn = (myImageButton)findViewById(R.id.unit_button);
		unitBtn.setStrategy(new UnitListener(this));

		myImageButton addBtn = (myImageButton)findViewById(R.id.add_button);
		addBtn.setStrategy(new AddListener(this));

		myImageButton setBtn = (myImageButton)findViewById(R.id.settings_button);
		setBtn.setStrategy(new SettingsListener(this));

		myImageButton zoomBtn = (myImageButton)findViewById(R.id.zoom_button);
		zoomBtn.setStrategy(new ZoomListener(this));

		myImageButton addBaseBtn = (myImageButton)findViewById(R.id.add_base_button);
		addBaseBtn.setStrategy(new AddNewBaseListener(this));

		Bundle bundle = getIntent().getExtras();
		Uri selectedImage = (Uri) bundle.get("imageUri");
		getContentResolver().notifyChange(selectedImage, null);

		gestureDetector = new GestureDetector(this, new SingleTapConfirm());

		customView.setOnTouchListener(this);
		observer = customView;

		pointerList = new ArrayList<>();
		pointerList.add(new BasePointer(150, 250, 50, 250, this, 0, observer,baseType));
		pointerList.add(new Pointer(50, 50, 150, 50, this, 1, observer));

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

	public CustomView getCustomView() {
		return this.customView;
	}

	public boolean getZoomed() {
		return this.zoomed;
	}

	public void setZoomed(boolean z) {
		this.zoomed = z;
	}


	@Override
	protected void onPause() {
		super.onPause();
		customView.pause();

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("pointerListSize", pointerList.size());

		for(int i = 0; i < pointerList.size(); i++){
			String name = "pointer"+String.valueOf(i);
			editor.putInt(name+"x1", (int) pointerList.get(i).getPoint1X());
			editor.putInt(name+"y1", (int) pointerList.get(i).getPoint1Y());
			editor.putInt(name+"x2", (int) pointerList.get(i).getPoint2X());
			editor.putInt(name+"y2", (int) pointerList.get(i).getPoint2Y());

		}
		// Commit the edits!
		editor.commit();

		Calculations.saveBaseNameArray(basesNames, "names", this);
		Calculations.saveBaseValueArray(basesValues, "values", this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		customView.resume();

		this.basesNames = Calculations.loadBaseNameArray("names",this);
		this.basesValues = Calculations.loadBaseValueArray("values",this);

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		baseType = settings.getInt("baseType", 0);
		unitType = settings.getInt("unitType", 0);
		unit = settings.getString("unit", "cm");


		int pointerListSize = settings.getInt("pointerListSize",1);

		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;

		if(pointerList.size() == 0) {
			for (int i = 0; i < pointerListSize; i++) {
				String name = "pointer" + String.valueOf(i);
				x1 = settings.getInt(name + "x1", x1);
				y1 = settings.getInt(name + "y1", y1);
				x2 = settings.getInt(name + "x2", x2);
				y2 = settings.getInt(name + "y2", y2);

				if (i == 0) {
					pointerList.add(new BasePointer(x1, y1, x2, y2, this, i, observer, baseType));
				} else {
					pointerList.add(new Pointer(x1, y1, x2, y2, this, i, observer));
				}

			}
		}


		observer.update(0,0,0,0,0);

	}

	@Override
	protected void onStop() {
		super.onStop();

		Calculations.saveBaseNameArray(basesNames, "names", this);
		Calculations.saveBaseValueArray(basesValues, "values", this);

		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("baseType", baseType);
		editor.putInt("unitType", unitType);
		editor.putString("unit", unit);

		// Commit the edits!
		editor.commit();


	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		try {
			Thread.sleep(50);
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
					PointerOptionsDialog dialogFragment = new PointerOptionsDialog();
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

	public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
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

		if(dialog instanceof ChooseBaseDialog) {
			this.baseType = ((ChooseBaseDialog) (dialog)).getSelectedItem();
			pointerList.set(0, new BasePointer(pointerList.get(0).getPoint1X(), pointerList.get(0).getPoint1Y(),
					pointerList.get(0).getPoint2X(), pointerList.get(0).getPoint2Y(),
					this, 0, observer, baseType));
		}

		if(dialog instanceof ChooseUnitDialog) {
			Resources res = getResources();
			String[] units = res.getStringArray(R.array.string_array_units);
			this.unitType = ((ChooseUnitDialog) (dialog)).getSelectedItem();
			this.unit = units[unitType];
		}

		if(dialog instanceof PointerOptionsDialog) {
			int selected = ((PointerOptionsDialog) (dialog)).getSelectedItem();

			switch (selected) {
				case 0 : pointerList.get(index).rotateBitmapRight();
					break;

				case 1 : pointerList.get(index).rotateBitmapLeft();
					break;

				case 2 : if(index > 1 && index == pointerList.size() - 1)
					pointerList.remove(index);
					break;

			}
		}

		if(dialog instanceof NewBaseDialog) {
			int tabSize = this.basesNames.length;
			String[] newNames = new String[tabSize+1];
			float[] newValues = new float[tabSize+1];

			for(int i = 0; i < tabSize; i++) {
				newNames[i] = this.basesNames[i];
				newValues[i] = this.basesValues[i];
			}

			newNames[tabSize] = ((NewBaseDialog) dialog).getName();
			newValues[tabSize] = ((NewBaseDialog) dialog).getLength();

			Calculations.saveBaseNameArray(newNames,"names",this.getApplicationContext());
			this.basesNames = Calculations.loadBaseNameArray("names",this.getApplicationContext());

			Calculations.saveBaseValueArray(newValues,"values",this.getApplicationContext());
			this.basesValues = Calculations.loadBaseValueArray("values",this.getApplicationContext());

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
