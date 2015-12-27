package camera.cam;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class myImageButton extends ImageButton{

    protected myOnClickListener onClickLst;

    public myImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStrategy(myOnClickListener strategy) {
        this.onClickLst = strategy;
        setOnClickListener();
    }

    public void setOnClickListener() {
        buttonEffect(this);
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLst.onClick(v);
            }
        });
    }

    public static void buttonEffect(View button){
        button.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
