package com.pdf.service;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 体验不一样的生成pdf。但是也是可以。
 */
public class CreatePdf {

    public static void main(String[] args) {
        try {
            createPdf();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

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



}
