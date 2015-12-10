package camera.cam;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.view.Menu;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
		
	private static String logtag = "CameraApp";
	private static final int TAKE_PICTURE = 1;
	private static final int SELECT_PHOTO = 100;
	private Uri imageUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		Button cameraButton = (Button)findViewById(R.id.button_camera);
		cameraButton.setOnClickListener(cameraListener);
		
		Button directoryButton = (Button)findViewById(R.id.button_directory);
		directoryButton.setOnClickListener(directoryListener);
	}
	
	private OnClickListener cameraListener = new OnClickListener() {
		public void onClick(View v) {
			takePhoto(v);
		}
	};
	
	private OnClickListener directoryListener = new OnClickListener() {
		public void onClick(View v) {
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, SELECT_PHOTO);    
		}
	};
	
	private void takePhoto(View v) {
		Intent intentPhoto = new Intent("android.media.action.IMAGE_CAPTURE");
		
		Calendar c = Calendar.getInstance();
		String fileName = "photo" + c.get(Calendar.YEAR) + "_" + 
									 c.get(Calendar.MONTH) + "_" +
									 c.get(Calendar.DAY_OF_MONTH) + "_" +
									 c.get(Calendar.HOUR_OF_DAY) + "_" +
									 c.get(Calendar.MINUTE) + "_" + 
									 c.get(Calendar.SECOND);
		String fileExtension = ".jpg";
		File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		
		File photo = new File(directory, fileName+fileExtension);
		
		imageUri = Uri.fromFile(photo);
		
		intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intentPhoto, TAKE_PICTURE);		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			
		if(requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
			Intent intentDisplay = new Intent(getApplicationContext(), DisplayActivity.class);
			intentDisplay.putExtra("imageUri", imageUri);		
			
			startActivity(intentDisplay);			
		}
		
		if(requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK) {
			Uri selectedImage = data.getData();			
			Intent intentDisplay = new Intent(getApplicationContext(), DisplayActivity.class);
			intentDisplay.putExtra("imageUri", selectedImage);		
			
			startActivity(intentDisplay);			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	

}
