package camera.cam.listeners;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import camera.cam.DisplayActivity;
import camera.cam.Pointer;
import camera.cam.interfaces.myOnClickListener;

public class AddListener implements myOnClickListener {

    private DisplayActivity activity;

    public AddListener(DisplayActivity act) {
        this.activity = act;
    }

    @Override
    public void onClick(View v) {
        if(activity.pointerList.size() <= 5) {
            Pointer prev = activity.pointerList.get(activity.pointerList.size()-1);
            activity.pointerList.add(new Pointer(prev.getPoint1X(),prev.getPoint1Y()+prev.getRange(),
                                                prev.getPoint2X(),prev.getPoint2Y()+prev.getRange(),
                                                activity,prev.getType()+1,activity.observer));

            activity.observer.update(0,0,0,0,0);
        } else {
            Context context = activity.getApplicationContext();
            CharSequence text = "Too many pointers";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}

