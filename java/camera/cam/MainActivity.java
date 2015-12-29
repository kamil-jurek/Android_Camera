package camera.cam;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.net.Uri;

import camera.cam.buttons.myImageButton;
import camera.cam.listeners.CameraListener;
import camera.cam.listeners.DirectoryListener;
import camera.cam.listeners.ManualListener;

public class MainActivity extends Activity {

	public static final int TAKE_PICTURE = 1;
	public static final int SELECT_PHOTO = 100;
	myImageButton camBtn;
	myImageButton dirBtn;
	myImageButton manBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		

		camBtn = (myImageButton)findViewById(R.id.button_camera);
		camBtn.setStrategy(new CameraListener(this));

		dirBtn = (myImageButton)findViewById(R.id.button_directory);
		dirBtn.setStrategy(new DirectoryListener(this));

		manBtn = (myImageButton)findViewById(R.id.button_manual);
		manBtn.setStrategy(new ManualListener(this));
	}

	private void galleryAddPic(Uri imageUri) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(imageUri);
		this.sendBroadcast(mediaScanIntent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			
		if(requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
			Uri selectedImage = ((CameraListener)(camBtn.getStrategy())).getImageUri();
			galleryAddPic(selectedImage);
			Intent intentDisplay = new Intent(getApplicationContext(), DisplayActivity.class);
			intentDisplay.putExtra("imageUri", selectedImage);
			
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
