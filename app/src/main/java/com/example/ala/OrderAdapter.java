package com.example.ala;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.ala.DAO.CustomerDAO;
import com.example.ala.model.object.Customer;
import com.example.ala.model.object.Order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends FirebaseRecyclerAdapter<Order,OrderViewHolder> {
    private OrderViewHolder.OnDetailListener onDetailListener;
    private List<Order> orderFull;
    private FirebaseRecyclerOptions<Order> list;


    public OrderAdapter(@NonNull FirebaseRecyclerOptions<Order> options, ArrayList<Order> list, OrderViewHolder.OnDetailListener onDetailListener) {
        super(options);
        this.onDetailListener = onDetailListener;
        this.list = options;
    }



  @Override
  protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, Order order) {
    //   Order order1= list.get(i);
      orderViewHolder.order_number.setText(String.valueOf(order.getOrder_number()));
      orderViewHolder.id_order = order.getId_order();
      Log.i("testt","HOLDER " + order.getOrder_number());
      int id_customer = order.getId_customer();

      CustomerDAO customerDAO = new CustomerDAO();

      customerDAO.get().addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              for (DataSnapshot dataSnapshot : snapshot.getChildren())
              {
                  Customer customer = dataSnapshot.getValue(Customer.class);
                  if(customer.getId() == id_customer)
                      orderViewHolder.id_customer.setText(customer.getFname() + " " + customer.getLname());
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

      parseDateFromDatabase(order, orderViewHolder);
      String status = order.getStatus();

      switch (status) {
          case "PE":
              orderViewHolder.status.setText("Čekající");
              orderViewHolder.statusImg.setImageResource(R.drawable.status_bar_pe);
              break;
          case "IP":
              orderViewHolder.status.setText("Vyřizuje se");
              orderViewHolder.statusImg.setImageResource(R.drawable.status_bar_ip);
              break;
          case "CO":
              orderViewHolder.status.setText("Vyřízena");
              orderViewHolder.statusImg.setImageResource(R.drawable.status_bar_co);
              break;
          case "CA":
              orderViewHolder.status.setText("Stornována");
              orderViewHolder.statusImg.setImageResource(R.drawable.status_bar_ca);
              break;

      }


  }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_orders, parent, false);
        return new OrderViewHolder(v, onDetailListener);
    }

    public void filterList(FirebaseRecyclerOptions<Order> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

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
}
