package camera.cam.listeners;

import android.app.Activity;
import android.view.View;

import camera.cam.DisplayActivity;
import camera.cam.dialogs.UnitDialog;
import camera.cam.interfaces.myOnClickListener;

public class ZoomListener implements myOnClickListener {

    private DisplayActivity activity;

    public ZoomListener(DisplayActivity act) {
        this.activity = act;
    }

    @Override
    public void onClick(View v) {
        activity.setZoomed(!activity.getZoomed());
        activity.observer.update(0,0,0,0,0);
    }
}