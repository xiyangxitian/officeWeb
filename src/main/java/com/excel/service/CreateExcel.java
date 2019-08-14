package com.excel.service;

import com.excel.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;

public class CreateExcel {
    public static void main(String[] args) {

        new CreateExcel().createExcel();
    }

    //常量
    private final String LEFT = "LEFT";
    private final String RIGHT = "RIGHT";
    private final String CENTER = "CENTER";
    public void createExcel() {
        HSSFWorkbook workbook = new HSSFWorkbook();//xls
//        XSSFWorkbook workbook1 = new XSSFWorkbook();//xlsx
        //样式
        HSSFSheet sheet = workbook.createSheet("第一页");
        /****设置格式***/
        HSSFCellStyle styleCommon = ExcelUtil.setHssFCellStyle(workbook, CENTER, "宋体", 12, true, false, false);

        //设置列宽
        sheet.setColumnWidth(0,2000);
        sheet.setColumnWidth(1,4000);

        //创建行
//        ExcelUtil
        ExcelUtil.createRow(sheet, 0, "秘密", new CellRangeAddress(0, 0, 0, 1), styleCommon, 0);

        HSSFRow row1 = sheet.createRow(1);
        ExcelUtil.fillRowData(row1,800,styleCommon,new String[]{"11","22"});

//        workbook.setSheetHidden(1,true);

        //保存文件到本地
        FileOutputStream stream = null;
        String sFilePath = "E:\\java\\excell";
        String filePath = "";
        String fileName = "a.xls";

        File file = new File(sFilePath);
        if(!file.exists()){
            file.mkdirs();
        }
        filePath = sFilePath + File.separator + fileName;
        try {
            stream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            workbook.write(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
