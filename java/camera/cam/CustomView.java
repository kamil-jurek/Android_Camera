package camera.cam;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class CustomView extends SurfaceView implements Runnable, Observer {
    private Thread thread = null;
    private SurfaceHolder holder;
    private boolean isItOK = false;
    private DisplayAct activity;
    private ArrayList<Subject> subjects;
    protected Canvas canvas;
    private Paint paint;
    private String str = " elo";
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

    public void setActivity(DisplayAct a) {
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

            canvas = holder.lockCanvas();

            int cx = (this.getWidth() - activity.bitmap.getWidth()) >> 1; // same as (...) / 2
            int cy = (this.getHeight() - activity.bitmap.getHeight()) >> 1;
            canvas.drawARGB(150, 0, 0, 0);
            canvas.drawBitmap(activity.bitmap,cx,cy,null);

            for (Pointer p : activity.pointerList) {
                canvas.drawBitmap(p.getPoint1Bitmap(), p.getPoint1X(), p.getPoint1Y(), null);
                canvas.drawBitmap(p.getPoint2Bitmap(), p.getPoint2X(), p.getPoint2Y(), null);

                float x1 = p.getPoint1X() + p.getRange() / 2;
                float y1 = p.getPoint1Y() + p.getRange() - p.RANGE/20;

                float x2 = p.getPoint2X() + p.getRange() / 2;
                float y2 = p.getPoint2Y() + p.getRange() - p.RANGE/20;

                Path pa = new Path();
                pa.moveTo(x1, y1);
                pa.lineTo(x2, y2);
                //canvas.drawCircle(p.getPoint1X()+p.RANGE/2,p.getPoint1Y()+p.RANGE/2,p.RANGE/2,p.getPaint());
                canvas.drawPath(pa, p.getPaint());
                //canvas.drawLine(p.getPoint1X() + p.getRange() / 2, p.getPoint1Y() + p.getRange() - 3, p.getPoint2X() + p.getRange() / 2, p.getPoint2Y() + p.getRange()-3,p.getPaint());
                if(p.getType()==0) {
                    // canvas.drawTextOnPath("karta kredytowa", pa, (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))/2-80,-5,paint);
                } else {
                    canvas.drawTextOnPath(calcSize((BasePointer)(activity.pointerList.get(0)),activity.getUnit()), pa, (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))/2-60,-5,p.getPaint());
                }
            }

            //canvas.drawBitmap(activity.pointer,activity.x-activity.pointer.getWidth()/2,activity.y-activity.pointer.getHeight()/2,null);

            holder.unlockCanvasAndPost(canvas);

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
       // str = calcSize(type);
        //updated = true;
    }

    private String calcSize(BasePointer base, String unit) {
        String calculatedSize = " ";

        double C = base.getBaseSize();
        //double card = Math.sqrt(Math.pow(point4.getX()-point3.getX(), 2) + Math.pow(point4.getY()-point3.getY(), 2));
        //double obj = Math.sqrt(Math.pow(point2.getX()-point1.getX(), 2) + Math.pow(point2.getY()-point1.getY(), 2));

        // double card = Math.sqrt(Math.pow(this.cardX2-this.cardX1, 2) + Math.pow(this.cardY2-this.cardY1, 2));
        //double obj = Math.sqrt(Math.pow(X2-X1, 2) + Math.pow(Y2-Y1, 2));

        double bas = Math.sqrt(Math.pow(base.getPoint2X() - base.getPoint1X(), 2) +
                                Math.pow(base.getPoint2Y() - base.getPoint1Y(), 2));

        double obj = Math.sqrt(Math.pow(activity.pointerList.get(1).getPoint2X() - activity.pointerList.get(1).getPoint1X(), 2) +
                               Math.pow(activity.pointerList.get(1).getPoint2Y() - activity.pointerList.get(1).getPoint1Y(), 2));

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