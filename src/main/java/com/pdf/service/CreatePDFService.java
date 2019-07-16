package com.pdf.service;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.*;

public class CreatePDFService {
    //
    private Style wenhaoStyle;
    private Style beizhuStyle;
    private Style biaotiStyle;
    //仿宋 正文用
    private Style style;

    public static CreatePDFService getInstance(){
        CreatePDFService instance = new CreatePDFService();
        try {
            PdfFont wenhaoFont = PdfFontFactory.createFont("E:\\my\\pdf\\tool\\fzfs_GBK.ttf", PdfEncodings.IDENTITY_H,false);
            PdfFont beizhuFont = PdfFontFactory.createFont("E:\\my\\pdf\\tool\\ht.ttf", PdfEncodings.IDENTITY_H,false);
            PdfFont biaotiFont = PdfFontFactory.createFont("E:\\my\\pdf\\tool\\fzxbsjt.ttf", PdfEncodings.IDENTITY_H,false);
            PdfFont font = PdfFontFactory.createFont("E:\\my\\pdf\\tool\\fs_GB2312.ttf", PdfEncodings.IDENTITY_H,false);
            instance.wenhaoStyle = new Style();
            instance.wenhaoStyle.setFont(wenhaoFont).setFontSize(16).setBold();
            instance.beizhuStyle = new Style();
            instance.beizhuStyle.setFont(beizhuFont).setFontSize(12).setBold();
            instance.biaotiStyle = new Style();
            instance.biaotiStyle.setFont(biaotiFont).setFontSize(22).setBold();
            instance.style = new Style();
            instance.style.setFont(font).setFontSize(12);
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
            PdfReader pdfReader = new PdfReader("E:\\my\\pdf\\cc.pdf");

            PdfWriter pdfWriter = new PdfWriter(fos);
            //
            PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);

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


            Paragraph p = new Paragraph();
            p.addStyle(biaotiStyle);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);

//            doc.add(new AreaBreak(AreaBreakType.NEXT_AREA));


            p = new Paragraph("报告文件头");
            p.addStyle(biaotiStyle);
            p.setTextAlignment(TextAlignment.CENTER);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);
            doc.add(p);

            doc.add(p);

            doc.close();
            pdfDocument.close();
            pdfWriter.close();
            pdfReader.close();
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

    static void insertImage(String templatePath) throws Exception {
        // 生成的文件路径
        String targetPath = "E:\\my\\pdf\\target.pdf";
        // 书签名
        String fieldName = "BB";
        // 图片路径
        String imagePath = "C:\\Users\\ly\\Pictures\\1.jpg";

        // 读取模板文件
        InputStream input = new FileInputStream(new File(templatePath));
//        PdfReader reader = new PdfReader(input);
        com.itextpdf.text.pdf.PdfReader reader = new com.itextpdf.text.pdf.PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(targetPath));
        // 提取pdf中的表单
        AcroFields form = stamper.getAcroFields();
        form.addSubstitutionFont(BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));

        // 通过域名获取所在页和坐标，左下角为起点
/*        int pageNo = form.getFieldPositions(fieldName).get(0).page;
        Rectangle signRect = form.getFieldPositions(fieldName).get(0).position;
        float x = signRect.getLeft();
        float y = signRect.getBottom();*/

        // 读图片
        Image image = Image.getInstance(imagePath);
        // 获取操作的页面
        PdfContentByte under = stamper.getOverContent(1);
        // 根据域的大小缩放图片
        //image.scaleToFit(signRect.getWidth(), signRect.getHeight());
        // 添加图片
        image.setAbsolutePosition(0, 0);
        under.addImage(image);

        stamper.close();
        reader.close();
    }
}
