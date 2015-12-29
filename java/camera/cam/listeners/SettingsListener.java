package camera.cam.listeners;

import android.app.Activity;
import android.view.View;

import camera.cam.dialogs.BaseDialog;
import camera.cam.interfaces.myOnClickListener;

public class SettingsListener implements myOnClickListener {

    private Activity activity;

    public SettingsListener(Activity act) {
        this.activity = act;
    }

    @Override
    public void onClick(View v) {
        BaseDialog dialogFragment = new BaseDialog();
        dialogFragment.show(activity.getFragmentManager(), "DialogFragment");
    }
}
