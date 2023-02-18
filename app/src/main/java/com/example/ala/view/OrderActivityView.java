package com.example.ala.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ala.Inventory.StatusData;
import com.example.ala.model.object.Order;
import com.example.ala.OrderAdapter;
import com.example.ala.DAO.OrderDAO;
import com.example.ala.OrderViewHolder;
import com.example.ala.R;
import com.example.ala.view.dialog.PaymentDialog;
import com.example.ala.view.dialog.SaleDialog;
import com.example.ala.StatusAdapter;
import com.example.ala.controller.OrderActivityController;
import com.example.ala.view.dialog.StornoDialog;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class OrderActivityView extends AppCompatActivity implements OrderViewHolder.OnDetailListener, SaleDialog.SaleDialogListener, StornoDialog.StornoDialogListener, PaymentDialog.PaymentDialogListener {

    OrderActivityController controller;
    OrderDAO dao;

    private Spinner spinner_status;
    private StatusAdapter statAdapter;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    public OrderAdapter adapter;
    public ArrayList<Order> list;
    public TextView numberOrder, register_num, txt_date_order, txt_status, txt_type_payment, txt_paid, txt_price, txt_name_customer,txt_email_customer,
            txt_phone_customer, txt_offic_address,txt_office_name,txt_name_product, txt_discount, txt_date_pay, title_date_pay, title_locate,
            txt_locate, txt_date_locate, title_registr_num, txt_register_num, txt_description;
    public EditText edT_search;
    public ImageView img_status_bar;
    public Button btn_payment, btn_storno, btn_edit_sale;
    public BottomSheetDialog bottomSheetDialog;
    Context context;
    FirebaseRecyclerOptions<Order> options;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    int i = 0;
    //TODO fce filter orders by word search or status
    //TODO adding count  product

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        controller = new OrderActivityController(this);

        context = this;

        recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        edT_search = findViewById(R.id.edT_search);
        progressBar = findViewById(R.id.progress_bar);
        spinner_status = findViewById(R.id.spinner_filter);
        statAdapter = new StatusAdapter(OrderActivityView.this, StatusData.getStatusList());
        spinner_status.setAdapter(statAdapter);
        list = new ArrayList<>();
        dao = new OrderDAO();
        progressBar.setVisibility(View.VISIBLE);
        controller.setRecViewContent(); //load data
        progressBar.setVisibility(View.INVISIBLE);


        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser office = mAuth.getCurrentUser();
        String id = office.getUid();


        Query query = dao.get().orderByChild("office").equalTo(id+"");


         options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(query, new SnapshotParser<Order>() {
                    @NonNull
                    @Override
                    public Order parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Order order = snapshot.getValue(Order.class);
                        list.add(order);
                        Log.i("firb", list.toString());
                        return order;


                    }
                }).build();

        /*FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Order").child("Orders"),Order.class)
                .build();*/




        adapter = new OrderAdapter(options, list, this);

        recyclerView.setAdapter(adapter);

        edT_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //filter(s.toString());
                Log.i("firb", s.toString());
                controller.setRecViewFilterContent(dao, s.toString());
            }
        });

    }

    private void filter(String text){

        /* ArrayList<Order> filteredList = null;

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getStatus().equals(text))
            {
                Log.i("sucfireb", String.valueOf(list.get(i).getOrder_number()));
                filteredList.add(list.get(i));
            }
        }
*/


        Query query = dao.get().orderByChild("status").equalTo(text);

        FirebaseRecyclerOptions<Order> filteredList = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();

        OrderAdapter adapter2;

        adapter2 = new OrderAdapter(filteredList, list, this);
        recyclerView.setAdapter(adapter2);

   /*     for(Order order : list){
            if(order.getCustomer_name().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(order);
            }
        }*/





    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem seatchItem = menu.findItem(R.id.edT_search_order);
        SearchView searchView = (SearchView) seatchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }*/

    @Override
    protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDetailClick(int position)
    {
        controller.getOrderFirebaseResources(controller.getOrderID(position));

        createBottomSheet();


    }


    private void createBottomSheet() {
         bottomSheetDialog = new BottomSheetDialog(OrderActivityView.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sheet_detail_order,(LinearLayout)findViewById(R.id.sheet_container));
        bottomSheetDialog.setContentView(bottomSheetView);

        numberOrder = bottomSheetDialog.findViewById(R.id.number_order);
        txt_date_order = bottomSheetDialog.findViewById(R.id.txt_date_order);
        txt_status = bottomSheetDialog.findViewById(R.id.txt_status);
        img_status_bar = bottomSheetDialog.findViewById(R.id.img_status_bar);
        txt_type_payment = bottomSheetDialog.findViewById(R.id.txt_type_payment);
        txt_paid = bottomSheetDialog.findViewById(R.id.txt_paid);
        txt_price = bottomSheetDialog.findViewById(R.id.txt_price);
        txt_name_customer = bottomSheetDialog.findViewById(R.id.txt_name_customer);
        txt_email_customer = bottomSheetDialog.findViewById(R.id.txt_email_customer);
        txt_phone_customer = bottomSheetDialog.findViewById(R.id.txt_phone_customer);
        txt_offic_address = bottomSheetDialog.findViewById(R.id.txt_offic_address);
        txt_office_name = bottomSheetDialog.findViewById(R.id.txt_office_name);
        txt_name_product = bottomSheetDialog.findViewById(R.id.txt_name_product);
        txt_discount = bottomSheetDialog.findViewById(R.id.txt_discount);
        txt_date_pay = bottomSheetDialog.findViewById(R.id.txt_date_pay);
        title_date_pay = bottomSheetDialog.findViewById(R.id.title_date_pay);
        title_locate = bottomSheetDialog.findViewById(R.id.title_locate);
        txt_locate = bottomSheetDialog.findViewById(R.id.txt_locate);
        txt_date_locate = bottomSheetDialog.findViewById(R.id.txt_date_locate);
        title_registr_num = bottomSheetDialog.findViewById(R.id.title_registr_num);
        txt_register_num = bottomSheetDialog.findViewById(R.id.txt_register_num);
        btn_payment = bottomSheetDialog.findViewById(R.id.btn_payment);
        btn_storno = bottomSheetDialog.findViewById(R.id.btn_storno);
        btn_edit_sale = bottomSheetDialog.findViewById(R.id.btn_edit_sale);
        txt_description = bottomSheetDialog.findViewById(R.id.txt_waiting_alert);


        bottomSheetDialog.show();

        btn_edit_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSaleDialog();
            }
        });

        btn_storno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStornoDialog();
            }
        });

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPaymentDialog();
              //  controller.setPDF(context);

            }
        });

    }

    private void openPaymentDialog() {
        PaymentDialog paymentDialog = new PaymentDialog();
        paymentDialog.show(getSupportFragmentManager(), "payment dialog");
    }

    private void openStornoDialog() {
        StornoDialog stornoDialog = new StornoDialog();
        stornoDialog.show(getSupportFragmentManager(), "storno dialog");
    }

    public void openSaleDialog() {
        SaleDialog saleDialog = new SaleDialog();
        saleDialog.show(getSupportFragmentManager(),"sale dialog");
    }
    @Override
    public void applyTexts2() {
        controller.setAfterStorno();

    }

    @Override
    public void applyTexts(String sale) {
        controller.setAfterSale(sale);

    }

    @Override
    public void applyTexts3() {
        controller.setAfterPayment();
        adapter.notifyDataSetChanged();
        controller.removeProducts();
        controller.setPDF(context);
       // controller.sendToEmail();
        bottomSheetDialog.cancel();

    }

}