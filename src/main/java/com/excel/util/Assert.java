package com.excel.util;

import java.text.NumberFormat;

public class Assert {



    public static boolean isNull(Object ... objects){
        if(objects == null){
            return true;
        }
        for(Object o : objects){
            if(o==null){
                return true;
            }
            if(o.getClass() == String.class){
                String data = o.toString();
                if("".equals(data.trim()) || "null".equals(data)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Double a = 3234d;
        NumberFormat f = NumberFormat.getInstance();
        f.setMaximumFractionDigits(2);//如果不加默认是3位并且每百位带上,
        f.setGroupingUsed(false);
        String s = f.format(a);
        System.out.println(s);
    }
}
