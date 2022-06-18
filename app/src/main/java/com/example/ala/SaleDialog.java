package com.example.ala;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SaleDialog extends AppCompatDialogFragment {
    private EditText editSaleText;
    private SaleDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater flater = getActivity().getLayoutInflater();
        View view = flater.inflate(R.layout.layout_sale_dialog, null);

        builder.setView(view)
                .setTitle("Přidání slevy")
                .setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Potvrdit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String sale = editSaleText.getText().toString();
                       listener.applyTexts(sale);
                    }
                });

        editSaleText = view.findViewById(R.id.edt_sale);
        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (SaleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "nust implement dialog listener");
        }
    }

    public interface SaleDialogListener{
        void applyTexts(String sale);
    }
}
