package com.pdf.service;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.ILeafElement;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.Underline;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 体验不一样的生成pdf。但是也是可以。
 */
public class CreatePdf {

    static void createPdf1() throws DocumentException, FileNotFoundException {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("E:\\java\\aa.pdf"));
        doc.open();

        Chunk chunk = new Chunk("Cat");
        chunk.setBackground(Color.BLUE);
        //
        Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
        font.setColor(Color.BLACK);
        chunk.setFont(font);
//        Phrase phrase = new Phrase();
//        doc.add(phrase);
        doc.add(chunk);

        chunk = new Chunk("Dog");
//        chunk.setUnderline();
        doc.add(chunk);

        doc.close();
    }

    static void createPdf2() throws DocumentException, FileNotFoundException {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("E:\\java\\aa.pdf"));
        doc.open();

        //建块
        Chunk chunk1 = new Chunk("Cat");
        Chunk chunk2 = new Chunk("DOG");

        //建短语
        Phrase phrase = new Phrase();
        phrase.add(chunk1);
        phrase.add(chunk2);
        phrase.add("Hello world");

        doc.add(phrase);

        //新建一行
        doc.add(Chunk.NEWLINE);
        doc.add(new Chunk("new line"));

        doc.close();

    }
    static void createPdf() throws DocumentException, FileNotFoundException {
        Document doc = new Document();
        PdfWriter.getInstance(doc , new FileOutputStream("E:\\java\\aa.pdf"));
        doc.open();

        //建块
        Chunk chunk1 = new Chunk("Cat");
        Chunk chunk2 = new Chunk("DOG");

        //建短语
        Phrase phrase = new Phrase();
        phrase.add(chunk1);
        phrase.add(chunk2);
        phrase.add("Hello world");

        //建段落
        Paragraph paragraph = new Paragraph();
        paragraph.add(phrase);
        paragraph.add("Hello World");

        //设置段落对齐方式
        paragraph.setAlignment(Element.ALIGN_LEFT);
        //设置缩进
        paragraph.setIndentationLeft(100f);

        Paragraph paragraph1 = new Paragraph();
        paragraph1.add("AA");

        //注意增加段落时会自动换行
        doc.add(paragraph);
        doc.add(paragraph1);

        doc.close();

    }


    static void createPDF4() throws com.itextpdf.text.DocumentException, FileNotFoundException {

        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new FileOutputStream("E:\\java\\aa.pdf"));
        com.itextpdf.text.Chunk chunk = new com.itextpdf.text.Chunk("AA");
        com.itextpdf.text.Phrase phrase = new com.itextpdf.text.Phrase("BB");
doc.open();
        doc.add(chunk);
        doc.add(phrase);

        com.itextpdf.text.Paragraph p = new com.itextpdf.text.Paragraph("CC");
        doc.add(p);
doc.close();

    }


    static void createPdf5() throws IOException {
        com.itextpdf.kernel.pdf.PdfWriter pdfWriter = new com.itextpdf.kernel.pdf.PdfWriter(new FileOutputStream("E:\\java\\aa.pdf"));
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        com.itextpdf.layout.Document doc = new com.itextpdf.layout.Document(pdfDocument, PageSize.A4);

        com.itextpdf.layout.element.Paragraph p = new com.itextpdf.layout.element.Paragraph("BCDE");
        doc.add(p);

//        PdfFont font = PdfFontFactory.createFont();
        PdfFont font = PdfFontFactory.createFont("E:\\my\\pdf\\tool\\fzfs_GBK.ttf", PdfEncodings.IDENTITY_H,false);
        p = new com.itextpdf.layout.element.Paragraph("EEE以有为GGGG");//中文不显示。
//        p.setUnderline(1, 3);
        Style style = new Style();
        style.setFont(font);
        style.setBold();
        style.setFontSize(30);
//        p.setUnderline(com.itextpdf.kernel.color.Color.RED, 3, 5, 0, 0, 0, 2);
        p.setUnderline();
        p.setKeepTogether(true);
        p.setKeepWithNext(true);
        p.addStyle(style);
        doc.add(p);

        p = new com.itextpdf.layout.element.Paragraph("哈哈我知道了。。");
        p.addStyle(style);
        doc.add(p);

        p = new com.itextpdf.layout.element.Paragraph("44");
        p.setHeight(15);
        p.addStyle(style);
        p.setFixedPosition(60f, 500f, 15);
        doc.add(p);

        //给一个段落中个别字加下划线
        p = new com.itextpdf.layout.element.Paragraph("我的");
        Text t = new Text("aaaaaa");
        t.setUnderline();
        p.add(t);
        t = new Text("bbbb");
        p.add(t);
        t = new Text("cccc");
        p.add(t);
        p.addStyle(style);
        doc.add(p);



        doc.close();
    }

    public static void main(String[] args) {
        try {
            createPdf5();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
