package camera.cam;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

public class ManualButton extends myImageButton {
    public ManualButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Activity activity;

    public void setActivity(Activity act) {
        this.activity = act;
        this.onClickLst = new ManualListener(activity);
    }
}

