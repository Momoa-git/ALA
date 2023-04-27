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

import java.util.Objects;

public class InternetWarningDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater flater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = flater.inflate(R.layout.internet_warning_dialog, null);

        builder.setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    //listener.applyAfterCheckConnection();
                });

        return builder.create();
    }


    public interface InternetWarningDialogListener{
        void applyAfterCheckConnection();
    }
}
