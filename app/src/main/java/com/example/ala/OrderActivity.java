package com.example.ala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowId;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ala.Inventory.StatusData;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OrderActivity extends AppCompatActivity implements OrderAdapter.OnDetailListener {

    private Spinner spinner_status;
    private StatusAdapter statAdapter;

    private FirebaseDatabase firebaseDatabase, firebaseDatabase2, firebaseDatabase3;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference, databaseReference2, databaseReference3, referenceOffice;
    private RecyclerView recyclerView;
    OrderAdapter adapter;
    ArrayList<Order> list;
    Order order = new Order();
    Customer customer = new Customer();
    Office office = new Office();
    Product product = new Product();
    private FirebaseUser officeF;
    ArrayList<String> names_product = new ArrayList<String>(2);
  //  String name[] = new String[2];
    int count = 0;

    //TODO fce filter orders by word search or status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView = findViewById(R.id.recycler_view2);
        spinner_status = findViewById(R.id.spinner_filter);
        statAdapter = new StatusAdapter(OrderActivity.this, StatusData.getStatusList());
        spinner_status.setAdapter(statAdapter);

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser office = mAuth.getCurrentUser();
        String id = office.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Order").child("Orders");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new OrderAdapter(this, list, this);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Order order = dataSnapshot.getValue(Order.class);


                    if(order.getOffice().contains(id)) {
                        list.add(order);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onDetailClick(int position)
    {
      //  String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_email_customer)).getText().toString();
      //  Toast.makeText(this, "Position" + position, Toast.LENGTH_SHORT).show();


      //  Toast.makeText(this, "Position " + numberOrder.getText().toString(), Toast.LENGTH_SHORT).show();
        int id_order = list.get(position).getId_order();
        Toast.makeText(this, "ID order " + id_order, Toast.LENGTH_SHORT).show();

        getOrderFirebaseResources(id_order);

       // SharedPreferences sharedPreferences = getSharedPreferences("MyOrderPref", MODE_PRIVATE);

      //  String shared_order_num = sharedPreferences.getString("fr_order_number","q");

/*
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(OrderActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sheet_detail_order,(LinearLayout)findViewById(R.id.sheet_container));
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView numberOrder = bottomSheetDialog.findViewById(R.id.number_order);
        TextView title_registr_num = bottomSheetDialog.findViewById(R.id.title_registr_num);
        TextView register_num = bottomSheetDialog.findViewById(R.id.register_num);

        numberOrder.setText("Objednávka " + order.getOrder_number());
        title_registr_num.setVisibility(View.GONE);
        register_num.setVisibility(View.GONE);




        bottomSheetDialog.show();*/
    }

    private void getOrderFirebaseResources(int id_order) {
        firebaseDatabase2 = FirebaseDatabase.getInstance();
        databaseReference2 = firebaseDatabase2.getReference().child("Order").child("Orders");

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String id_order_firebase_STR = dataSnapshot.child("id_order").getValue().toString();
                    int id_order_firebase = Integer.valueOf(id_order_firebase_STR);

                    if(id_order_firebase == id_order)
                    {
                        String order_number = dataSnapshot.child("order_number").getValue().toString();
                        String date_order = dataSnapshot.child("date").getValue().toString();
                        String time_order = dataSnapshot.child("time").getValue().toString();
                        String status = dataSnapshot.child("status").getValue().toString();
                        String office = dataSnapshot.child("office").getValue().toString();
                        String id_list_product = dataSnapshot.child("id_list_product").getValue().toString();

                        String[] str = id_list_product.split(",");
                        String[] arr = new String[str.length];
                        for(int i = 0; i < str.length; i++)
                            arr[i] = str[i];

                        String id_customer = dataSnapshot.child("id_customer").getValue().toString();
                        //Payment detail
                        String type_pay = dataSnapshot.child("Payment").child("type").getValue().toString();
                        boolean paid = Boolean.parseBoolean(dataSnapshot.child("Payment").child("paid").getValue().toString());
                        String price = dataSnapshot.child("Payment").child("price").getValue().toString();





                        Log.i("getOrderFirebaseRes", "Num.order: " + order_number + ", Status: " + status + ", TypePay: " + type_pay + " Paid: "+ paid +", ListProd.: " + arr[0]);

                        order.setOrder_number(Integer.parseInt(order_number));
                        order.setDate(date_order);
                        order.setTime(time_order);
                        order.setStatus(status);
                        order.setType_pay(type_pay);
                        order.setPaid(paid);
                        order.setPrice(Integer.parseInt(price));
                        order.setId_customer(Integer.parseInt(id_customer));

                        getCustomerFirebaseResources(Integer.parseInt(id_customer), office, arr);



                    }

                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCustomerFirebaseResources(int id_customer, String office, String[] arr) {

        firebaseDatabase3 = FirebaseDatabase.getInstance();
        databaseReference3 = firebaseDatabase3.getReference().child("Customer").child("Customers");

        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String id_customer_STR = dataSnapshot.child("id").getValue().toString();
                    int id_order_firebase = Integer.valueOf(id_customer_STR);

                    if(id_order_firebase == id_customer)
                    {
                        String fname = dataSnapshot.child("fname").getValue().toString();
                        String lname = dataSnapshot.child("lname").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();


                        Log.i("getCustomerFirebaseRes", "L.name: " + lname + ", Email: " + email + ", Phone num.:" + phone);

                        customer.setLname(lname);
                        customer.setFname(fname);
                        customer.setEmail(email);
                        customer.setPhone(phone);


                        getOfficeFirebaseResources(office, arr);


                    }

                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getOfficeFirebaseResources(String officeS, String[] arr) {

       //officeF = FirebaseAuth.getInstance().getCurrentUser();
       referenceOffice = FirebaseDatabase.getInstance().getReference("Office");
      // String officeID = officeF.getUid();

        referenceOffice.child(officeS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Office officeProfile = snapshot.getValue(Office.class);

                //  if (officeProfile != null){
                String name = officeProfile.name;
                String address = officeProfile.address;

                office.setName(name);
                office.setAddress(address);

                Log.i("getOfficeFirebaseRes", "office: " + officeS);

                getProductListFirebaseResources(arr);
               // createBottomSheet();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void getProductListFirebaseResources(String[]arr) {
        firebaseDatabase3 = FirebaseDatabase.getInstance();
        databaseReference3 = firebaseDatabase3.getReference().child("Product").child("Products");



        count = 0;

        for(int i = 0; i < arr.length; i++) {

            databaseReference3.child(arr[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   product = snapshot.getValue(Product.class);
                    //  if (officeProfile != null){
                    String name_product = snapshot.child("name").getValue().toString();

                    names_product.add(name_product);
                   // name[count] = name_product;

                 //   product.setName(name_product);

                    Log.i("getProductListFirebRes", "product name: " + name_product);


                    Log.i("getProductListFirebRes", "product names: " + names_product);

                    if(count == arr.length -1) {
                        String out = Arrays.toString(names_product.toArray()).replace("[","").replace("]","").replace(",",",\n");
                        product.setName(out);
                        names_product.clear();
                        createBottomSheet();
                    }

                    count++;



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //Log.i("getProductListFirebRes", "product names: " + names_product);
      //  product.setName(names_product);

    }



    private void createBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(OrderActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sheet_detail_order,(LinearLayout)findViewById(R.id.sheet_container));
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView numberOrder = bottomSheetDialog.findViewById(R.id.number_order);
        TextView title_registr_num = bottomSheetDialog.findViewById(R.id.title_registr_num);
        TextView register_num = bottomSheetDialog.findViewById(R.id.register_num);
        TextView txt_date_order = bottomSheetDialog.findViewById(R.id.txt_date_order);
        TextView txt_status = bottomSheetDialog.findViewById(R.id.txt_status);
        ImageView img_status_bar = bottomSheetDialog.findViewById(R.id.img_status_bar);
        TextView txt_type_payment = bottomSheetDialog.findViewById(R.id.txt_type_payment);
        TextView txt_paid = bottomSheetDialog.findViewById(R.id.txt_paid);
        TextView txt_price = bottomSheetDialog.findViewById(R.id.txt_price);
        TextView txt_name_customer = bottomSheetDialog.findViewById(R.id.txt_name_customer);
        TextView txt_email_customer = bottomSheetDialog.findViewById(R.id.txt_email_customer);
        TextView txt_phone_customer = bottomSheetDialog.findViewById(R.id.txt_phone_customer);
        TextView txt_offic_address = bottomSheetDialog.findViewById(R.id.txt_offic_address);
        TextView txt_office_name = bottomSheetDialog.findViewById(R.id.txt_office_name);
        TextView txt_name_product = bottomSheetDialog.findViewById(R.id.txt_name_product);

        numberOrder.setText("Objednávka " + order.getOrder_number());
        txt_date_order.setText(parseDateFromDatabase(order) + " "+ order.getTime());
        String status = order.getStatus();
        String typePay = order.getType_pay();
        int id_customer = order.getId_customer();
        boolean paid = order.isPaid();
        Log.i("getOrderFirebaseRes", String.valueOf(paid));
        setStatus(status, txt_status, img_status_bar);
        setTypePayTitle(typePay, txt_type_payment);
        setPaidTitle(paid, txt_paid);
        txt_price.setText(order.getPrice() + ",00 Kč s DPH ");

        //customer
        txt_name_customer.setText(customer.getFname() + " " + customer.getLname());
        txt_email_customer.setText(customer.getEmail());
        txt_phone_customer.setText(customer.getPhone());

        //office
        txt_offic_address.setText(office.getAddress());
        txt_office_name.setText(office.getName());

        //list product
        txt_name_product.setText(product.getName());

        title_registr_num.setVisibility(View.GONE);
        register_num.setVisibility(View.GONE);




        bottomSheetDialog.show();

    }



    private void setPaidTitle(boolean paid, TextView txt_paid) {
        if(paid == true)
            txt_paid.setText("ANO");
        else
            txt_paid.setText("NE");
    }

    private void setTypePayTitle(String typePay, TextView txt_type_payment) {
        if(typePay.equals("card"))
            txt_type_payment.setText("Kartou");
        if(typePay.equals("cash"))
            txt_type_payment.setText("Hotovost");

    }

    private void setStatus(String status, TextView txt_status, ImageView img_status_bar) {

        switch (status) {
            case "PE":
                txt_status.setText("Čekající");
                img_status_bar.setImageResource(R.drawable.status_bar_pe);
                break;
            case "IP":
                txt_status.setText("Vyřizuje se");
                img_status_bar.setImageResource(R.drawable.status_bar_ip);
                break;
            case "CO":
                txt_status.setText("Vyřízena");
                img_status_bar.setImageResource(R.drawable.status_bar_co);
                break;
            case "CA":
                txt_status.setText("Stornována");
                img_status_bar.setImageResource(R.drawable.status_bar_ca);
                break;

        }

    }

    private String parseDateFromDatabase(Order order) {
        SimpleDateFormat database_format = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat output_format = new SimpleDateFormat("dd.M.yyyy");

        try {
            Date date = database_format.parse(order.getDate());
            return output_format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

}