package com.jingge.sensorcollect;

//import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.ExcelWriter;
//import com.alibaba.excel.util.ListUtils;
//import com.alibaba.excel.write.metadata.WriteSheet;
import com.jingge.sensorcollect.pojo.AccData;
import com.jingge.sensorcollect.util.FileUtil;

import org.junit.Test;

import java.util.List;


public class XlsTest {

    String fileName = FileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
//    List<AccData> accDataList = ListUtils.newArrayList();

    @Test
    public void repeatedWrite() {
//        AccData accData = new AccData("1111", "2222", "3333", "4444");
//        accDataList.add(accData);
//        accDataList.add(accData);
//        accDataList.add(accData);
        System.out.println(fileName);
//        try (ExcelWriter excelWriter = EasyExcel.write(fileName, AccData.class).build()) {
//            WriteSheet writeSheet = EasyExcel.writerSheet("Accelerometer").build();
//            excelWriter.write(accDataList, writeSheet);
//        }
    }

}
