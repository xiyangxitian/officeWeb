package com.pdf.service;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import org.aspectj.weaver.ast.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


/**
 * @author Administrator
 * @version $Id$
 * @since
 * @see
 */
public class Word2PdfUtil {

    public static void main(String[] args) {

        //doc2pdf("E:\\my\\cc.doc", "E:\\my\\dd.pdf");

       word2pdf("E:\\my\\pdf\\new.docx", "E:\\my\\pdf\\new1.pdf");
    }

    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = Test.class.getClassLoader().getResourceAsStream("license.xml"); // license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();

            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void doc2pdf(String inPath, String outPath) {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        try {
            long old = System.currentTimeMillis();
            File file = new File(outPath); // 新建一个空白pdf文档
            FileOutputStream os = new FileOutputStream(file);
            Document doc = new Document(inPath); // Address是将要被转化的word文档
            doc.save(os, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
            // EPUB, XPS, SWF 相互转换
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void word2pdf(String docPath,String savePath){
        try {
            String s = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType><SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber></Data><Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature></License>";
            ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
            License license = new License();
            license.setLicense(is);
            com.aspose.words.Document document = new com.aspose.words.Document(docPath);
            document.save(new FileOutputStream(new File(savePath)),SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
