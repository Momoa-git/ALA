package com.example.ala.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ala.Office;
import com.example.ala.Order;
import com.example.ala.Product;
import com.example.ala.controller.OrderActivityController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class OrderActivityModel {

    private FirebaseDatabase firebaseDatabase, firebaseDatabase2;
    private DatabaseReference databaseReference, databaseReference2;
    private FirebaseAuth mAuth;
    Order order = new Order();
    int count;
    private OrderActivityController controller;
    ArrayList<String> names_product = new ArrayList<String>(2);

    public OrderActivityModel(OrderActivityController controller)
    {
        this.controller = controller;
    }

    public void setRecViewContent()
    {
        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser office = mAuth.getCurrentUser();
        String id = office.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();//
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Order").child("Orders");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Order order = dataSnapshot.getValue(Order.class);


                    if(order.getOffice().contains(id)) {
                        //list.add(order);
                        controller.onAddOrderToList(order);
                    }

                }
                //adapter.notifyDataSetChanged();
                controller.onNotifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public int getOrderID(int position)
    {
        return controller.onOrderID(position);

    }

    public void getOrderFirebaseResources(int id_order) {
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
                        String[] array_id_list_product = new String[str.length];
                        for(int i = 0; i < str.length; i++)
                            array_id_list_product[i] = str[i];

                        String id_customer = dataSnapshot.child("id_customer").getValue().toString();
                        //Payment detail
                        String type_pay = dataSnapshot.child("Payment").child("type").getValue().toString();
                        boolean paid = Boolean.parseBoolean(dataSnapshot.child("Payment").child("paid").getValue().toString());
                        String price = dataSnapshot.child("Payment").child("price").getValue().toString();
                        String possibleDiscount = checkPossibleDiscount(dataSnapshot);
                        String possibleDatePay = checkPosssibleDatePay(dataSnapshot, paid);





                        Log.i("getOrderFirebaseRes", "Num.order: " + order_number + ", Status: " + status + ", TypePay: " + type_pay + " Paid: "+ paid +", ListProd.: " + array_id_list_product[0]);


                        String paidAfterParse = setPaidTitle(paid);
                        String typePayAfterParse = setTypeTitle(type_pay);
                        String priceAfterParse = setPriceFormat(price);
                        String dateAfterParse = setDateFormat(date_order);

                        controller.setOrderResources(order_number, dateAfterParse,time_order,status, office, id_list_product, typePayAfterParse, paidAfterParse, priceAfterParse, possibleDiscount, possibleDatePay);


                        getCustomerFirebaseResources(Integer.parseInt(id_customer));
                        getOfficeFirebaseResources(office);
                        getProductListFirebaseResources(array_id_list_product);


                    }

                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String checkPosssibleDatePay(DataSnapshot dataSnapshot, boolean paid) {
        try {
            if (paid) {
                String date_pay = dataSnapshot.child("Payment").child("date_pay").getValue().toString();
                String time_pay = dataSnapshot.child("Payment").child("time_pay").getValue().toString();

                return setDateFormat(date_pay) + " " + time_pay;
            } else {
                controller.setInvisibleDatePay();
                return "invisible";
            }

        }catch (NullPointerException e){
            controller.setInvisibleDatePay();
            return "invisible";
        }

    }

    private String checkPossibleDiscount(DataSnapshot dataSnapshot) {
            try {
                String discount = dataSnapshot.child("Payment").child("discount").getValue().toString();
                try {
                    Integer.parseInt(discount);
                    return discount + "%";
                }catch (NumberFormatException n){
                    return "ERR format exception";
                }
            }catch (NullPointerException e){
                return "-";
            }


    }

    private String setDateFormat(String date_order) {
        SimpleDateFormat database_format = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat output_format = new SimpleDateFormat("dd.M.yyyy");

        try {
            Date date = database_format.parse(date_order);
            return output_format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String setPriceFormat(String price) {

        double amount = Double.parseDouble(price);
        DecimalFormat formater = new DecimalFormat("###,###.00");

        return formater.format(amount);

    }

    private String setTypeTitle(String type_pay) {
        if(type_pay.equals("card"))
            return "Kartou";
        if(type_pay.equals("cash"))
            return "Hotovost";
        else
            return "ERR";
    }

    private String setPaidTitle(boolean paid) {
        if(paid == true)
            return "ANO";
        else
            return "NE";

    }

    public void getCustomerFirebaseResources(int id_customer) {

        firebaseDatabase2 = FirebaseDatabase.getInstance();
        databaseReference2 = firebaseDatabase2.getReference().child("Customer").child("Customers");

        databaseReference2.addValueEventListener(new ValueEventListener() {
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

                        controller.setCustomerResources(fname, lname, email, phone);
/*
                        customer.setLname(lname);
                        customer.setFname(fname);
                        customer.setEmail(email);
                        customer.setPhone(phone);
*/




                    }

                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getOfficeFirebaseResources(String officeS) {

        //officeF = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Office");
        // String officeID = officeF.getUid();

        databaseReference2.child(officeS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Office officeProfile = snapshot.getValue(Office.class);


                String name = officeProfile.name;
                String address = officeProfile.address;


/*
                office.setName(name);
                office.setAddress(address);
*/
                Log.i("getOfficeFirebaseRes", "office: " + officeS);

                controller.setOfficeResources(name, address);

               // getProductListFirebaseResources(arr);
                // createBottomSheet();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void getProductListFirebaseResources(String[]arr) {
        firebaseDatabase2 = FirebaseDatabase.getInstance();
        databaseReference2 = firebaseDatabase2.getReference().child("Product").child("Products");



        count = 0;

        for(int i = 0; i < arr.length; i++) {

            databaseReference2.child(arr[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Product product = snapshot.getValue(Product.class);
                    //  if (officeProfile != null){
                    String name_product = snapshot.child("name").getValue().toString();

                    names_product.add(name_product);
                    // name[count] = name_product;

                    //   product.setName(name_product);

                    Log.i("getProductListFirebRes", "product name: " + name_product);


                    Log.i("getProductListFirebRes", "product names: " + names_product);

                    if(count == arr.length -1) {
                        String nameStream = Arrays.toString(names_product.toArray()).replace("[","").replace("]","").replace(",","\n");
                       // product.setName(out);
                        controller.setProductListResources(nameStream);
                        names_product.clear();
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


    public String calculatePriceAfterSale(float sale_f, float price, float old_sale_f) {

        float full_price;

        if(old_sale_f != 0)
         full_price = price * 100 / (100 - old_sale_f);

        else
            full_price = price;

        float sale = sale_f * full_price / 100;
        return setPriceFormat(String.valueOf(full_price - sale));
    }
}
