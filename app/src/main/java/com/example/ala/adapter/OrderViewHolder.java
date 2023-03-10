package com.example.ala.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ala.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public Integer id_order;
    public TextView order_number, id_customer, date, status;
    public ImageView statusImg;
    private OnDetailListener onDetailListener;

    public OrderViewHolder(@NonNull View itemView, OnDetailListener onDetailListener) {
        super(itemView);
        id_order = 0;
        order_number = itemView.findViewById(R.id.txt_number_order);
        id_customer = itemView.findViewById(R.id.txt_customer);
        date = itemView.findViewById(R.id.txt_date);
        status = itemView.findViewById(R.id.txt_status);
        statusImg = itemView.findViewById(R.id.img_status_bar);
        this.onDetailListener = onDetailListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onDetailListener.onDetailClick(getAdapterPosition());
    }

    public interface OnDetailListener{
        void onDetailClick(int position);
    }
}
