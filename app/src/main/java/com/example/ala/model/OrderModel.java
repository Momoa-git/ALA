package com.example.ala.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ala.DAO.CustomerDAO;
import com.example.ala.DAO.CustomerDAOInterface;
import com.example.ala.DAO.OfficeDAO;
import com.example.ala.DAO.OfficeDAOInterface;
import com.example.ala.DAO.OrderDAOInterface;
import com.example.ala.DAO.ProductDAO;
import com.example.ala.DAO.ProductDAOInterface;
import com.example.ala.model.object.ProductInOrder;
import com.example.ala.model.object.Invoice;
import com.example.ala.model.object.Office;
import com.example.ala.model.object.Order;
import com.example.ala.DAO.OrderDAO;
import com.example.ala.model.object.Product;
import com.example.ala.controller.OrderController;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class OrderModel {
    private OrderController controller;
    ArrayList<Order> orders = new ArrayList<>();
    Order order = new Order();

    public OrderModel(OrderController controller) {
        this.controller = controller;
    }

    public void setRecViewContent() {
        OfficeDAOInterface officeDAO = new OfficeDAO();
        String id = officeDAO.getFirebUser().getUid();

        OrderDAOInterface orderDAO = new OrderDAO();
        orderDAO.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);


                    if (order.getOffice().contains(id)) {
                        orders.add(order);
                    }

                }
                controller.onSetItems(orders);
                controller.onNotifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    public int getOrderID(int position) {
        return orders.get(position).getId_order();
    }

    public void getOrderFirebRes(int id_order) {
        OrderDAOInterface orderDAO = new OrderDAO();

        orderDAO.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id_order_firebase_STR = dataSnapshot.child("id_order").getValue().toString();
                    int id_order_firebase = Integer.valueOf(id_order_firebase_STR);

                    if (id_order_firebase == id_order) {
                        String order_number = dataSnapshot.child("order_number").getValue().toString();
                        String date_order = dataSnapshot.child("date").getValue().toString();
                        String time_order = dataSnapshot.child("time").getValue().toString();
                        String status = dataSnapshot.child("status").getValue().toString();
                        String office = dataSnapshot.child("office").getValue().toString();

                        //Piece and name of products
                        String name_products = "";
                        String reg_numbers = "";
                        long iteration = dataSnapshot.child("Product item").getChildrenCount();

                        order.inventory.removeItems();

                        for (int count = 0; count < iteration; count++) {
                            String name = dataSnapshot.child("Product item").child(String.valueOf(count)).child("name").getValue().toString();
                            long pieces_of_product = (long )dataSnapshot.child("Product item").child(String.valueOf(count)).child("piece").getValue();
                            String reg_number = dataSnapshot.child("Product item").child(String.valueOf(count)).child("registration_num").getValue().toString();
                            String price = dataSnapshot.child("Product item").child(String.valueOf(count)).child("price").getValue().toString();
                            double price_double = Double.parseDouble(price);
                            if (name_products.isEmpty()) {
                                name_products = "(" + pieces_of_product + "ks) " + name;
                            } else {
                                name_products = name_products + "\n" + "(" + pieces_of_product + "ks) " + name;

                            }
                            if (reg_numbers.isEmpty()) {
                                reg_numbers = reg_number;
                            }
                            else {
                                reg_numbers = reg_numbers + "\n" + reg_number;
                            }

                            order.inventory.addItem(new ProductInOrder(name, pieces_of_product, price_double, Integer.valueOf(reg_number)));

                        }

                        String id_customer = dataSnapshot.child("id_customer").getValue().toString();
                        //Payment detail
                        String type_pay = dataSnapshot.child("Payment").child("type").getValue().toString();
                        boolean paid = Boolean.parseBoolean(dataSnapshot.child("Payment").child("paid").getValue().toString());
                        String price = dataSnapshot.child("Payment").child("price").getValue().toString();
                        long possibleDiscount = checkPossibleDiscount(dataSnapshot);
                        String possibleDatePay = checkPosssibleDatePay(dataSnapshot, paid);

                        Log.i("getOrderFirebaseRes", "Num.order: " + order_number + ", Status: " + status + ", TypePay: " + type_pay + " Paid: " + paid + ", NameProducts: " + name_products+"" + " DatePay: " + possibleDatePay);

                        String paidAfterParse = setPaidTitle(paid);
                        String typePayAfterParse = setTypeTitle(type_pay);
                        String priceAfterParse = setPriceFormat(price);
                        String dateAfterParse = setDateFormat(date_order);

                        order.setOrder_number(Integer.valueOf(order_number));
                        order.setDate_order(dateAfterParse);
                        order.payment.setType_pay(typePayAfterParse);
                        order.payment.setDiscount(possibleDiscount);
                        order.payment.setPrice(price);

                        controller.setOrderResources(order_number, dateAfterParse, time_order, status, name_products, reg_numbers, typePayAfterParse, paidAfterParse, priceAfterParse, possibleDiscount + "%", possibleDatePay);

                        getCustomerFirebRes(Integer.parseInt(id_customer));
                        getOfficeFirebRes(office);

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

                order.payment.setDate_pay(setDateFormat(date_pay));
                return setDateFormat(date_pay) + " " + time_pay;
            } else {
                controller.setInvisibleDatePay();
                return "invisible";
            }

        } catch (NullPointerException e) {
            controller.setInvisibleDatePay();
            return "invisible";
        }

    }

    private long checkPossibleDiscount(DataSnapshot dataSnapshot) {
        try {
            long discount = (long) dataSnapshot.child("Payment").child("discount").getValue();
            try {
                return discount;
            } catch (NumberFormatException n) {
                return -1;
            }
        } catch (NullPointerException e) {
            return 0;
        }


    }

    private String setDateFormat(String date_order) {
        SimpleDateFormat database_format = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat output_format = new SimpleDateFormat("dd.MM.yyyy");

        try {
            Date date = database_format.parse(date_order);
            return output_format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String setPriceFormat(String price) {

        double amount = Double.parseDouble(price);
        DecimalFormat formater = new DecimalFormat("###,###.##");

        return formater.format(amount);

    }

    private String setTypeTitle(String type_pay) {
        if (type_pay.equals("card"))
            return "Kartou Internetem";
        if (type_pay.equals("cash"))
            return "Hotovost";
        else
            return "ERR";
    }

    private String setPaidTitle(boolean paid) {
        if (paid == true)
            return "ANO";
        else
            return "NE";

    }

    public void getCustomerFirebRes(int id_customer) {

        CustomerDAOInterface customerDAO = new CustomerDAO();

        customerDAO.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id_customer_STR = dataSnapshot.child("id").getValue().toString();
                    int id_order_firebase = Integer.valueOf(id_customer_STR);

                    if (id_order_firebase == id_customer) {
                        String fname = dataSnapshot.child("fname").getValue().toString();
                        String lname = dataSnapshot.child("lname").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();

                        order.setCustomer_name(fname + " " + lname);
                        order.setCustomer_email(email);
                        order.setCustomer_phone(phone);

                        Log.i("getCustomerFirebaseRes", "L.name: " + lname + ", Email: " + email + ", Phone num.:" + phone);

                        controller.setCustomerResources(fname, lname, email, phone);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getOfficeFirebRes(String officeS) {
       OfficeDAOInterface officeDAO = new OfficeDAO();

        officeDAO.getRef(officeS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Office officeProfile = snapshot.getValue(Office.class);

                String name = officeProfile.name;
                String address = officeProfile.address;

                order.setAdress_office(address + ", " + name);

                Log.i("getOfficeFirebaseRes", "office: " + officeS);

                controller.setOfficeResources(name, address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public float calculatePriceAfterSale(long sale_f, float price, float old_sale_f) {
        float full_price;

        if (old_sale_f != 0 || sale_f != 0 )
            full_price = price * 100 / (100 - old_sale_f);

        else
            full_price = price;

        float sale = sale_f * full_price / 100;
        order.payment.setDiscount(sale_f);
        order.payment.setPrice(full_price-sale + "");

        return full_price - sale;
    }


    public void saveStornoStatus(int id)
    {
        Log.i("stornoo", "MODEL");
        OrderDAOInterface orderDAO = new OrderDAO();
        orderDAO.setStatus(id-1, "CA");
    }

    public void updateSaleAfterPay(float result_price, int old_sale, int id) {
        order.payment.setDiscount(old_sale);
        order.payment.setPrice(result_price + "");

        OrderDAOInterface orderDAO = new OrderDAO();
        orderDAO.setPayDetails(id, old_sale, result_price);
    }

    public void updatePaymentAfterPay(int id)
    {
        String date = getActualDate();
        String time = getActualTime();

        OrderDAOInterface orderDAO = new OrderDAO();
        orderDAO.setPayAfterPay(id, date, time);

        order.payment.setDate_pay(getActualDate());
        order.payment.setPaid(true);
    }

    public void updateStatusAfterPay(int id)
    {
        OrderDAOInterface orderDAO = new OrderDAO();
        orderDAO.setStatus(id-1, "CO");
       // order.payment.setPaid(true);
    }

    private String getActualDate() {
        SimpleDateFormat output_format = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date();

        return  output_format.format(date);
    }

    private String getActualTime() {

        SimpleDateFormat output_format = new SimpleDateFormat("H:mm:ss");
        Date date = new Date();

        return output_format.format(date);

    }

    public void loadPDF(Context context)
    {

        Log.i("pdfko", "facha1");
            Invoice invoice = new Invoice(order, context);
            //invoice.fetchCorporateInfo(context);

    }

    public void removeProductFromOffice() {
        ProductDAOInterface productDAO = new ProductDAO();

        for (int i = 0; i < order.inventory.getSize(); i++) {
            int register = order.inventory.getItem(i).getRegistration_num();
            productDAO.get().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        assert product != null;
                        if (product.getRegister_number() == register)
                        {

                            productDAO.removeItem(dataSnapshot.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }


}
