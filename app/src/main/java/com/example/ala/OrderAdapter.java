package com.example.ala;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private DatabaseReference databaseReference;
    ArrayList<Order> list = new ArrayList<>();
    private OrderViewHolder.OnDetailListener onDetailListener;

    public OrderAdapter(Context context, OrderViewHolder.OnDetailListener onDetailListener ) {
        this.context = context;
        this.onDetailListener = onDetailListener;
    }
    public void setItems(ArrayList<Order> order)
    {
        list.addAll(order);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_orders, parent, false);
        return new OrderViewHolder(v, onDetailListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         OrderViewHolder vh = (OrderViewHolder) holder;
         Order order= list.get(position);

         vh.order_number.setText(String.valueOf(order.getOrder_number()));
         vh.id_order = order.getId_order();

        int id_customer = order.getId_customer();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Customer").child("Customers");
        //   query = FirebaseDatabase.getInstance().getReference("Customer").child("Customers").orderByChild("id").equalTo(1);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    if(customer.getId() == id_customer)
                        vh.id_customer.setText(customer.getFname() + " " + customer.getLname());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        parseDateFromDatabase(order, vh);


        String status = order.getStatus();

        switch (status) {
            case "PE":
                vh.status.setText("Čekající");
                vh.statusImg.setImageResource(R.drawable.status_bar_pe);
                break;
            case "IP":
                vh.status.setText("Vyřizuje se");
                vh.statusImg.setImageResource(R.drawable.status_bar_ip);
                break;
            case "CO":
                vh.status.setText("Vyřízena");
                vh.statusImg.setImageResource(R.drawable.status_bar_co);
                break;
            case "CA":
                vh.status.setText("Stornována");
                vh.statusImg.setImageResource(R.drawable.status_bar_ca);
                break;

        }

    }

 /*   @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      Order order= list.get(position);
      //String orderNum = String.valueOf(order.getOrder_number());

      holder.order_number.setText(String.valueOf(order.getOrder_number()));
     //holder.id_customer.setText(String.valueOf(order.getId_customer()));
        holder.id_order = order.getId_order();

      // SELECT name FROM customers WHERE id = 1


      int id_customer = order.getId_customer();
    //  Query query;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Customer").child("Customers");
     //   query = FirebaseDatabase.getInstance().getReference("Customer").child("Customers").orderByChild("id").equalTo(1);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    if(customer.getId() == id_customer)
                        holder.id_customer.setText(customer.getFname() + " " + customer.getLname());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



       // Log.i("query", "ID: " + id_customer + "Query: " + query.toString());

      parseDateFromDatabase(order, holder);


        SimpleDateFormat database_format = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat output_format = new SimpleDateFormat("dd.MM.yyyy");

        try {
            Date date = database_format.parse(order.getDate());
            holder.date.setText(output_format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


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


    }*/

    private void parseDateFromDatabase(Order order, OrderViewHolder holder) {
        SimpleDateFormat database_format = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat output_format = new SimpleDateFormat("dd.M.yyyy");

        try {
            Date date = database_format.parse(order.getDate());
            holder.date.setText(output_format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /*public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Integer id_order;
        TextView order_number, id_customer, date, status;
        ImageView statusImg;
        OnDetailListener onDetailListener;

        public MyViewHolder(@NonNull View itemView, OnDetailListener onDetailListener) {
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
    }
    public interface OnDetailListener{
        void onDetailClick(int position);
    }*/
}
