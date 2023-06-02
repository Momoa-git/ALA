package com.vsb.ala.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.vsb.ala.DAO.OrderDAO;
import com.vsb.ala.DAO.OrderDAOInterface;
import com.vsb.ala.DAO.ProductDAO;
import com.vsb.ala.DAO.ProductDAOInterface;
import com.vsb.ala.controller.NewProductController;
import com.vsb.ala.model.object.Order;
import com.vsb.ala.model.object.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewProductModel {
    private NewProductController controller;
    ProductDAOInterface productDAO = new ProductDAO();
    OrderDAOInterface orderDAO = new OrderDAO();
    Integer reg_number, register_number;
    boolean semaphore = true;
    Integer counter;
    boolean order_assigned = false;

    public NewProductModel(NewProductController controller){
        this.controller = controller;
    }

    public void addNewProduct(String name, String price, String bar_code, Integer piece, String line, String place, int id_list_product) {

            Log.i("outline","[LIST:] ID: "+ id_list_product  + " Name: " + name + " Pieces: "+ piece + "x");

            order_assigned = false;
            counter = 0;

            semaphore = false;
            readRegisterNumberData(new FirebaseCallback() {
                @Override
                public void onCallBack(int reg_num) {
                    searchingOrder(id_list_product, register_number, new FirebaseCallback2() {
                        @Override
                        public void onCallBack2(boolean order_assigned, Order order, int product_iteration) {
                            for (int i = 0; i < piece; i++) {
                                register_number = reg_num + counter;

                                Product product = new Product(register_number, id_list_product, name, price, bar_code, line, place, getActualDateTime(), order_assigned);
                                productDAO.setProduct(product);
                                int new_reg_number = register_number + 1;
                                productDAO.setSerial_number(new_reg_number);
                                counter++;

                                if (order_assigned) {
                                    Log.i("outline", "[MATCH:] Order: " + order.getOrder_number() + " | Product: " + register_number);
                                     orderDAO.setRegisterNumberOfProduct(order,product_iteration, register_number);
                                    checkfillAllProduct(order.getId_order() - 1, new FirebaseCallback3(){
                                        @Override
                                        public void onCallBack3() {

                                        }

                                    });

                                    Log.i("outline", "[WAREHOUSE:] Register_num.: " + register_number + " ID_list_product: " + id_list_product + " Name: " + name + " Price: " + price +
                                            " BarCode: " + bar_code + " Line-place: " + line + "-" + place + " DateTime arrivals " + getActualDateTime() + " Assigned: " + order_assigned);

                                    order_assigned = false;
                                }
                                else{
                                    Log.i("outline", "[WAREHOUSE:] Register_num.: " + register_number + " ID_list_product: " + id_list_product + " Name: " + name + " Price: " + price +
                                            " BarCode: " + bar_code + " Line-place: " + line + "-" + place + " DateTime arrivals " + getActualDateTime() + " Assigned: " + order_assigned);
                                }
                            }
                        }
                    });
                }
            });
            controller.displayToast(piece);
    }

    private void readRegisterNumberData(FirebaseCallback firebaseCallback){
        Log.i("outline", "Before read");
        productDAO.getSerialNumber().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reg_number = Integer.valueOf(String.valueOf(snapshot.getValue()));
                Log.i("outline", "Before call");
                firebaseCallback.onCallBack(reg_number);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void searchingOrder(int id_list_product, Integer register_number, FirebaseCallback2 firebaseCallback) {
        Log.i("outline", "Before searching");
        orderDAO.get().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Order searchOrder = null;
                int product_iteration = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    long count_product = Long.valueOf(dataSnapshot.child("Product item").getChildrenCount());
                    for (int i = 0; productDAO.getmAuth().getUid().contains(order.getOffice()) && i < count_product; i++)
                    {
                        Log.i("DBZ", "ORDER: "+ order.getOrder_number());
                        int id_product = Integer.valueOf(dataSnapshot.child("Product item").child(i+"").child("id_product").getValue().toString()) + 1;
                        int registration_num = Integer.valueOf(dataSnapshot.child("Product item").child(i+"").child("registration_num").getValue().toString());
                        Log.i("DBZ", "ID product " + id_product + " IDLISTPRODUCT: " + id_list_product + "|" + registration_num);

                        if(id_product == id_list_product && order.getStatus().equals("PE") && registration_num == 0) {
                            Log.i("DBZ", "[MATCH IN ORDER:]" + id_list_product );
                            order_assigned = true;
                            searchOrder = order;
                            product_iteration = i;

                        }

                    }


                }
                firebaseCallback.onCallBack2(order_assigned, searchOrder, product_iteration);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }

    private void checkfillAllProduct(int id, FirebaseCallback3 firebaseCallback3) {
        orderDAO.getProductItem(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                long count_product = Long.valueOf(snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    i++;
                    String register = dataSnapshot.child("registration_num").getValue().toString();


                    if(Integer.valueOf(register) == 0)
                        break;

                    Log.i("outline", "Fill reg. num: " + snapshot.child("registration_num").getValue() + " " + count_product);

                    if(i == count_product) {
                        orderDAO.setStatus(id, "IP");
                        Log.i("outline", "STATUS CHANGE...");
                    }
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        firebaseCallback3.onCallBack3();

    }


    private String getActualDateTime() {

        SimpleDateFormat output_format = new SimpleDateFormat("dd.M.yyyy H:mm:ss");
        Date date = new Date();

        return output_format.format(date);

    }

    private interface FirebaseCallback{
        void onCallBack(int reg_num);
    }

    private interface FirebaseCallback2{
        void onCallBack2(boolean order_assigned, Order order, int product_iteration);
    }
    private interface FirebaseCallback3{
        void onCallBack3();
    }

}
