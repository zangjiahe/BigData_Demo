import com.youran.controller.DataControll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test {
//    @org.junit.Test
//    public void Test() throws InterruptedException, IOException, ClassNotFoundException {
//        new DataControll().startJob("I:\\bigdata\\seq10w","I:\\bigdata\\out0011");
//    }
    @org.junit.Test
    public void testList(){
        List<Integer> value_list=new ArrayList<>();
        value_list.add(1);
        value_list.add(2);
        value_list.add(3);
        value_list.add(4);
        Integer[] value1=new Integer[value_list.size()];
        value1=value_list.toArray(new Integer[0]);
        for (int num :value1) {
            System.out.println(num);
        }
    }
    @org.junit.Test
    public void testTimeStmap(){
//        long tmp= (int) ;//2020-06-12 14:34:02
//        Date date=new Date("2020-06-12 14:34:02".replace("-","/"));
        System.out.println(System.currentTimeMillis()-new Date("2020/06/12 14:34:02").getTime());
//        System.out.println(new Date(req.getParameter("timestmap"));
    }

}
