package camera.cam;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class Pointer implements Subject  {
    private Point point1 = new Point();
    private Point point2 = new Point();
    private Boolean moving1 = false;
    private Boolean moving2 = false;
    private Paint paint;


    private Observer observer;

    private Bitmap bPoint1;
    private Bitmap bPoint2;

    int type;
    public int RANGE = 100;
    //private int pH;
    //private int pW;

    public Activity activity;

    public Pointer(float x1, float y1, float x2, float y2, DisplayAct _activity, int _type, Observer obs) {
        observer = obs;
        observer.register(this);

        this.point1.set(x1, y1);
        this.point2.set(x2, y2);

        type = _type;

        this.activity = _activity;
        this.RANGE = dp2px(50);

        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setTextSize(RANGE/2);

        switch (type) {

            case 0 :
                bPoint1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point0);
                bPoint2 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point0);
                paint.setColor(0xff00FF00);
                break;
            case 1 :
                bPoint1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point1);
                bPoint2 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point1);
                paint.setColor(0xffff0000);
                break;
            case 2 :
                bPoint1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point2);
                bPoint2 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point2);
                paint.setColor(0xff0000FF);
                break;
            case 3 :
                bPoint1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point3);
                bPoint2 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point3);
                paint.setColor(0xff0000FF);
                break;
            case 4 :
                bPoint1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point4);
                bPoint2 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point4);
                paint.setColor(0xff00FFFF);
                break;
            case 5 :
                bPoint1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point5);
                bPoint2 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point5);
                paint.setColor(0xffFF00FF);
                break;
        }
            bPoint1 = Bitmap.createScaledBitmap(bPoint1, RANGE, RANGE, false);
            bPoint2 = Bitmap.createScaledBitmap(bPoint2, RANGE, RANGE, false);

            System.out.println(RANGE);
            System.out.println(bPoint1.getHeight());
            System.out.println(bPoint1.getWidth());

            notifyObserver();
    }

    public int dp2px(int dp) {
        Resources r = activity.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public Paint getPaint() {
        return  this.paint;
    }

    public int getType() {
        return this.type;
    }

    public boolean checkMoving(MotionEvent event) {
        if(event.getRawX() > point1.x + RANGE/2 - RANGE/2 &&
           event.getRawX() < point1.x + RANGE/2 + RANGE/2 &&
           event.getRawY() > point1.y + RANGE/2 - RANGE/2 &&
           event.getRawY() < point1.y + RANGE/2 + RANGE/2) {
                moving1 = true;
                moving2 = false;

                return true;
        }

        else if(event.getRawX() > point2.x + RANGE/2- RANGE/2 &&
                event.getRawX() < point2.x + RANGE/2 + RANGE/2 &&
                event.getRawY() > point2.y + RANGE/2- RANGE/2 &&
                event.getRawY() < point2.y + RANGE/2 + RANGE/2) {
                    moving1 = false;
                    moving2 = true;

                    return true;
        }
        else return false;
    }

    public void move(MotionEvent event) {
        if (moving1) {
            point1.x = event.getRawX() - RANGE / 2;
            point1.y = event.getRawY() - RANGE / 2;

            notifyObserver();
        }

       else if (moving2) {
            point2.x = event.getRawX() - RANGE / 2;
            point2.y = event.getRawY() - RANGE / 2;

            notifyObserver();
        }
    }

    public void stopMoving(MotionEvent event) {
                moving1 = false;
                moving2 = false;
    }

    public int getRange() {
        return this.RANGE;
    }

    public Bitmap getPoint1Bitmap() {
        return this.bPoint1;
    }

    public Bitmap getPoint2Bitmap() {
        return this.bPoint2;
    }

    public float getPoint1X() {
        return point1.x;
    }

    public float getPoint1Y() {
        return point1.y;
    }

    public float getPoint2X() {
        return point2.x;
    }

    public float getPoint2Y() {
        return point2.y;
    }

    @Override
    public void notifyObserver() {
        observer.update(type, point1.x,point1.y,point2.x,point2.y);

    }

    private class Point {
        public float x;
        public float y;

        public void set(float _x, float _y) {
            x = _x;
            y = _y;
        }
    }
}
