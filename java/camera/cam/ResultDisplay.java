package camera.cam;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;

import camera.cam.interfaces.Observer;
import camera.cam.interfaces.Subject;

public class ResultDisplay extends TextView implements Observer {

    private ArrayList<Subject> subjects;
    private ArrayList<Points> pointers;

    public ResultDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);

        subjects = new ArrayList<>();
        pointers = new ArrayList<>();
        pointers.add(new Points(0,0,0,0));
        pointers.add(new Points(0,0,0,0));

        this.setTextColor(Color.RED);
    }


    @Override
    public void register(Subject s) {
        subjects.add(s);
    }

    @Override
    public void unregister(Subject s) {
        int subjectIndex = subjects.indexOf(s);

        subjects.remove(subjectIndex);
    }

    @Override
    public void update(int type, double x1, double y1, double x2, double y2) {
        switch (type) {
            case 0: if(pointers.size()==0) {
                        pointers.add(0, new Points(x1,y1,x2,y2));
                    } else {
                        pointers.remove(0);
                        pointers.add(0, new Points(x1,y1,x2,y2));
                    } break;

            case 1: if(pointers.size() == 1) {
                pointers.add(1, new Points(x1,y1,x2,y2));
            } else {
                pointers.remove(1);
                pointers.add(1, new Points(x1,y1,x2,y2));
            } break;

            /*case 2: if(pointers.size() >= 2) {
                        pointers.add(2, new Points(x1,y1,x2,y2));
                    } else {
                        pointers.remove(2);
                        pointers.add(2, new Points(x1,y1,x2,y2));
                    } break;*/
        }

        //if(pointers.size() == 2)
            this.setText(calcSize(type));
       // else if(pointers.size() == 3)
           // this.setText(calcSize(type-1) + "  " +calcSize(type));

    }

    private String calcSize(int type) {
        String calculatedSize;
        double C = 8.56;
        //double card = Math.sqrt(Math.pow(point4.getX()-point3.getX(), 2) + Math.pow(point4.getY()-point3.getY(), 2));
        //double obj = Math.sqrt(Math.pow(point2.getX()-point1.getX(), 2) + Math.pow(point2.getY()-point1.getY(), 2));

       // double card = Math.sqrt(Math.pow(this.cardX2-this.cardX1, 2) + Math.pow(this.cardY2-this.cardY1, 2));
        //double obj = Math.sqrt(Math.pow(X2-X1, 2) + Math.pow(Y2-Y1, 2));

        double card = Math.sqrt(Math.pow(pointers.get(0).x2-pointers.get(0).x1, 2) + Math.pow(pointers.get(0).y2-pointers.get(0).y1, 2));
        type = 1;
        double obj = Math.sqrt(Math.pow(pointers.get(type).x2-pointers.get(type).x1, 2) + Math.pow(pointers.get(type).y2-pointers.get(type).y1, 2));

        double x = (C*obj) / card;

        calculatedSize = "Size : " + String.format("%1$,.2f", x) + " cm ";
        return calculatedSize;
    }

    private class Points {
        public double x1;
        public double y1;
        public double x2;
        public double y2;

        public Points(double _x1,double _y1,double _x2,double _y2){
            x1 = _x1;
            y1 = _y1;
            x2 = _x2;
            y2 = _y2;
        }

    }
}
