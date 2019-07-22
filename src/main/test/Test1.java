import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1 {


    @Test
    public void test1(){

        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        list.add(map);
        map.put("NAME", "李炎");
        System.out.println(list);

    }

    @Test
    public void test2(){
        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();
        l1.add("李炎");
        l2.add("加油");
        l1.addAll(l2);
        System.out.println(l1);
        System.out.println(l2);
    }



}
