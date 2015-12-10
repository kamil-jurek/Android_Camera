package camera.cam;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class Pointer  {
    private Point point1 = new Point();
    private Point point2 = new Point();
    private Boolean moving1 = false;
    private Boolean moving2 = false;

    private ImageView ivPoint1;
    private ImageView ivPoint2;

    private static final int RANGE = 75;

    public Activity activity;

    public Pointer(float x1, float y1, float x2, float y2, DisplayActivity _activity, int n) {
        this.point1.set(x1, y1);
        this.point2.set(x2, y2);

        this.activity = _activity;

        if(n > 1) {
            ivPoint1 = (ImageView) this.activity.findViewById(R.id.image_point_3);
            ivPoint2 = (ImageView) this.activity.findViewById(R.id.image_point_4);
        } else {
            ivPoint1 = (ImageView) this.activity.findViewById(R.id.image_point_1);
            ivPoint2 = (ImageView) this.activity.findViewById(R.id.image_point_2);
        }

        ivPoint1.setOnTouchListener((View.OnTouchListener) this.activity);
        ivPoint2.setOnTouchListener((View.OnTouchListener) this.activity);

        ivPoint1.setX(x1);
        ivPoint1.setY(y1);

        ivPoint2.setX(x2);
        ivPoint2.setY(y2);


    }

    public void checkMoving(MotionEvent event) {
        if(event.getRawX() > ivPoint1.getX() - RANGE &&
                event.getRawX() < ivPoint1.getX() + RANGE &&
                event.getRawY() > ivPoint1.getY() - RANGE &&
                event.getRawY() < ivPoint1.getY() + RANGE) {
                    moving1 = true;
                    moving2 = false;
                }

       else if(event.getRawX() > ivPoint2.getX() - RANGE &&
                event.getRawX() < ivPoint2.getX() + RANGE &&
                event.getRawY() > ivPoint2.getY() - RANGE &&
                event.getRawY() < ivPoint2.getY() + RANGE) {
                    moving1 = false;
                    moving2 = true;
                }
    }

    public void move(MotionEvent event, Bitmap b, ImageView iv) {
        if (moving1) {
            point1.x = event.getRawX() - ivPoint1.getWidth() / 2;
            point1.y = event.getRawY() - ivPoint1.getHeight() / 2;

            ivPoint1.setX(point1.x);
            ivPoint1.setY(point1.y);

            this.drawLine(b, iv);
        }

        if (moving2) {
            point2.x = event.getRawX() - ivPoint2.getWidth() / 2;
            point2.y = event.getRawY() - ivPoint2.getHeight() / 2;

            ivPoint2.setX(point2.x);
            ivPoint2.setY(point2.y);

            this.drawLine(b, iv);
        }
    }

    public void stopMoving(MotionEvent event) {
                moving1 = false;
                moving2 = false;
    }

    private float getPoint1X() {
        return point1.x;
    }
    private float getPoint1Y() {
        return point1.y;
    }

    private float getPoint2X() {
        return point2.x;
    }
    private float getPoint2Y() {
        return point2.y;
    }

    public String calcSize(Pointer p) {
        String calculatedSize;
        double C = 8.56;
        //double card = Math.sqrt(Math.pow(point4.getX()-point3.getX(), 2) + Math.pow(point4.getY()-point3.getY(), 2));
        //double obj = Math.sqrt(Math.pow(point2.getX()-point1.getX(), 2) + Math.pow(point2.getY()-point1.getY(), 2));

        double card = Math.sqrt(Math.pow(this.getPoint2X()-this.getPoint1X(), 2) + Math.pow(this.getPoint2Y()-this.getPoint1Y(), 2));
        double obj = Math.sqrt(Math.pow(p.getPoint2X()-p.getPoint1X(), 2) + Math.pow(p.getPoint2Y()-p.getPoint1Y(), 2));

        double x = (C*obj) / card;

        calculatedSize = "Size : " + String.format("%1$,.2f", x) + " cm ";
        return calculatedSize;
    }

    private void drawLine(Bitmap _bitmap, ImageView _imageView) {
        //Create a new image bitmap and attach a brand new canvas to it


        Bitmap tempBitmap = Bitmap.createBitmap(_bitmap.getWidth(), _bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);

        //Draw the image bitmap into the cavas
        tempCanvas.drawBitmap(_bitmap, 0, 0, null);

        //Draw everything else you want into the canvas, in this example a rectangle with rounded edges
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources res = activity.getResources();
        int col = res.getColor(android.R.color.holo_blue_dark);


        Resources r = activity.getResources();
        float px50 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        paint.setColor(col);
        tempCanvas.drawLine(point1.x + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics()),
                            point1.y + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics()),
                            point2.x + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics()),
                            point2.y + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics()), paint);


        //Attach the canvas to the ImageView
        _imageView.setImageDrawable(new BitmapDrawable(activity.getResources(), tempBitmap));
        //imageView.setImageBitmap(tempBitmap);
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
