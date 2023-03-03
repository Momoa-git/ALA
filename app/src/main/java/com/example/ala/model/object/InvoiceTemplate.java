package com.example.ala.model.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class InvoiceTemplate{

    Invoice invoice;
    Context context;

    public InvoiceTemplate(Invoice invoice, Context context){
       this.invoice = invoice;
       this.context = context;
    }


    public File createPDF() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file;
        file = new File(pdfPath, "faktura_"+ invoice.order.getOrder_number() +".pdf");

        Log.i("pdfko", "facha " + pdfPath);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        com.itextpdf.layout.Document document = new Document(pdfDocument);

        /*--Table1--*/
        float[] column_width = {140,100,180,140};
        Table table1 = new Table(column_width);

        /*--Table2--*/
        float[] column_width2 = {70,110,100,280};
        Table table_text = new Table(column_width2);

        /*--Table3--*/
        float[] column_width3 = {55,175,15,70,70,65,40,70};
        Table table_product = new Table(column_width3);

        /*--Table4--*/
        float[] column_width4 = {380,75,105};
        Table table_price = new Table(column_width4);

        /*--Table4--*/
        float[] column_width5 = {280,280};
        Table table_bottom = new Table(column_width5);


        /*--Row1*/
        Bitmap bitmap = invoice.corpoInfo.getLogo_bitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData2 = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData2);
        Image logo = new Image(imageData);
        logo.setHeight(100);

        PdfFont font;
        // table1.addCell(new Cell(3,1).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell(3,1).add(logo).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        try {
            String path = "/res/font/arialce.ttf";
            font = PdfFontFactory.createFont(path, BaseFont.IDENTITY_H, true);
            Paragraph p = new Paragraph("Faktura - Daňový doklad - " + invoice.getSerial_number()).setFont(font).setFontSize(14).setBold().setCharacterSpacing(1);
            table1.addCell(new Cell(1,2).add(p).setBorder(Border.NO_BORDER));

            /*--Row2*/
            // table1.addCell(new Cell().add(new Paragraph("")));
            table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table1.addCell(new Cell().add(new Paragraph("Záruční a dodací list")).setFont(font).setBorder(Border.NO_BORDER));


            com.itextpdf.barcodes.BarcodeQRCode qrCode = new BarcodeQRCode(invoice.getSerial_number()+"");

            PdfFormXObject object = qrCode.createFormXObject(ColorConstants.BLACK,pdfDocument);

            Image imageBarcode = new Image(object).setWidth(100f);
            table1.addCell(new Cell().add(imageBarcode).setBorder(Border.NO_BORDER));


            /*--Row1*/
            table_text.addCell(new Cell().add(new Paragraph("Prodávající: ").setFont(font).setFontSize(9).setUnderline().setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph(invoice.corpoInfo.getName()).setFont(font).setBold().setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));



            /*--Row2-3*/
            table_text.addCell(new Cell(1,4).add(new Paragraph("Sídlo pobočky: "+ invoice.order.getAdress_office() +"," +" Sídlo společnosti: "+ invoice.corpoInfo.getResidence()+"," +
                    " IČ: "+ invoice.corpoInfo.getIc()+"," +" DIČ: "+ invoice.corpoInfo.getDic()+"," +" Internet: "+ invoice.corpoInfo.getWebsite()+"," +
                    " Kontakt: "+ invoice.corpoInfo.getContact()+", Telefon: "+ invoice.corpoInfo.getPhone()+".").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row4*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Daňový doklad: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("Faktura").setFont(font).setBold().setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("Kupující:").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));

            /*--Row5*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum vystavění: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(invoice.order.getDate_order()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph(invoice.order.getCustomer_name()).setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER));

            /*--Row6*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum uskut. zdaň. plnění: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(invoice.order.getDate_pay()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("Email: " + invoice.order.getCustomer_email()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row7*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum splatnosti: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(invoice.getActualDate()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("Tel: "+ invoice.order.getCustomer_phone()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row8*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Datum převzetí: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(invoice.getActualDate()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));

            /*--Row9*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Způsob úhrady: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(invoice.order.getType_pay()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));

            /*--Row9*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Bankovní účet: ").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));

            /*--Row10*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("ČSOB, a.s. (CZK): ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(invoice.corpoInfo.getBank_account()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));

            /*--Row11*/
            table_text.addCell(new Cell(1,2).add(new Paragraph("Variabilní symbol: ").setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            // table_text.addCell(new Cell().add(new Paragraph("Smile Shop s.r.o.").setBold().setFontSize(10).setCharacterSpacing(1)));
            table_text.addCell(new Cell().add(new Paragraph(invoice.corpoInfo.getVariable_symbol()).setFont(font).setFontSize(9)).setBorder(Border.NO_BORDER));
            table_text.addCell(new Cell().add(new Paragraph("").setFont(font).setFontSize(9).setBold()).setBorder(Border.NO_BORDER));


            /*--Row1*/


            table_product.addCell(new Cell().add(new Paragraph("Kód").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("Popis").setFont(font).setBold().setFontSize(9).setCharacterSpacing(1).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("Ks").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("Cena Ks").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("bez DPH").setFont(font).setFontSize(9).setBold().setBold().setCharacterSpacing(1)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("DPH").setFont(font).setFontSize(9).setBold().setCharacterSpacing(1)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("DPH%").setFont(font).setFontSize(9).setBold().setBold().setCharacterSpacing(1)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
            table_product.addCell(new Cell().add(new Paragraph("Cena").setFont(font).setFontSize(9).setBold().setBold().setCharacterSpacing(1)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));

            for(int i = 0; i < invoice.inventory.getSize(); i++)
            {
                table_product.addCell(new Cell().add(new Paragraph(String.valueOf(invoice.inventory.getItem(i).getRegister_number())).setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
                table_product.addCell(new Cell().add(new Paragraph(invoice.inventory.getItem(i).getName()).setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
                table_product.addCell(new Cell().add(new Paragraph(String.valueOf(invoice.inventory.getItem(i).getPiece())).setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
                table_product.addCell(new Cell().add(new Paragraph(invoice.setPriceFormat(String.valueOf(invoice.inventory.getItem(i).getPrice_double()))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                double DPH =  invoice.calculateDPH(invoice.inventory.getItem(i).getPrice_double());
                double priceWithoutDPH = invoice.calculatePriceWithoutDPH(invoice.inventory.getItem(i).getPrice_double(), DPH);

                table_product.addCell(new Cell().add(new Paragraph(invoice.setPriceFormat(String.valueOf(priceWithoutDPH))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                table_product.addCell(new Cell().add(new Paragraph(invoice.setPriceFormat(String.valueOf(DPH))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                table_product.addCell(new Cell().add(new Paragraph(invoice.DPH_percent + "%").setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

                double sumPrice = invoice.calculateSumPrice(invoice.inventory.getItem(i).getPrice_double(), invoice.inventory.getItem(i).getPiece());
                table_product.addCell(new Cell().add(new Paragraph(invoice.setPriceFormat(String.valueOf(sumPrice))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            }

            if(invoice.order.getDiscount() != 0) {
                long discount = invoice.order.getDiscount();
                table_product.addCell(new Cell().add(new Paragraph("SLEVA"+discount).setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph("Zákaznická sleva " + discount + "%").setFont(font).setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph("1").setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph(invoice.setPriceFormat(String.valueOf(invoice.getDiscountAmount(discount)))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph(invoice.setPriceFormat(String.valueOf(invoice.getDiscountAmount(discount)))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                double DPH_discount = invoice.calculateDPH(invoice.getDiscountAmount(discount));
                table_product.addCell(new Cell().add(new Paragraph(invoice.setPriceFormat(String.valueOf(DPH_discount))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph(invoice.DPH_percent + "%").setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
                table_product.addCell(new Cell().add(new Paragraph(invoice.setPriceFormat(String.valueOf(invoice.getDiscountAmount(discount)))).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1f)));
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
            table_price.addCell(new Cell().add(new Paragraph(invoice.setPriceFormat(invoice.order.getPrice()) + " Kč").setBold().setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            table_price.addCell(new Cell(1,3).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table_price.addCell(new Cell(1,3).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            table_price.addCell(new Cell(1,3).add(new Paragraph("Doporučujeme zboží překontrolovat ihned po převzetí, " +
                    "pozdější připomínky ke stavu předávaného zboží mohou být zamítnuty. " +
                    "Kupující nabude vlastnického práva ke zboží až po úplném zaplacení kupní ceny. " +
                    "V ceně zboží je zahrnut recyklační poplatek a autorské odměny v zákonné výši. " +
                    "Více informací o podmínkách záruky naleznete ve Všeobecných obchodních podmínkách a Reklamačním řádu na " + invoice.corpoInfo.getWebsite() + ".")
                    .setFont(font).setFontSize(9).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));

            invoice.setDatetime();
            table_bottom.setHeight(PageSize.A4.getHeight()-10);
            table_bottom.addCell(new Cell().add(new Paragraph().setFont(font).setFontSize(9).setCharacterSpacing(1).setCharacterSpacing(1)).setBorder(Border.NO_BORDER));
            table_bottom.addCell(new Cell().add(new Paragraph("Tisk: PDFGen " + invoice.getDatetime()).setFont(font).setFontSize(9).setCharacterSpacing(1)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

        } catch (IOException e) {
            e.printStackTrace();
        }


        document.add(table1);
        document.add(table_text);
        document.add(new Paragraph("\n"));
        document.add(table_product);
        document.add(table_price);
        document.add(table_bottom);
        document.close();

        Toast toast = Toast.makeText(context,"PDF document was created",Toast.LENGTH_LONG);
        toast.show();

        Log.i("pdfko", "facha_after");

        return file;

    }



}
