package camera.cam.buttons;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import camera.cam.listeners.UnitListener;

public class UnitButton extends myImageButton {

    private Activity activity;

    public UnitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setActivity(Activity act) {
        this.activity = act;
        this.onClickLst = new UnitListener(activity);
    }
}