package camera.cam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import camera.cam.interfaces.Observer;
import camera.cam.interfaces.Subject;

public class CustomView extends SurfaceView implements Runnable, Observer {
    private Thread thread = null;
    private SurfaceHolder holder;
    private boolean isItOK = false;
    private DisplayActivity activity;
    private ArrayList<Subject> subjects;
    protected Canvas canvas;
    private Paint paint;
    private String str = "  ";
    private boolean updated = true;

    public CustomView(Context context)
    {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setActivity(DisplayActivity a) {
        this.activity = a;
    }
    public void init() {
        subjects = new ArrayList<>();

        holder = getHolder();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(0xff00FF00);
        paint.setTextSize(22);

    }

    @Override
    public void run() {
        while(isItOK == true) {
            if(!holder.getSurface().isValid()) {
                continue;
            }

            if(updated == false) {
                continue;
            }

            canvas = holder.lockCanvas();

            int cx = (this.getWidth() - activity.bitmap.getWidth()) >> 1; // same as (...) / 2
            int cy = (this.getHeight() - activity.bitmap.getHeight()) >> 1;
            canvas.drawARGB(150, 0, 0, 0);
            canvas.drawBitmap(activity.bitmap, cx, cy, null);

            for (Pointer p : activity.pointerList) {
                p.drawPointer(canvas);

            }

            holder.unlockCanvasAndPost(canvas);
            updated = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
    }


    public void pause() {
        isItOK = false;
        while(true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        thread = null;
    }

    public void resume() {
        isItOK = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void register(Subject o) {
        subjects.add(o);

    }

    @Override
    public void unregister(Subject o) {
        int subjectIndex = subjects.indexOf(o);
        subjects.remove(subjectIndex);
    }

    @Override
    public void update(int type, double x1, double y1, double x2, double y2) {
        //str = calcSize((BasePointer)activity.pointerList.get(0), activity.pointerList.get(type),activity.getUnit());
        updated = true;
    }

    private String calcSize(BasePointer base, Pointer p, String unit) {
        String calculatedSize = " ";

        double C = base.getBaseSize();

        double bas = Math.sqrt(Math.pow(base.getPoint2X() - base.getPoint1X(), 2) +
                                Math.pow(base.getPoint2Y() - base.getPoint1Y(), 2));

        double obj = Math.sqrt(Math.pow(p.getPoint2X() - p.getPoint1X(), 2) + Math.pow(p.getPoint2Y() - p.getPoint1Y(), 2));

        double x = (C * obj) / bas;

        switch (unit) {
            case "cm" : calculatedSize = String.format("%1$,.2f", x) + " cm";

                break;
            case "mm" : calculatedSize = String.format("%1$,.2f", x*10) + " mm";
                break;

            case "inch" : calculatedSize = String.format("%1$,.2f", x*0.3937) + "inch";
                break;

            case "feet" : calculatedSize = String.format("%1$,.2f", x*0.0328) + " feet";
                break;
        }

        return calculatedSize;

    }

}