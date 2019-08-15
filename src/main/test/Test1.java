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


    @Test
    public void test3(){
        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("NAME","AA");
        list.add(map);
        System.out.println(list);
        map.put("NATION","中国");
        System.out.println(list);
    }


    @Test
    public void test4(){
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println(list);
        for(int i = 0; i < list.size(); i++){
            Integer a = list.get(i);
            if(a == 2){
                list.remove(a);
            }
            if(a==1){
                list.remove(a);
            }
        }
        System.out.println(list);
    }

    @Test
    public void test5(){
        List<String> list = new ArrayList<>();
        list.add("AA");
        list.add("BB");
        list.add("CC");
        list.add("DD");
        for(int i=0;i<list.size();i++){
            String s = list.get(i);
            if("BB".equals(s)){
                list.remove(s);
            }
        }
        System.out.println(list);
    }

    @Test
    public void test6(){
        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("1","AA");
        map.put("NAME","A");
        list.add(map);
        map = new HashMap<>();
        map.put("2","BB");
        map.put("NAME","B");
        list.add(map);
        map = new HashMap<>();
        map.put("3","CC");
        map.put("NAME","C");
        list.add(map);
        map = new HashMap<>();
        map.put("4","DD");
        map.put("NAME","D");
        list.add(map);
        System.out.println(list);

        for(int i = 0;i<list.size();i++){
            Map<String, String> map1 = list.get(i);
            String name = map1.get("NAME");
            if("B".equals(name)){
                list.remove(map1);
                i--;
            }
            if("A".equals(name)){
                list.remove(map1);
                i--;
            }
        }
        System.out.println(list);
    }

}
