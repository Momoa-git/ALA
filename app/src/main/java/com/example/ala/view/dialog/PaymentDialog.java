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
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PaymentDialog extends AppCompatDialogFragment {
    private PaymentDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater flater = getActivity().getLayoutInflater();
        View view = flater.inflate(R.layout.layout_payment_dialog, null);

        builder.setView(view)
                .setTitle("Dokončení objednávky")
                .setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Potvrdit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       listener.applyTexts3();
                    }
                });

        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (PaymentDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "nust implement dialog listener");
        }
    }

    public interface PaymentDialogListener{
        void applyTexts3();
    }
}
