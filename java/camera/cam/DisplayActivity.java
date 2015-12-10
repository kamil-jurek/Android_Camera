package camera.cam;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayActivity extends Activity implements OnTouchListener {


	protected List<Pointer> pointerList = new ArrayList<Pointer>();
	//protected Pointer pointer1;
	//protected Pointer pointer2;


	Bitmap bitmap;
	ImageView imageView;
	TextView text;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);



		Bundle b = getIntent().getExtras();
		Uri selectedImage =  (Uri) b.get("imageUri");
		getContentResolver().notifyChange(selectedImage, null);
		text = (TextView)findViewById(R.id.text);
		imageView = (ImageView)findViewById(R.id.image_camera);          
		
		//text.setText(calcSize());
		text.setTextColor(Color.RED);
		//text.setTextSize(15);

		pointerList.add(new Pointer(50, 50, 150, 50, this, 1));
		pointerList.add(new Pointer(50, 250, 150, 250, this,2));

		//-------------
		
				
		
		try {
			//bitmap = resizeBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage), 700, 400);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int w = dm.widthPixels;
			int h = dm.heightPixels;

			bitmap = decodeUri(selectedImage);
			float aspectRatio = bitmap.getWidth() /
					(float) bitmap.getHeight();


			Resources r = getResources();
			float px50 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
			int width = w-50;
			int height = Math.round(width / aspectRatio);

			bitmap = Bitmap.createScaledBitmap(
					bitmap, width, height, false);


			imageView.setImageBitmap(bitmap);	

		} catch(Exception e) {
			System.out.println("Problemo!");
		}
		
		
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			for(Pointer p : pointerList) {
				p.checkMoving(event);
			}

			break;
			
		case MotionEvent.ACTION_MOVE:
			for(Pointer p : pointerList) {
				p.move(event, this.bitmap, this.imageView);
			}

			text.setText(pointerList.get(1).calcSize(pointerList.get(0)));
			break;
		case MotionEvent.ACTION_UP:
			for(Pointer p : pointerList) {
				p.stopMoving(event);
			}
			break;
		}
		
		return true;
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
	

	

	
	private Bitmap resizeBitmap(Bitmap originalImage, double width, double height) {
		Bitmap background = Bitmap.createBitmap((int)width, (int)height, Config.ARGB_8888);
		float originalWidth = originalImage.getWidth(), originalHeight = originalImage.getHeight();
		Canvas canvas = new Canvas(background);
		float scale = (float) (width/originalWidth);
		float xTranslation = 0.0f, yTranslation = (float) ((height - originalHeight * scale)/2.0f);
		Matrix transformation = new Matrix();
		transformation.postTranslate(xTranslation, yTranslation);
		transformation.preScale(scale, scale);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		canvas.drawBitmap(originalImage, transformation, paint);
		
		return background;
	}
	
}
