package com.jingge.sensorcollect;

import com.jingge.sensorcollect.pojo.AccData;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

public class reflectionTest {


    @Test
    public void aa() throws NoSuchFieldException {
        AccData accData = new AccData("1111", "2222", "3333", "4444");
        AccData accData1 = new AccData("1111", "2222", "3333", "4444");
        AccData accData2 = new AccData("1111", "2222", "3333", "4444");
        List<AccData> list = new ArrayList<>();
        list.add(accData);
        list.add(accData1);
        list.add(accData2);
//        System.out.println(list.getClass().getGenericSuperclass());
        bb(AccData.class);
    }

    public void bb(Class dataclass){
        System.out.println(dataclass.getSimpleName());
        System.out.println(dataclass==AccData.class);
        switch (dataclass.getSimpleName()){
            case "AccData":
                System.out.println(111);
                break;
        }
    }
}
