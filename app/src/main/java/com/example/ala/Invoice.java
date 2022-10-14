package com.example.ala;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.barcodes.BarcodeQRCode;
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
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Invoice {
    String order_number, adress_office, date_order, date_pay, type_pay, customer_name, customer_email, customer_phone, result_price;
    String name, residence, ic, dic, website, contact, phone, bank_account, variable_symbol, logo_path;
    String datetime;
    long discount;
    Bitmap logo;
    List<Long> piecesofProduct = new ArrayList<>();
    List<String> namesofProduct = new ArrayList<>();
    List<Integer> registerNumsofProduct = new ArrayList<>();
    List<Double> pricesOfProduct = new ArrayList<>();
    final int DPH_percent = 21;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public Invoice() {
      //  fetchShopLogo();
    }

    public Invoice(String order_number, String adress_office, String date_order, String date_pay, String type_pay, String customer_name, String customer_email, String customer_phone) {
        this.order_number = order_number;
        this.adress_office = adress_office;
        this.date_order = date_order;
        this.date_pay = date_pay;
        this.type_pay = type_pay;
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.customer_phone = customer_phone;
    }

    public void removeAllPieces()
    {
        piecesofProduct.clear();
        namesofProduct.clear();
        registerNumsofProduct.clear();
        pricesOfProduct.clear();
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        Date date = new Date();
        this.datetime = formatter.format(date);
    }

    public void addPiecesofProduct(long adding_value){
        piecesofProduct.add(adding_value);
    }

    public void addNamesofProduct(String adding_value){
        namesofProduct.add(adding_value);
    }

    public void addRegisterNumsofProduct(int adding_value){
        registerNumsofProduct.add(adding_value);
    }

    public void addPricesOfProduct(double adding_value)
    {
        pricesOfProduct.add(adding_value);
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public void setVariable_symbol(String variable_symbol) {
        this.variable_symbol = variable_symbol;
    }

    public String getOrder_number() {
        return order_number;
    }

    public String getName() {
        return name;
    }

    public String getResidence() {
        return residence;
    }

    public String getIc() {
        return ic;
    }

    public String getDic() {
        return dic;
    }

    public String getWebsite() {
        return website;
    }

    public String getContact() {
        return contact;
    }

    public String getPhone() {
        return phone;
    }

    public String getBank_account() {
        return bank_account;
    }

    public String getVariable_symbol() {
        return variable_symbol;
    }

    public String getAdress_office() {
        return adress_office;
    }

    public String getDate_order() {
        return date_order;
    }

    public String getDate_pay() {
        return date_pay;
    }

    public String getType_pay() {
        return type_pay;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public long getDiscount() {
        return discount;
    }

    public String getResult_price() {
        return result_price;
    }

    public void setResult_price(String result_price) {
        this.result_price = result_price;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public void setAdress_office(String adress_office) {
        this.adress_office = adress_office;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }

    public void setDate_pay(String date_pay) {
        this.date_pay = date_pay;
    }

    public void setType_pay(String type_pay) {
        this.type_pay = type_pay;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }


    private void fetchShopLogo() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://ala-bk.appspot.com").child("shop-logo.jpg");

        try {
            final File file = File.createTempFile("logo","jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    logo = BitmapFactory.decodeFile(file.getAbsolutePath());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void fetchCorporateInfo(Context context) {

        readCorpoInfo(new FirebaseCallback() {
            @Override
            public void onCallBack() {

              /*  readProductsInfo(new FirebaseCallback(){
                    @Override
                    public void onCallBack() {*/

                        try {
                            createPDF(context);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                   /* }

                });*/


            }
        });



    }
    private void readCorpoInfo(FirebaseCallback firebaseCallback)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Corporate info");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                setName(snapshot.child("name").getValue().toString());
                setResidence(snapshot.child("residence").getValue().toString());
                setIc(snapshot.child("ič").getValue().toString());
                setDic(snapshot.child("dič").getValue().toString());
                setWebsite(snapshot.child("website").getValue().toString());
                setContact(snapshot.child("contact").getValue().toString());
                setPhone(snapshot.child("phone").getValue().toString());
                setBank_account(snapshot.child("bank account").getValue().toString());
                setVariable_symbol(snapshot.child("variable symbol").getValue().toString());
                setLogo_path(snapshot.child("logo").getValue().toString());

                firebaseCallback.onCallBack();



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readProductsInfo(FirebaseCallback firebaseCallback)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Order").child("Orders");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                setName(snapshot.child("name").getValue().toString());
                setResidence(snapshot.child("residence").getValue().toString());
                setIc(snapshot.child("ič").getValue().toString());
                setDic(snapshot.child("dič").getValue().toString());
                setWebsite(snapshot.child("website").getValue().toString());
                setContact(snapshot.child("contact").getValue().toString());
                setPhone(snapshot.child("phone").getValue().toString());
                setBank_account(snapshot.child("bank account").getValue().toString());
                setVariable_symbol(snapshot.child("variable symbol").getValue().toString());
                setLogo_path(snapshot.child("logo").getValue().toString());

                firebaseCallback.onCallBack();



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void createPDF(Context context) throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "faktura_"+ getOrder_number() +".pdf");
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
        float column_width3[] = {55,175,15,70,70,65,40,70};
        Table table_product = new Table(column_width3);

        /*--Table4--*/
        float column_width4[] = {380,75,105};
        Table table_price = new Table(column_width4);

        /*--Table4--*/
        float column_width5[] = {280,280};
        Table table_bottom = new Table(column_width5);


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
            table1.addCell(new Cell().add(new Paragraph("Záruční a dodací list")).setFont(font).setBorder(Border.NO_BORDER));
             /* BarcodeEAN barcodeEAN = new BarcodeEAN();
            barcodeEAN.setCode("5050505050");
            barcodeEAN.setCodeType(BarcodeEAN.CODE128);*/


            com.itextpdf.barcodes.BarcodeQRCode qrCode = new BarcodeQRCode("1010101010");

            PdfFormXObject object = qrCode.createFormXObject(ColorConstants.BLACK,pdfDocument);

            Image imageBarcode = new Image(object).setWidth(100f);
            table1.addCell(new Cell().add(imageBarcode).setBorder(Border.NO_BORDER));


            /*--Row1*/
            table_text.addCell(new Cell().add(new Paragraph("Prodávající: ").setFont(font).setFontSize(9).setUnderline().setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph(getName()).setFont(font).setBold().setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));



            /*--Row2-3*/
            table_text.addCell(new Cell(1,4).add(new Paragraph("Sídlo pobočky: "+ getAdress_office() +"," +" Sídlo společnosti: "+ getResidence()+"," +
                                                                                " IČ: "+ getIc()+"," +" DIČ: "+ getDic()+"," +" Internet: "+getWebsite()+"," +
                                                                                " Kontakt: "+ getContact()+", Telefon: "+ getPhone()+".").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row4*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Daňový doklad: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("Faktura").setFont(font).setBold().setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("Kupující:").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));

            /*--Row5*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum vystavění: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(getDate_order()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph(getCustomer_name()).setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER));

            /*--Row6*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum uskut. zdaň. plnění: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(getDate_pay()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("Email: " + getCustomer_email()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row7*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum splatnosti: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(getActualDate()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("Tel: "+ getCustomer_phone()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row8*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum převzetí: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(getActualDate()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row9*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Způsob úhrady: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(getType_pay()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));

            /*--Row9*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Bankovní účet: ").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));

            /*--Row10*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("ČSOB, a.s. (CZK): ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(getBank_account()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));

            /*--Row11*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Variabilní symbol: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(getVariable_symbol()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));


            /*--Row1*/

            //SolidBorder solid = new SolidBorder(black,new boolean[]{false,false,true,false});

            table_product.addCell(new Cell().add(new Paragraph("Kód").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("Popis").setFont(font).setBold().setFontSize(9).setCharacterSpacing(1).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("Ks").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("Cena Ks").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("bez DPH").setFont(font).setFontSize(9).setBold().setBold().setCharacterSpacing(1)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("DPH").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("DPH%").setFont(font).setFontSize(9).setBold().setBold().setCharacterSpacing(1)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("Cena").setFont(font).setFontSize(9).setBold().setBold().setCharacterSpacing(1)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));

            for(int i = 0; i < piecesofProduct.size(); i++)
            {
                table_product.addCell(new Cell().add(new Paragraph(String.valueOf(registerNumsofProduct.get(i))).setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
                table_product.addCell(new Cell().add(new Paragraph(namesofProduct.get(i)).setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
                table_product.addCell(new Cell().add(new Paragraph(piecesofProduct.get(i).toString()).setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
                table_product.addCell(new Cell().add(new Paragraph(setPriceFormat(pricesOfProduct.get(i).toString())).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                double DPH =  calculateDPH(pricesOfProduct.get(i));
                double priceWithoutDPH = calculatePriceWithoutDPH(pricesOfProduct.get(i), DPH);
               // Log.i("priceDPH", DPH + " " + priceWithoutDPH + " " + pricesOfProduct.get(i));

                table_product.addCell(new Cell().add(new Paragraph(setPriceFormat(String.valueOf(priceWithoutDPH))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                table_product.addCell(new Cell().add(new Paragraph(setPriceFormat(String.valueOf(DPH))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                table_product.addCell(new Cell().add(new Paragraph(DPH_percent + "%").setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

                double sumPrice = calculateSumPrice(pricesOfProduct.get(i),piecesofProduct.get(i));
                table_product.addCell(new Cell().add(new Paragraph(setPriceFormat(String.valueOf(sumPrice))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            }

            if(getDiscount() != 0) {
                    long discount = getDiscount();
                    table_product.addCell(new Cell().add(new Paragraph("SLEVA"+discount).setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                    table_product.addCell(new Cell().add(new Paragraph("Zákaznická sleva " + discount + "%").setFont(font).setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                    table_product.addCell(new Cell().add(new Paragraph("1").setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                    table_product.addCell(new Cell().add(new Paragraph(setPriceFormat(String.valueOf(getDiscountAmount(discount)))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                    table_product.addCell(new Cell().add(new Paragraph(setPriceFormat(String.valueOf(getDiscountAmount(discount)))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                    double DPH_discount = calculateDPH(getDiscountAmount(discount));
                    table_product.addCell(new Cell().add(new Paragraph(setPriceFormat(String.valueOf(DPH_discount))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                    table_product.addCell(new Cell().add(new Paragraph(DPH_percent + "%").setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                    table_product.addCell(new Cell().add(new Paragraph(setPriceFormat(String.valueOf(getDiscountAmount(discount)))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            }
            else {
                table_product.addCell(new Cell().add(new Paragraph().setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph().setFont(font).setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph().setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph().setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph().setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph().setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph().setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph().setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));

            }


            table_price.addCell(new Cell().add(new Paragraph().setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            table_price.addCell(new Cell().add(new Paragraph("Celkem:").setBold().setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            table_price.addCell(new Cell().add(new Paragraph(setPriceFormat(getResult_price()) + " Kč").setBold().setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            table_price.addCell(new Cell(1,3).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table_price.addCell(new Cell(1,3).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table_price.addCell(new Cell(1,3).add(new Paragraph("Doporučujeme zboží překontrolovat ihned po převzetí, " +
                                                                                "pozdější připomínky ke stavu předávaného zboží mohou být zamítnuty. " +
                                                                                "Kupující nabude vlastnického práva ke zboží až po úplném zaplacení kupní ceny. " +
                                                                                "V ceně zboží je zahrnut recyklační poplatek a autorské odměny v zákonné výši. " +
                                                                                "Více informací o podmínkách záruky naleznete ve Všeobecných obchodních podmínkách a Reklamačním řádu na " + getWebsite() + ".")
                                                                                .setFont(font).setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));

            setDatetime();
            table_bottom.setHeight(PageSize.A4.getHeight()-10);
            table_bottom.addCell(new Cell().add(new Paragraph().setFont(font).setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            table_bottom.addCell(new Cell().add(new Paragraph("Tisk: PDFGen " + getDatetime()).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

        } catch (IOException e) {
            e.printStackTrace();
        }
        // table1.addCell(new Cell().add(new Paragraph("")));






        document.add(table1);
        document.add(table_text);
        document.add(new Paragraph("\n"));
        document.add(table_product);
        document.add(table_price);
        document.add(table_bottom);
        document.close();

        Toast toast = Toast.makeText(context,"PDF was created",Toast.LENGTH_LONG);
        toast.show();

        Log.i("pdfko", "facha_after");

    }

    private float getDiscountAmount(long discount) {
       float original_price = 100 * Float.valueOf(getResult_price()) / (100 - discount);
       Log.i("original_price", original_price + " ");
       return original_price * discount / 100 * (-1);
    }

    private double calculateSumPrice(double price, Long count) {
        return price * count;
    }

    private double calculatePriceWithoutDPH(double price, double dph) {
        return price - dph;
    }

    private double calculateDPH(double price) {
        return price * DPH_percent / 100;
    }

    private String setPriceFormat(String price) {
        Log.i("priceDPH", price );
        double amount = Double.parseDouble(price);
        DecimalFormat formater = new DecimalFormat("###,###.00");

        return formater.format(amount);
    }

    private String getActualDate() {
        SimpleDateFormat output_format = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();

        return  output_format.format(date);
    }

    private interface FirebaseCallback{
        void onCallBack();
    }
}
