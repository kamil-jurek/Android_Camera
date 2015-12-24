package camera.cam.buttons;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import camera.cam.listeners.SettingsListener;
import camera.cam.myImageButton;

public class SettingsButton extends myImageButton {

    private Activity activity;

    public SettingsButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setActivity(Activity act) {
            this.activity = act;
            this.onClickLst = new SettingsListener(activity);
    }
}
