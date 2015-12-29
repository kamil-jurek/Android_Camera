package camera.cam;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import camera.cam.interfaces.Statelike;

public class StateBottom implements Statelike {
    @Override
    public void drawPointer(Canvas canvas, Pointer pointer) {
        canvas.drawBitmap(pointer.getPoint1Bitmap(), pointer.getPoint1X(), pointer.getPoint1Y(), null);
        canvas.drawBitmap(pointer.getPoint2Bitmap(), pointer.getPoint2X(), pointer.getPoint2Y(), null);

        float x1,x2,y1,y2;
        x1 = pointer.getPoint1X() + pointer.getRange()/2;
        y1 = pointer.getPoint1Y() + pointer.RANGE/20;
        x2 = pointer.getPoint2X() + pointer.getRange()/2;
        y2 = pointer.getPoint2Y() + pointer.RANGE/20;

        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);

        canvas.drawPath(path, pointer.getPaint());

        if(pointer.getType()==0) {
            // canvas.drawTextOnPath("karta kredytowa", pa, (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))/2-80,-5,paint);
        } else {
            canvas.drawTextOnPath(Calculations.calculateSize((BasePointer) (pointer.activity.pointerList.get(0)),
                            pointer, pointer.activity.getUnit()), path,
                    (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))/2-60,-5,pointer.getPaint());

        }

        if(pointer.activity.getZoomed() && (pointer.getMoving1() || pointer.getMoving2())) {
            Matrix matrix = new Matrix();
            matrix.postScale(2, 2);
            Bitmap b = Calculations.addWhiteBorder(pointer.activity.bitmap, pointer.getRange() / 2);

            Bitmap resizedBitmap = Bitmap.createBitmap(b,
                    (int) (pointer.getPoint1X()),
                    (int) (pointer.getPoint1Y() + pointer.getRange()), pointer.getRange(), pointer.getRange(), matrix, true);;
            if (pointer.getMoving1()) {
                resizedBitmap = Bitmap.createBitmap(b,
                        (int) (pointer.getPoint1X()),
                        (int) (pointer.getPoint1Y() ), pointer.getRange(), pointer.getRange(), matrix, true);
            } else if (pointer.getMoving2()) {
                resizedBitmap = Bitmap.createBitmap(b,
                        (int) (pointer.getPoint2X()),
                        (int) (pointer.getPoint2Y()), pointer.getRange(), pointer.getRange(), matrix, true);
            }

            canvas.drawBitmap(resizedBitmap, pointer.activity.getCustomView().getWidth() - resizedBitmap.getWidth(), pointer.activity.getCustomView().getHeight() - resizedBitmap.getHeight(), null);

            pointer.getPaint().setStyle(Paint.Style.STROKE);
            pointer.getPaint().setStrokeWidth(pointer.RANGE / 30);

            float zx = pointer.activity.getCustomView().getWidth() - (resizedBitmap.getWidth()) / 2;
            float zy = pointer.activity.getCustomView().getHeight() - (resizedBitmap.getHeight()) / 2;
            Path pa = new Path();
            pa.moveTo(zx - pointer.getRange() / 3, zy);
            pa.lineTo(zx + pointer.getRange() / 3, zy);
            pa.moveTo(zx, zy - pointer.getRange() / 3);
            pa.lineTo(zx, zy + pointer.getRange() / 3);

            canvas.drawPath(pa, pointer.getPaint());

            pointer.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
            pointer.getPaint().setStrokeWidth(pointer.RANGE / 30);
        }
    }

    @Override
    public void setNewCoordinates(Pointer pointer, MotionEvent event) {
        if (pointer.getMoving1()) {
            if(pointer.activity.getZoomed() == false) {
                pointer.setPoint1(event.getRawX()- pointer.getRange()/2, event.getRawY() - pointer.getRange());
                pointer.notifyObserver();
            } else {
                float x1 = pointer.getPoint1X();
                float y1 = pointer.getPoint1Y();

                float x = event.getRawX();
                float y = event.getRawY();

                float nx = (x1 + x)/2;
                float ny = (y1 + y)/2;
                //float nx = (nx1 + x1)/2;
                //float ny = (ny1 + y1) / 2;

                pointer.setPoint1(nx - pointer.getRange() / 2, ny - pointer.getRange());
                pointer.notifyObserver();
            }
        }

        else if (pointer.getMoving2()) {
            if(pointer.activity.getZoomed() == false) {
                pointer.setPoint2(event.getRawX() - pointer.getRange() / 2, event.getRawY() - pointer.getRange());
                pointer.notifyObserver();
            } else {

                float x2 = pointer.getPoint2X();
                float y2 = pointer.getPoint2Y();

                float x = event.getRawX();
                float y = event.getRawY();

                float nx = (x2 + x)/2;
                float ny = (y2 + y)/2;
                //float nx = (nx1 + x2)/2;
                //float ny = (ny1 + y2) / 2;

                pointer.setPoint2(nx - pointer.getRange() / 2, ny - pointer.getRange());
                pointer.notifyObserver();
            }
        }
    }

}


