package com.pdf.service;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.Document;

import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePDFService {
    //
    private Style hongtouStyle;
    private Style wenhaoStyle;
    private Style beizhuStyle;
    private Style biaotiStyle;
    //仿宋 正文用
    private Style style;
    private Style zDingYiStyle;

    public static CreatePDFService getInstance(){
        CreatePDFService instance = new CreatePDFService();
        try {
            PdfFont wenhaoFont = PdfFontFactory.createFont("E:\\my\\pdf\\tool\\fzfs_GBK.ttf", PdfEncodings.IDENTITY_H,false);
            PdfFont beizhuFont = PdfFontFactory.createFont("E:\\my\\pdf\\tool\\ht.ttf", PdfEncodings.IDENTITY_H,false);
            PdfFont biaotiFont = PdfFontFactory.createFont("E:\\my\\pdf\\tool\\fzxbsjt.ttf", PdfEncodings.IDENTITY_H,false);
            PdfFont font = PdfFontFactory.createFont("E:\\my\\pdf\\tool\\fs_GB2312.ttf", PdfEncodings.IDENTITY_H,false);
            instance.wenhaoStyle = new Style();
            instance.wenhaoStyle.setFont(wenhaoFont).setFontSize(16).setBold();
            instance.wenhaoStyle.setFontColor(Color.RED);
//            instance.wenhaoStyle.setUnderline();


            instance.beizhuStyle = new Style();
            instance.beizhuStyle.setFont(beizhuFont).setFontSize(12).setBold();
            instance.biaotiStyle = new Style();
            instance.biaotiStyle.setFont(biaotiFont).setFontSize(22).setBold();
            instance.style = new Style();
            instance.style.setFont(font).setFontSize(12);

            instance.hongtouStyle = new Style();
            instance.hongtouStyle.setFont(wenhaoFont).setFontSize(20)
                    .setBold().setFontColor(Color.RED);
            instance.hongtouStyle.setBorder(new SolidBorder(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static void main(String[] args) {
        CreatePDFService.getInstance().cratePdfByTemplate();
/*        String templatePath = "E:\\my\\pdf\\cc.pdf";
        try {
            insertImage(templatePath);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    void createPdf(){
        //创建PDF文档
        Document document = new Document();

        try {
            FileOutputStream fos = new FileOutputStream("E:\\my\\aa.pdf");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void cratePdfByTemplate(){
        try {
            FileOutputStream fos = new FileOutputStream("E:\\my\\pdf\\outFile.pdf");
//            PdfReader pdfReader = new PdfReader("E:\\my\\pdf\\cc.pdf");

            PdfWriter pdfWriter = new PdfWriter(fos);
            //
//            PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            com.itextpdf.layout.Document doc = new com.itextpdf.layout.Document(pdfDocument, PageSize.A4);

            doc.setMargins(0, 0, 50, 0);


         /*   Paragraph p = getFirstLineIndentParagraph("1", 80, 80);
            doc.add(p);
            p = getFirstLineIndentParagraph("2", 80, 80);
            doc.add(p);
            p = getFirstLineIndentParagraph("3", 80, 80);
            doc.add(p);
            p = getFirstLineIndentParagraph("4", 80, 80);
            doc.add(p);*/


//            Paragraph p = new Paragraph("深圳市监察委员会");
//            p.addStyle(hongtouStyle);
//            p.setTextAlignment(TextAlignment.CENTER);
//            doc.add(p);

            //参数
            //1.线宽度
            //2.直线长度，是个百分百，0-100之间
            //3.直线颜色
            //4.直线位置
            //5.上下移动位置
//            com.itextpdf.text.pdf.draw.LineSeparator line = new com.itextpdf.text.pdf.draw.LineSeparator(2f,100,BaseColor.RED,Element.ALIGN_CENTER,-5f);
//            DashedLine dl = new DashedLine();
//            DottedLine dl2 = new DottedLine();
//            SolidLine sl = new SolidLine();
//            sl.setLineWidth(2);
//            sl.setColor(Color.RED);
//            LineSeparator line = new LineSeparator(sl);
//            line.setHeight(200);
//            doc.add(line);
////            doc.add(new Paragraph());
//            sl.setLineWidth(1);
//            line = new LineSeparator(sl);
//            doc.add(line);


            Paragraph p = new Paragraph("报告标题");
            p.addStyle(biaotiStyle);
            p.setTextAlignment(TextAlignment.CENTER);
            doc.add(p);

//            doc.add(new AreaBreak(AreaBreakType.NEXT_AREA));
            p = new Paragraph("省监察委员会：");
            p.addStyle(wenhaoStyle);
            p.setTextAlignment(TextAlignment.LEFT);
            p.setMarginLeft(80);
            p.setMarginRight(80);
            doc.add(p);

            String str = "我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人我是一个中国人。";
            p = getFirstLineIndentParagraph(str, 80, 80);
            p.setUnderline();
            doc.add(p);

            doc.add(new Paragraph("\r\n"));

            p = new Paragraph("深圳市监察委员会\n");
            p.add("2019年7月17日");
            p.setTextAlignment(TextAlignment.RIGHT);
            p.setMarginTop(80);
            p.setMarginLeft(80);
            p.setMarginRight(80);
            p.addStyle(wenhaoStyle);
            doc.add(p);


/*
            Image image = new Image(ImageDataFactory.create("E:\\my\\pdf\\tool\\gongzhang.png"));
            image.setAutoScale(true);
            image.setMarginTop(-80);
            image.setMarginLeft(80);
            float imageHeight = image.getImageHeight();
//            image.set
            doc.add(image);
*/

            doc.close();
            pdfDocument.close();
            pdfWriter.close();
//            pdfReader.close();
            fos.close();

        } catch (IOException e) {

        }

    }


    private Paragraph getFirstLineIndentParagraph(String str,float marginLeft,float marginRight){
        Paragraph p = new Paragraph(str);
        p.addStyle(wenhaoStyle);
        p.setFirstLineIndent(30);
        p.setTextAlignment(TextAlignment.LEFT);
        p.setMarginLeft(marginLeft);
        p.setMarginRight(marginRight);
        return p;
    }

}
