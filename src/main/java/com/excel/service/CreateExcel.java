package com.excel.service;

import com.excel.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.*;
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
        HSSFRow row2 = sheet.createRow(2);
        ExcelUtil.fillRowData(row1,800,styleCommon,new String[]{"55","66"});
        ExcelUtil.fillRowData(row2,800,styleCommon,new String[]{"88","99"});
        HSSFRow row3 = sheet.createRow(3);
        HSSFRow row4 = sheet.createRow(4);
        ExcelUtil.fillRowData(row4,800,styleCommon,new String[]{"21","56"});
        HSSFRow row5 = sheet.createRow(5);
        ExcelUtil.fillRowData(row5,800,styleCommon,new String[]{"7","9"});
        HSSFRow row6 = sheet.createRow(6);
        ExcelUtil.fillRowData(row6,800,styleCommon,new String[]{"32","98"});

        //插入公式
        HSSFCell cell = row1.createCell(2);
        cell.setCellFormula("$A2+$B2");
        cell.setCellStyle(styleCommon);
        //如果公式不起作用就加上下面两句代码
//        HSSFFormulaEvaluator he = new HSSFFormulaEvaluator(workbook);
//        he.evaluateInCell(cell);

        HSSFRow row7 = sheet.createRow(7);
        row7.setHeight((short)800);
        cell = row7.createCell(0);
        String str = "SUM(A2:A7)";
        cell.setCellFormula(str);
//        HSSFFormulaEvaluator he = new HSSFFormulaEvaluator(workbook);
//        he.evaluateInCell(cell);
        cell.setCellStyle(styleCommon);




//        cell = row2.getCell(0);
//        cell.setCellValue(93);
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
