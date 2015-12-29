package camera.cam.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import camera.cam.MainActivity;
import camera.cam.interfaces.myOnClickListener;

public class DirectoryListener implements myOnClickListener {

    private Activity activity;

    public DirectoryListener(Activity act) {
        this.activity = act;
    }
    @Override
    public void onClick(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, MainActivity.SELECT_PHOTO);
    }


}
