package camera.cam;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class DisplayActivity extends Activity implements OnTouchListener {


	protected List<Pointer> pointerList = new ArrayList<>();
	private ResultDisplay observer;// = new ResultDisplay(getApplicationContext());

	Bitmap bitmap;
	ImageView imageView;

	public static int RANGE = 25;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);

		RANGE = this.dp2px(50);

		Bundle b = getIntent().getExtras();
		Uri selectedImage = (Uri) b.get("imageUri");
		getContentResolver().notifyChange(selectedImage, null);

		observer = (ResultDisplay) findViewById(R.id.text);
		imageView = (ImageView) findViewById(R.id.image_camera);

		//text.setText(calcSize());
		//text.setTextColor(Color.RED);
		//text.setTextSize(15);


		//pointerList.add(new Pointer(50, 250, 150, 250, this, 0, observer));
		//pointerList.add(new Pointer(50, 50, 150, 50, this, 1, observer));
		//pointerList.add(new Pointer(200, 50, 350, 50, this, 2, observer));

		//-------------


		try {
			/*DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int w = dm.widthPixels;
			int h = dm.heightPixels;

			float aspectRatio = bitmap.getWidth() /
					(float) bitmap.getHeight();


			Resources r = getResources();
			float px50 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
			int width = w;
			int height = Math.round(width / aspectRatio);*/

			bitmap = decodeUri(selectedImage);
			imageView.setImageBitmap(bitmap);

		} catch (Exception e) {
			System.out.println("Problem!");
		}



	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
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
					//p.move(event, this.bitmap, this.imageView);
				}

				break;
			case MotionEvent.ACTION_UP:
				for (Pointer p : pointerList) {
					p.stopMoving(event);
				}
				break;
		}

		return true;
	}

	public int dp2px(int dp) {
		Resources r = getResources();
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
	}


	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 400;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE
					|| height_tmp / 2 < REQUIRED_SIZE) {
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

	/*public Memento save(){
		return new Memento(this);
	}

	public void undoToLastSave(Object obj){
		Memento memento = (Memento) obj;


		this.pointerList=memento.list;
	}


	private class Memento{

		private ArrayList<Pointer> list;

		public Memento(DisplayActivity content){

			this.list=new ArrayList<>(pointerList);

		}

	}
	CareTaker ct= new CareTaker();
	@Override
	protected void onPause() {
		super.onPause();

		ct.save(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		ct.undo(this);
	}*/
}
