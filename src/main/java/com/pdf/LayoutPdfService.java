package com.pdf;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.*;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

public class LayoutPdfService {

    static void createPdf5() throws IOException {

        PdfFont font = PdfFontFactory.createFont("E:\\my\\pdf\\tool\\fzfs_GBK.ttf", PdfEncodings.IDENTITY_H,false);
        Style style = new Style();
        style.setFont(font);
        style.setBold();
        style.setFontSize(30);

        PdfWriter pdfWriter = new PdfWriter(new FileOutputStream("E:\\java\\aa.pdf"));
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document doc = new Document(pdfDocument, PageSize.A4);

/*        Div div = new Div();
//        div.setRelativePosition(10, 10, 10, 10);
        div.addStyle(style);
//        div.setBorder(SolidBorder.NO_BORDER);
//        div.setBorder(new SolidBorder(5));
//        div.setBorder(new DashedBorder(3));
//        div.setBorder(new DoubleBorder(3));
        div.setBorder(new SolidBorder(3));
        div.setFillAvailableArea(true);
        doc.add(div);*/

        ListItem li = new ListItem("第二个");
        li.setFillAvailableArea(true);
        li.addStyle(style);
        li.setBorder(new SolidBorder(3));
        doc.add(li);

        Paragraph p = new Paragraph("BCD");
        doc.add(p);
        doc.add(p);
        doc.close();
    }

    public static void main(String[] args) throws IOException {
        createPdf5();
    }

}