package com.excel.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelUtil {

    public static HSSFCellStyle setHssFCellStyle(HSSFWorkbook workbook, String position, String font, int fontSize, boolean blod, boolean wrapText, boolean border) {
        if (workbook != null) {
            HSSFCellStyle style = workbook.createCellStyle();
            /**内容默认居左left，right居右，center居中***/
            switch (position) {
                case "LEFT":
                    style.setAlignment(HorizontalAlignment.LEFT);
                    break;
                case "RIGHT":
                    style.setAlignment(HorizontalAlignment.RIGHT);
                    break;
                case "CENTER":
                    style.setAlignment(HorizontalAlignment.CENTER);
                    break;
                default:
                    style.setAlignment(HorizontalAlignment.LEFT);
                    break;
            }
            //设置单元格内容是否自动换行
            if (border) {
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
            }
            //设置字体
            HSSFFont sFont = workbook.createFont();
            //默认宋体
            if (Assert.isNull(font)) {
                sFont.setFontName("宋体");
            } else {
                sFont.setFontName(font);
            }
            //设置字体
            if (fontSize <= 0) {
                sFont.setFontHeightInPoints((short) 13);
            } else {
                sFont.setFontHeightInPoints((short)fontSize);
            }

            //设置字体加粗
            sFont.setBold(blod);
            style.setFont(sFont);
            return style;
        }
        return null;
    }

    public static HSSFCell createRow(HSSFSheet sheet, int rowNum, String cellText, CellRangeAddress region, HSSFCellStyle style, int rowHeight) {
        if (!Assert.isNull(sheet)) {
            HSSFRow row = sheet.createRow(rowNum);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(new HSSFRichTextString(cellText));
            //设置行高
            if (rowHeight > 0) {
                row.setHeight((short) rowHeight);
            }
            //设置格式
            if (style != null) {
                cell.setCellStyle(style);
            }
            //合并单元格
            if (!Assert.isNull(region)) {
                sheet.addMergedRegion(region);
            }
            return cell;
        }
        return null;
    }


    public static void fillRowData(HSSFRow row, int rowHeight, HSSFCellStyle style, String[] CellValues) {
        if (row != null) {
            row.setHeight((short)rowHeight);
        }
        if(CellValues.length>0){
            for(int i=0;i<CellValues.length;i++){
                HSSFCell cell = row.createCell(i);
//                cell.setCellValue(new HSSFRichTextString(CellValues[i]));
                cell.setCellValue(Double.parseDouble(CellValues[i]));
                //一般单元格内容靠左或居中
                cell.setCellStyle(style);
            }
        }
    }

}
