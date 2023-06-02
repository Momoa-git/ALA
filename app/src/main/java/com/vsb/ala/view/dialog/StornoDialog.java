package com.vsb.ala.view.dialog;

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

import com.vsb.ala.R;

public class StornoDialog extends AppCompatDialogFragment {
    private StornoDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater flater = getActivity().getLayoutInflater();
        View view = flater.inflate(R.layout.layout_storno_dialog, null);

        builder.setView(view)
                .setTitle("Stornování objednávky")
                .setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Potvrdit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       listener.applyTexts2();
                    }
                });

        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (StornoDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "nust implement dialog listener");
        }
    }

    public interface StornoDialogListener{
        void applyTexts2();
    }
}
