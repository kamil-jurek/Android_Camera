package camera.cam.listeners;

import android.app.Activity;
import android.view.View;

import camera.cam.dialogs.NewBaseDialog;
import camera.cam.interfaces.myOnClickListener;

public class AddNewBaseListener implements myOnClickListener {

    private Activity activity;

    public AddNewBaseListener(Activity act) {
        this.activity = act;
    }

    @Override
    public void onClick(View v) {
        NewBaseDialog dialogFragment = new NewBaseDialog();
        dialogFragment.show(activity.getFragmentManager(), "DialogFragment");
    }
}

