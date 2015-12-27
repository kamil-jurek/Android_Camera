package camera.cam;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

public class CameraButton extends myImageButton {
    public CameraButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Activity activity;
    private CameraListener camLst;

    public void setActivity(Activity act) {
        this.activity = act;
        camLst = new CameraListener(activity);
        this.onClickLst = camLst;
    }

    public Uri getImageUri() {
        return camLst.getImageUri();
    }
}
