package camera.cam.buttons;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import camera.cam.DisplayAct;
import camera.cam.listeners.AddListener;
import camera.cam.listeners.SettingsListener;
import camera.cam.myImageButton;

public class AddButton extends myImageButton {

    private DisplayAct activity;

    public AddButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setActivity(DisplayAct act) {
        this.activity = act;
        this.onClickLst = new AddListener(activity);
    }
}
