import com.excel.util.FormatUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Test2 {


    @Test
    public void test1(){
        String a = "343534223423123123223";
        double v = Double.parseDouble(a);
        System.out.println(v);
        String s = FormatUtil.toMaxScale(v * 10000, 6);
        System.out.println(s);


        NumberFormat nf = NumberFormat.getInstance();
        long l = Long.parseLong(a);
        String format = nf.format(l);
        System.out.println(format);
    }

    @Test
    public void test2(){
        String a = "3421.2334";
        double v = Double.parseDouble(a);
        String s = FormatUtil.toMaxScale(v , 1);
        System.out.println(s);
    }


    @Test
    public void test3(){
        String a = "343534223423123123223";

        BigDecimal b = new BigDecimal(a);
        int i = b.intValue();
        long l1 = b.longValue();
        double v = b.doubleValue();
        System.out.println(i);
        System.out.println(l1);
        System.out.println(v);
        System.out.println(b.toString());

        int i1 = b.intValueExact();
        System.out.println(i1);



/*
        NumberFormat nf = NumberFormat.getInstance();
        long l = Long.parseLong(a);
        String format = nf.format(l);
        System.out.println(format);*/
    }

    @Test
    public void test4(){
        String a = "abc.defg";
        String s = a.substring(0, a.length() - 1);
        System.out.println(s);

        System.out.println(a.indexOf("."));
        int len =  a.length() - 1 - a.indexOf(".");
        System.out.println(len);
    }


    @Test
    public void test5(){
        String a = "1342.36";
        String b = a.substring(a.indexOf(".")+1,a.indexOf(".")+2);
        System.out.println(b);
    }

    @Test
    public void test6(){
        String a = "399999999999999999999999999999999999999998.1234352";
        char[] chars = a.toCharArray();
//        System.out.println(chars);
//        BigDecimal b = new BigDecimal(a);
//        BigDecimal add = b.add(new BigDecimal("0.01"));
//        System.out.println(add.toString());

        String s = FormatUtil.toMaxScale(a, 10, 1,true);
        System.out.println(s);
    }


    @Test
    public void test7(){
        char a = '1';
        System.out.println((int)a);
        char b = '2';
        System.out.println((int)b);
        char c = '.';
        System.out.println((int)c);
        c= '0';
        System.out.println((int)c);
        c= '9';
        System.out.println((int)c);
    }

    @Test
    public void test8(){
        String a = ".893.4";
        String s = FormatUtil.toMaxScale(a, 6, 1, true);
        System.out.println(s);
    }


}
