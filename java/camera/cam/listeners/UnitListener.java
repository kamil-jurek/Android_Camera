package camera.cam.listeners;

import android.app.Activity;
import android.view.View;

import camera.cam.dialogs.ChooseUnitDialog;
import camera.cam.interfaces.myOnClickListener;

public class UnitListener implements myOnClickListener {

    private Activity activity;

    public UnitListener(Activity act) {
        this.activity = act;
    }

    @Override
    public void onClick(View v) {
        ChooseUnitDialog dialogFragment = new ChooseUnitDialog();
        dialogFragment.show(activity.getFragmentManager(), "DialogFragment");
    }
}

