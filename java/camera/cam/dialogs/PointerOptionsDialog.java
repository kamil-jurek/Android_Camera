package camera.cam.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import camera.cam.R;
import camera.cam.interfaces.NoticeDialogListener;

public class PointerOptionsDialog extends DialogFragment {

    private int mSelectedItem;
    private Button rotateRightBtn;
    private Button rotateLeftBtn;
    private Button deleteBtn;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */


    // Use this instance of the intAerface to deliver action events
    NoticeDialogListener mListener;

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
        mSelectedItem = 0;
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.pointer_options, null);

        rotateRightBtn = (Button)dialoglayout.findViewById(R.id.rotate_right);
        rotateLeftBtn = (Button)dialoglayout.findViewById(R.id.rotate_left);
        deleteBtn = (Button)dialoglayout.findViewById(R.id.delete_pointer);

        builder.setView(dialoglayout);
        rotateRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem = 0;
                mListener.onDialogPositiveClick(PointerOptionsDialog.this);
                dismiss();
            }
        });

        rotateLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem = 1;
                mListener.onDialogPositiveClick(PointerOptionsDialog.this);
                dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem = 2;
                mListener.onDialogPositiveClick(PointerOptionsDialog.this);
                dismiss();
            }
        });
        /*builder.setItems(R.array.string_array_pointer_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                mSelectedItem = which;
                mListener.onDialogPositiveClick(PointerOptionsDialog.this);
            }
        });*/


        // Create the AlertDialog object and return it
        return builder.create();
    }

    public int getSelectedItem() {
        return this.mSelectedItem;
    }
}
