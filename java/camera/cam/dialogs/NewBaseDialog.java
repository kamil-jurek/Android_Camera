package camera.cam.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import camera.cam.Calculations;
import camera.cam.DisplayActivity;
import camera.cam.R;
import camera.cam.interfaces.NoticeDialogListener;

public class NewBaseDialog extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */


    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;


    private EditText name;
    private EditText length;
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;

        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.add_new_base, null);

        name = (EditText)dialoglayout.findViewById(R.id.base_name);
        length = (EditText)dialoglayout.findViewById(R.id.base_length);

        builder.setView(dialoglayout)
            .setTitle("New reference object:")
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //((DisplayActivity)(mListener)).basesNames;

                        String[] newNames = new String[4];
                        float[] newValues = new float[4];

                        for (int i = 0; i < 4; i++) {
                            newNames[i] = ((DisplayActivity)(mListener)).basesNames[i];
                            newValues[i] = ((DisplayActivity)(mListener)).basesValues[i];
                        }

                        Calculations.deleteSharedPreferences("bases","names", ((DisplayActivity)(mListener)).getApplicationContext());
                        Calculations.deleteSharedPreferences("bases","values,", ((DisplayActivity)(mListener)).getApplicationContext());

                        Calculations.saveBaseNameArray(newNames, "names",
                                ((DisplayActivity)(mListener)).getApplicationContext());
                        ((DisplayActivity)(mListener)).basesNames = Calculations.loadBaseNameArray("names",
                                ((DisplayActivity) (mListener)).getApplicationContext());

                        Calculations.saveBaseValueArray(newValues, "values",
                                ((DisplayActivity) (mListener)).getApplicationContext());
                        ((DisplayActivity)(mListener)).basesValues = Calculations.loadBaseValueArray("values",
                                ((DisplayActivity) (mListener)).getApplicationContext());
                    }
                })
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        mListener.onDialogPositiveClick(NewBaseDialog.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        mListener.onDialogNegativeClick(NewBaseDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public String getName() {
        return this.name.getText().toString();
    }

    public float getLength() {
        return Float.valueOf(this.length.getText().toString());
    }
}