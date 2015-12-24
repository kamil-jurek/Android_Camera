package camera.cam;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ManualActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        TextView textView= (TextView)findViewById(R.id.manualText);
    }
}
