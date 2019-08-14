package com.excel.util;

import java.text.NumberFormat;

public class FormatUtil {

    /**
     * 最多保留的小数位数
     * @param value
     * @param scale
     * @return
     */
    public static String toMaxScale(Double value,int scale){
        NumberFormat f = NumberFormat.getInstance();
        f.setMaximumFractionDigits(scale);//如果不加默认是3位并且每百位带上,
        f.setGroupingUsed(false);
        return f.format(value);
    }





}
