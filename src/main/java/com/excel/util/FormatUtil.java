package com.excel.util;

import java.math.BigDecimal;
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
        f.setMaximumFractionDigits(scale);
        f.setGroupingUsed(false);//如果不加默认是3位并且每百位带上,
        return f.format(value);
    }

    /**
     * 最多保留几位小数，字符串的情况，这个要做大数据的问题
     * @param value  数据
     * @param scale  精度
     * @param mode  截断的模式 1.四舍五入  其他截断
     * @param showZero  最后的零要不要显示
     * @return
     */
    public static String toMaxScale(String value,int scale,int mode,boolean showZero){
        //验证数据
        if(value.startsWith(".")){
            value = 0 + value;
        }
        if(value.indexOf(".") != value.lastIndexOf(".")){
//            System.out.println("数据value不合法，包含多个.");
            try {
                throw new Exception("数据value不是数字！");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        char[] chars = value.toCharArray();
        for(char c : chars){
            int cs = (int)c;
            if(cs != 46 && (cs < 48 || cs > 57) ){
//                System.out.println("数据value不合法!");
                try {
                    throw new Exception("数据value不是数字！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        //如果有小数点的情况 先去掉最后面的0
        if(value.contains(".") && scale >= 1){
            while(!showZero && value.endsWith("0")){
                value = value.substring(0,value.length()-1);
            }
            //结尾是不是小数点
            if(value.endsWith(".")){
                value = value.substring(0,value.length()-1);
            }
            if(value.contains(".")){
                //判断小数点后的位数
                int len =  value.length() - 1 - value.indexOf(".");
                if(len > scale){
                    if(mode == 1){ //模式1  四舍五入
                        //精度的下五位  如3.145  精度是2  下一位就是5
                        String lastIndexValue = value.substring(value.indexOf(".")+scale+1,value.indexOf(".")+scale+2);
                        int i = Integer.parseInt(lastIndexValue);
                        if(i>=5){
                            //相应位加1
                            value = value.substring(0,value.indexOf(".")+scale+1);
                            BigDecimal b = new BigDecimal(value);
                            StringBuffer sb = new StringBuffer("0.");
                            for(int j = 1;j < scale;j++){
                                sb.append("0");
                            }
                            sb.append("1");
                            BigDecimal b1 = new BigDecimal(sb.toString());
                            BigDecimal sum = b.add(b1);//这个数会带.0 这种格式，所以要再次执行来保证格式是符合要求的。
                            value = sum.toString();
                            value = toMaxScale(value,scale,mode,showZero);
                        }else{
                            value = value.substring(0,value.indexOf(".")+scale+1);
                        }
                    }else{ //直接截取相应的位数
                        value = value.substring(0,value.indexOf(".")+scale+1);
                    }
                }
            }
        }else{
            if(scale<=0 && value.contains(".")){
                value = value.substring(0,value.indexOf("."));
            }
        }
        if(showZero && scale>=1){
            if(value.contains(".")){
                String s = value.substring(value.indexOf(".") + 1);
                if(s.length()<scale){
                    StringBuffer sb = new StringBuffer();
                    for(int i = 0 ;i < scale - s.length();i++){
                        sb.append("0");
                    }
                    value = value + sb.toString();
                }
            }else{
                StringBuffer sb = new StringBuffer();
                while(scale-- > 0){
                    sb.append("0");
                }
                value = value + "." + sb.toString();
            }
        }
        return value;
    }




}
