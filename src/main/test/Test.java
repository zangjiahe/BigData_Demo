import com.youran.controller.DataControll;

import java.io.IOException;

public class Test {
    @org.junit.Test
    public void Test() throws InterruptedException, IOException, ClassNotFoundException {
        new DataControll().startJob("I:\\bigdata\\seq10w","I:\\bigdata\\out0011");
    }
}
