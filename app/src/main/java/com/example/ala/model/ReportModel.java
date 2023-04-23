package com.example.ala.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ala.DAO.OfficeDAO;
import com.example.ala.DAO.OfficeDAOInterface;
import com.example.ala.DAO.OrderDAO;
import com.example.ala.DAO.OrderDAOInterface;
import com.example.ala.controller.ReportController;
import com.example.ala.model.object.Order;
import com.example.ala.model.object.Product;
import com.example.ala.model.object.ProductInOrder;
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
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportModel {
    private ReportController controller;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;


    public ReportModel(ReportController controller) {
        this.controller = controller;
    }

    public void execWarehouseReport() {

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser office = mAuth.getCurrentUser();
        String id = office.getUid();

        databaseReference =  FirebaseDatabase.getInstance().getReference().child("Office").child(id).child("Product");

        // productAdapter = new ProductAdapter(context, list, list2, this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                float sum_price = 0;
                long count_product = snapshot.getChildrenCount();
                ArrayList<String> most_product = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);

                    if (i == 0)
                        sum_price = Float.parseFloat(product.getPrice());
                    else
                        sum_price = sum_price + Float.parseFloat(product.getPrice());


                    most_product.add(product.getName());

                    i++;
                }
                   if(count_product != 0) {
                       String mostRepeatedProduct
                               = most_product.stream()
                               .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                               .entrySet()
                               .stream()
                               .max(Comparator.comparing(Map.Entry::getValue))
                               .get()
                               .getKey();


                       controller.setValueAfterWarehouseReport(count_product, setPriceFormat(sum_price), mostRepeatedProduct);
                   }
                   else{
                       controller.setValueAfterWarehouseReport(0, 0+"", "-");
                   }



            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    public String setPriceFormat(float price) {


        DecimalFormat formater = new DecimalFormat("###,###.##");

        return formater.format(price);

    }

    public void execOrderReport(String date_from, String date_to) {
        OfficeDAOInterface officeDAO = new OfficeDAO();
        String id = officeDAO.getFirebUser().getUid();




        OrderDAOInterface orderDAO = new OrderDAO();
        orderDAO.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count_order = 0;
                float sum_price_with_DPH = 0;
                float sum_price_without_DPH = 0;
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM-dd-yyyy");
                 Date date_from_d, date_to_d,search_date;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    try {
                        date_from_d = simpleDateFormat1.parse(date_from);
                        date_to_d = simpleDateFormat1.parse(date_to);

                        boolean paid = Boolean.parseBoolean(dataSnapshot.child("Payment").child("paid").getValue().toString());
                        if(paid) {
                            String date_pay = dataSnapshot.child("Payment").child("date_pay").getValue().toString();
                            search_date = simpleDateFormat2.parse(date_pay);
                        }else
                            search_date = null;

                    } catch (ParseException e) {
                        date_from_d = null;
                        date_to_d = null;
                        search_date = null;
                        e.printStackTrace();
                    }

          //          Log.i("dateexpression",date_from_d + "|" + date_to_d + "|" + search_date);
                    if (order.getOffice().contains(id) && search_date!= null &&((search_date.after(date_from_d) || search_date.equals(date_from_d)) && (search_date.before(date_to_d) || search_date.equals(date_to_d)))) {

                        if(count_order == 0) {
                            sum_price_with_DPH = Float.parseFloat(dataSnapshot.child("Payment").child("price").getValue().toString());
                            sum_price_without_DPH = calculatePriceWithoutDPH(sum_price_with_DPH);
                        }
                        else {
                            sum_price_with_DPH = sum_price_with_DPH + Float.parseFloat(dataSnapshot.child("Payment").child("price").getValue().toString());
                            sum_price_without_DPH = sum_price_without_DPH + calculatePriceWithoutDPH(Float.parseFloat(dataSnapshot.child("Payment").child("price").getValue().toString()));
                        }
                        count_order++;
                    }

                }
                controller.setValueAfterOrderReport(count_order, setPriceFormat(sum_price_with_DPH), setPriceFormat(sum_price_without_DPH));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



     /*   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date_from_d = simpleDateFormat.parse(date_from);
            Date date_to_d = simpleDateFormat.parse(date_to);
            Date search_date = simpleDateFormat.parse("22.4.2023");

            if((search_date.after(date_from_d) || search_date.equals(date_from_d)) && (search_date.before(date_to_d) || search_date.equals(date_to_d)))
                Log.i("dateexpression","YES");
            else{
                Log.i("dateexpression","NO");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    private float calculatePriceWithoutDPH(float sum_price_with_dph) {
        float dph = sum_price_with_dph * 21 / 100;
        return sum_price_with_dph - dph;
    }
}
