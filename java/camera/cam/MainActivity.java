package camera.cam;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.net.Uri;

public class MainActivity extends Activity {

	public static final int TAKE_PICTURE = 1;
	public static final int SELECT_PHOTO = 100;
	CameraButton camBtn;
	myImageButton dirBtn;
	ManualButton manBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		//ImageButton cameraButton = (ImageButton)findViewById(R.id.button_camera);
		//cameraButton.setOnClickListener(cameraListener);

		//ImageButton directoryButton = (ImageButton)findViewById(R.id.button_directory);
		//directoryButton.setOnClickListener(directoryListener);


		camBtn = (CameraButton)findViewById(R.id.button_camera);
		camBtn.setActivity(this);
		camBtn.setOnClickListener();

		dirBtn = (myImageButton)findViewById(R.id.button_directory);
		//dirBtn.setActivity(this);
		//dirBtn.setOnClickListener();
		dirBtn.setStrategy(new DirectoryListener(this));

		manBtn = (ManualButton)findViewById(R.id.button_manual);
		manBtn.setActivity(this);
		manBtn.setOnClickListener();
	}
	
	/*private OnClickListener cameraListener = new OnClickListener() {
		public void onClick(View v) {
			//takePhoto(v);
			dispatchTakePictureIntent();
		}
	};
	
	private OnClickListener directoryListener = new OnClickListener() {
		public void onClick(View v) {
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image*//*");
			startActivityForResult(photoPickerIntent, SELECT_PHOTO);    
		}
	};

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  *//* prefix *//*
				".jpg",         *//* suffix *//*
				storageDir      *//* directory *//*
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				imageUri = Uri.fromFile(photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(takePictureIntent, TAKE_PICTURE);
			}
		}
	}*/

	private void galleryAddPic(Uri imageUri) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(imageUri);
		this.sendBroadcast(mediaScanIntent);
	}

	/*private void takePhoto(View v) {
		Intent intentPhoto = new Intent("android.media.action.IMAGE_CAPTURE");
		
		Calendar c = Calendar.getInstance();
		String fileName = "photo" + c.get(Calendar.YEAR) + "_" + 
									 c.get(Calendar.MONTH) + "_" +
									 c.get(Calendar.DAY_OF_MONTH) + "_" +
									 c.get(Calendar.HOUR_OF_DAY) + "_" +
									 c.get(Calendar.MINUTE) + "_" + 
									 c.get(Calendar.SECOND);
		String fileExtension = ".jpg";
		File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/CamPic/");

		File photo = new File(directory, fileName+fileExtension);
		
		imageUri = Uri.fromFile(photo);

		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(imageUri);
		this.sendBroadcast(mediaScanIntent);


		intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intentPhoto, TAKE_PICTURE);		
	}*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			
		if(requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
			Uri selectedImage = camBtn.getImageUri();
			galleryAddPic(selectedImage);
			Intent intentDisplay = new Intent(getApplicationContext(), DisplayAct.class);
			intentDisplay.putExtra("imageUri", selectedImage);
			
			startActivity(intentDisplay);			
		}
		
		if(requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK) {
			Uri selectedImage = data.getData();			
			Intent intentDisplay = new Intent(getApplicationContext(), DisplayAct.class);
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
