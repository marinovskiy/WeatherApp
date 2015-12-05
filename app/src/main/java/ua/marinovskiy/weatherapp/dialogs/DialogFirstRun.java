package ua.marinovskiy.weatherapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.activities.SettingsActivity;

public class DialogFirstRun extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getActivity().getApplicationContext().
                getString(R.string.dialog_first_run_title))
                .setNegativeButton(getActivity().getApplicationContext().
                        getString(R.string.dialog_first_run_neg_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                })
                .setPositiveButton(getActivity().getApplicationContext().
                                getString(R.string.dialog_first_run_pos_btn),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity().getApplicationContext(),
                                        SettingsActivity.class);
                                startActivity(intent);
                            }
                        })
                .setMessage(getActivity().getApplicationContext().
                        getString(R.string.dialog_first_run_msg));
        return dialog.create();
    }


}
