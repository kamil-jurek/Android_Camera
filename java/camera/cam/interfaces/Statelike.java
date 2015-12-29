package camera.cam.interfaces;

import android.graphics.Canvas;
import android.view.MotionEvent;

import camera.cam.Pointer;

public interface Statelike {
    public void drawPointer(Canvas canvas, Pointer pointer);
    public void setNewCoordinates(Pointer pointer, MotionEvent event);

}
