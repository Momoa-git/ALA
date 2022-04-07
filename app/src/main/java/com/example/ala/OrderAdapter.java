package com.example.ala;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ala.Inventory.Status;
import com.example.ala.Inventory.StatusData;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    Context context;

    ArrayList<Order> list;

    public OrderAdapter(Context context, ArrayList<Order> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_orders, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      Order order= list.get(position);
      //String orderNum = String.valueOf(order.getOrder_number());

      holder.order_number.setText(String.valueOf(order.getOrder_number()));
      holder.id_customer.setText(String.valueOf(order.getId_customer()));
      holder.date.setText(order.getDate());

      String status = order.getStatus();

        switch (status) {
            case "PE":
                holder.status.setText("Čekající");
                holder.statusImg.setImageResource(R.drawable.status_bar_pe);
                break;
            case "IP":
                holder.status.setText("Vyřizuje se");
                holder.statusImg.setImageResource(R.drawable.status_bar_ip);
                break;
            case "CO":
                holder.status.setText("Vyřízena");
                holder.statusImg.setImageResource(R.drawable.status_bar_co);
                break;
            case "CA":
                holder.status.setText("Stornována");
                holder.statusImg.setImageResource(R.drawable.status_bar_ca);
                break;

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView order_number, id_customer, date, status;
        ImageView statusImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            order_number = itemView.findViewById(R.id.txt_number_order);
            id_customer = itemView.findViewById(R.id.txt_customer);
            date = itemView.findViewById(R.id.txt_date);
            status = itemView.findViewById(R.id.txt_status);
            statusImg = itemView.findViewById(R.id.img_status_bar);
        }
    }
}
