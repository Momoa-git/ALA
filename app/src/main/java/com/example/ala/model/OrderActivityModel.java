package com.example.ala.model;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.ala.Office;
import com.example.ala.Order;
import com.example.ala.Product;
import com.example.ala.R;
import com.example.ala.controller.OrderActivityController;
import com.example.ala.view.OrderActivityView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.pdf417.PDF417Writer;
import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.text.Element;
import com.itextpdf.text.TabStop;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.parser.LineDashPattern;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class OrderActivityModel{

    private FirebaseDatabase firebaseDatabase, firebaseDatabase2, firebaseDatabase3, firebaseDatabase4;
    private DatabaseReference databaseReference, databaseReference2, databaseReference3, databaseReference4;
    private FirebaseAuth mAuth;
    Order order = new Order();
   //boolean semaphore = true;
    int count;
    int id_order_firebase;
    private OrderActivityController controller;
    private Context context;
    ArrayList<String> names_product = new ArrayList<String>(2);

    public OrderActivityModel(OrderActivityController controller) {
        this.controller = controller;
    }

    public void setRecViewContent() {
        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser office = mAuth.getCurrentUser();
        String id = office.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();//
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Order").child("Orders");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);


                    if (order.getOffice().contains(id)) {
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

    public int getOrderID(int position) {
        return controller.onOrderID(position);

    }

    public void getOrderFirebaseResources(int id_order) {
        firebaseDatabase2 = FirebaseDatabase.getInstance();
        databaseReference2 = firebaseDatabase2.getReference().child("Order").child("Orders");

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id_order_firebase_STR = dataSnapshot.child("id_order").getValue().toString();
                    id_order_firebase = Integer.valueOf(id_order_firebase_STR);

                    if (id_order_firebase == id_order) {
                        String order_number = dataSnapshot.child("order_number").getValue().toString();
                        String date_order = dataSnapshot.child("date").getValue().toString();
                        String time_order = dataSnapshot.child("time").getValue().toString();
                        String status = dataSnapshot.child("status").getValue().toString();
                        String office = dataSnapshot.child("office").getValue().toString();

                        //Piece and name of products
                        String name_products = "";
                        long iteration = dataSnapshot.child("Product item").getChildrenCount();

                        for (int count = 0; count < iteration; count++) {
                            String name = dataSnapshot.child("Product item").child(String.valueOf(count)).child("name").getValue().toString();
                            String pieces_of_product = dataSnapshot.child("Product item").child(String.valueOf(count)).child("piece").getValue().toString();
                            if(name_products.isEmpty())
                                name_products = "(" + pieces_of_product + "ks) " + name;
                            else
                                name_products = name_products + "\n" + "(" + pieces_of_product + "ks) " + name;
                        }

                        String id_customer = dataSnapshot.child("id_customer").getValue().toString();
                        //Payment detail
                        String type_pay = dataSnapshot.child("Payment").child("type").getValue().toString();
                        boolean paid = Boolean.parseBoolean(dataSnapshot.child("Payment").child("paid").getValue().toString());
                        String price = dataSnapshot.child("Payment").child("price").getValue().toString();
                        String possibleDiscount = checkPossibleDiscount(dataSnapshot);
                        String possibleDatePay = checkPosssibleDatePay(dataSnapshot, paid);


                        Log.i("getOrderFirebaseRes", "Num.order: " + order_number + ", Status: " + status + ", TypePay: " + type_pay + " Paid: " + paid + ", NameProducts: " + name_products+"" + " DatePay: " + possibleDatePay);


                        String paidAfterParse = setPaidTitle(paid);
                        String typePayAfterParse = setTypeTitle(type_pay);
                        String priceAfterParse = setPriceFormat(price);
                        String dateAfterParse = setDateFormat(date_order);

                        controller.setOrderResources(order_number, dateAfterParse, time_order, status, name_products, typePayAfterParse, paidAfterParse, priceAfterParse, possibleDiscount, possibleDatePay);

                        //semaphore = true;
                        getCustomerFirebaseResources(Integer.parseInt(id_customer));
                        getOfficeFirebaseResources(office);
                       // getProductListFirebaseResources(array_id_list_product);


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

        } catch (NullPointerException e) {
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
            } catch (NumberFormatException n) {
                return "ERR format exception";
            }
        } catch (NullPointerException e) {
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

    public String setPriceFormat(String price) {

        double amount = Double.parseDouble(price);
        DecimalFormat formater = new DecimalFormat("###,###.00");

        return formater.format(amount);

    }

    private String setTypeTitle(String type_pay) {
        if (type_pay.equals("card"))
            return "Kartou";
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

    public void getCustomerFirebaseResources(int id_customer) {

        firebaseDatabase2 = FirebaseDatabase.getInstance();
        databaseReference2 = firebaseDatabase2.getReference().child("Customer").child("Customers");

        databaseReference2.addValueEventListener(new ValueEventListener() {
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

    private void getProductListFirebaseResources(String[] arr) {
        firebaseDatabase2 = FirebaseDatabase.getInstance();
        databaseReference2 = firebaseDatabase2.getReference().child("Product").child("Products");

        names_product.clear();

        count = 0;

        for (int i = 0; i < arr.length; i++) {
            Log.i("getProductListFirebRes", "iterace: " + i);
            databaseReference2.child(arr[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Product product = snapshot.getValue(Product.class);


                        String name_product = snapshot.child("name").getValue().toString();

                        names_product.add(name_product);
                        // name[count] = name_product;

                        //   product.setName(name_product);

                        Log.i("getProductListFirebRes", "product name: " + name_product);


                        Log.i("getProductListFirebRes", "product names: " + names_product);

                        if (count == arr.length - 1) {
                            String nameStream = Arrays.toString(names_product.toArray()).replace("[", "").replace("]", "").replace(",", "\n");
                            // product.setName(out);
                            Log.i("getProductListFirebRes", "product names: " + nameStream);
                            names_product.clear();
                            controller.setProductListResources(nameStream);

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


    public float calculatePriceAfterSale(float sale_f, float price, float old_sale_f) {

        float full_price;

        if (old_sale_f != 0)
            full_price = price * 100 / (100 - old_sale_f);

        else
            full_price = price;

        float sale = sale_f * full_price / 100;
        return full_price - sale;
    }

    //setPriceFormat(String.valueOf(
    public void saveDiscountInDB(float sale_F, int id) {
        firebaseDatabase3 = FirebaseDatabase.getInstance();
        databaseReference3 = firebaseDatabase3.getReference().child("Order").child("Orders");
        databaseReference3.child(String.valueOf(id - 1)).child("Payment").child("discount").setValue(sale_F);


    }

    public void saveNewPriceInDB(float result_price, int id) {
        firebaseDatabase4 = FirebaseDatabase.getInstance();
        databaseReference4 = firebaseDatabase3.getReference().child("Order").child("Orders");
        databaseReference4.child(String.valueOf(id - 1)).child("Payment").child("price").setValue(result_price);
    }

    public void hash(float sale_F, float result_price, int id)
    {
        firebaseDatabase3 = FirebaseDatabase.getInstance();
        databaseReference3 = firebaseDatabase3.getReference().child("Order").child("Orders").child(String.valueOf(id - 1)).child("Payment");

        Map update = new HashMap();
        update.put("discount", sale_F);
        update.put("price", result_price);


        databaseReference3.updateChildren(update);
    }

    public void saveStornoStatus(int id)
    {
        Log.i("stornoo", "MODEL");
        firebaseDatabase3 = FirebaseDatabase.getInstance();
        databaseReference3 = firebaseDatabase3.getReference().child("Order").child("Orders");

        databaseReference3.child(String.valueOf(id - 1)).child("status").setValue("CA");

    }

    public void updateAfterPayment(float result_price, int old_sale, int id, String paid) {
        firebaseDatabase3 = FirebaseDatabase.getInstance();
        databaseReference3 = firebaseDatabase3.getReference().child("Order").child("Orders").child(String.valueOf(id - 1)).child("Payment");

        Map updatepay = new HashMap();
        updatepay.put("discount", old_sale);
        updatepay.put("price", result_price);
        if (paid.equals("NE")) {
            updatepay.put("date_pay", getActualDate());
            updatepay.put("time_pay", getActualTime());
            updatepay.put("paid", true);
        }

        databaseReference3.updateChildren(updatepay);

        firebaseDatabase4 = FirebaseDatabase.getInstance();
        databaseReference4 = firebaseDatabase4.getReference().child("Order").child("Orders").child(String.valueOf(id - 1));

        Map update = new HashMap();
        update.put("status", "CO");

        databaseReference4.updateChildren(update);

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
      /*  ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);

*/
        Log.i("pdfko", "facha1");
        try {
            createPDF(context);
        } catch (FileNotFoundException e) {
            Log.i("pdfko", "facha3");
            e.printStackTrace();
        }
    }




    public void createPDF(Context context) throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "mypdfko.pdf");
        FileOutputStream outputStream = new FileOutputStream(file);

        Log.i("pdfko", "facha " + pdfPath);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        com.itextpdf.layout.Document document = new Document(pdfDocument);

        DeviceRgb white = new DeviceRgb(255,255,255);
        DeviceRgb black = new DeviceRgb(0,0,0);

        /*--Table1--*/
        float column_width[] = {140,100,180,140};
        Table table1 = new Table(column_width);

        /*--Table2--*/
        float column_width2[] = {70,110,100,280};
        Table table_text = new Table(column_width2);

        /*--Table3--*/
        float column_width3[] = {30,210,70,70,70,40,70};
        Table table_product = new Table(column_width3);

        /*--Row1*/
        Drawable shopLogo = ContextCompat.getDrawable(context, R.drawable.shop_logo);
        Bitmap bitmap = ((BitmapDrawable)shopLogo).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData2 = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData2);
        Image logo = new Image(imageData);
        logo.setHeight(100);

        PdfFont font;

        table1.addCell(new Cell(3,1).add(logo).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        try {
               // BaseFont baseFont = BaseFont.createFont("c:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, true);
                String path = "/res/font/arialce.ttf";
                //String name = getResource(path).toString();
                 font = PdfFontFactory.createFont(path, BaseFont.IDENTITY_H, true);
                Paragraph p = new Paragraph("Faktura - Daňový doklad - 0000000000").setFont(font).setFontSize(14).setBold().setCharacterSpacing(1);
                table1.addCell(new Cell(1,2).add(p).setBorder(Border.NO_BORDER));

            /*--Row2*/
            // table1.addCell(new Cell().add(new Paragraph("")));
            table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table1.addCell(new Cell().add(new Paragraph("Záruční a dodací list")).setBorder(Border.NO_BORDER));
             /* BarcodeEAN barcodeEAN = new BarcodeEAN();
            barcodeEAN.setCode("5050505050");
            barcodeEAN.setCodeType(BarcodeEAN.CODE128);*/


            com.itextpdf.barcodes.BarcodeQRCode qrCode = new BarcodeQRCode("1010101010");

            PdfFormXObject object = qrCode.createFormXObject(ColorConstants.BLACK,pdfDocument);

            Image imageBarcode = new Image(object).setWidth(100f);
            table1.addCell(new Cell().add(imageBarcode).setBorder(Border.NO_BORDER));
/*

            //table1.addCell(new Cell().add(new Paragraph("")));
            table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table1.addCell(new Cell().add(new Paragraph("Záruční a dodací list").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
*/



            /*--Row1*/
            table_text.addCell(new Cell().add(new Paragraph("Prodávající: ").setFont(font).setFontSize(9).setUnderline().setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setFont(font).setBold().setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

            /*--Row2-3*/
            table_text.addCell(new Cell(1,4).add(new Paragraph("Sídlo pobočky: Nádražní 2924/75, 70200 Ostrava 1, Sídlo společnosti: Českých Legií 75, 70200 Ostrava 1, IČ: 27082689, DIČ: CZ270697664," +
                    " Internet: www.smile-shop.cz, Kontakt: www.smile-shop.cz/kontakt, Telefon: +420722231123").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row4*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Daňový doklad: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
           // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("Faktura").setFont(font).setBold().setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("Kupující:").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));

            /*--Row5*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum vystavění: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("12.6.2022").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("Adam Daniel").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER));

            /*--Row6*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum uskut. zdaň. plnění: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("13.6.2022").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("Email: adam.dany@seznam.cz").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row7*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum splatnosti: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("14.6.2022").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("Tel: +420722231123").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row8*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum převzetí: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("15.6.2022").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row9*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Způsob úhrady: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("Kartou Internetem").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));

            /*--Row9*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Bankovní účet: ").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));

            /*--Row10*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("ČSOB, a.s. (CZK): ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("277505042 / 0300").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));

            /*--Row11*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Variabilní symbol: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("4560322251").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));


            /*--Row1*/

            //SolidBorder solid = new SolidBorder(black,new boolean[]{false,false,true,false});

            table_product.addCell(new Cell().add(new Paragraph("Ks").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("Popis").setBold().setFontSize(9).setCharacterSpacing(1).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("Cena Ks").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("bez DPH").setFont(font).setFontSize(9).setBold().setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("DPH").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("DPH%").setFont(font).setFontSize(9).setBold().setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("Cena").setFont(font).setFontSize(9).setBold().setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));


        } catch (IOException e) {
            e.printStackTrace();
        }
       // table1.addCell(new Cell().add(new Paragraph("")));






        document.add(table1);
        document.add(table_text);
        document.add(new Paragraph("\n"));
        document.add(table_product);
        document.close();

        Log.i("pdfko", "facha_after");

    }



}
