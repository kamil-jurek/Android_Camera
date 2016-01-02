package camera.cam.buttons;

import android.content.Context;
import android.util.AttributeSet;

import camera.cam.DisplayActivity;
import camera.cam.listeners.AddNewPointerListener;

public class AddButton extends myImageButton {

    private DisplayActivity activity;

    public AddButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setActivity(DisplayActivity act) {
        this.activity = act;
        this.onClickLst = new AddNewPointerListener(activity);
    }
}
