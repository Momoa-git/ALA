package com.example.ala.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.ala.R;

public class InternetWarningDialog extends AppCompatDialogFragment {
   // private InternetWarningDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater flater = getActivity().getLayoutInflater();
        View view = flater.inflate(R.layout.internet_warning_dialog, null);

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //listener.applyAfterCheckConnection();
                    }
                });

        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            //listener = (InternetWarningDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "nust implement dialog listener");
        }
    }

    public interface InternetWarningDialogListener{
        void applyAfterCheckConnection();
    }
}
