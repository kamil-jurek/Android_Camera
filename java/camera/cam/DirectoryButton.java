package camera.cam;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

public class DirectoryButton extends myImageButton {
    public DirectoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    private Activity activity;

    public void setActivity(Activity act) {
        this.activity = act;
        this.onClickLst = new DirectoryListener(activity);
    }
}
