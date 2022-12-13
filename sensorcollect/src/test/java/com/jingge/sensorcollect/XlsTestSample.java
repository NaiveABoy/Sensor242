//package com.jingge.sensorcollect;
//
////import com.alibaba.excel.EasyExcel;
////import com.alibaba.excel.annotation.ExcelIgnore;
////import com.alibaba.excel.annotation.ExcelProperty;
////import com.alibaba.excel.util.ListUtils;
//import com.jingge.sensorcollect.util.FileUtil;
//
//import org.junit.Test;
//
//import java.util.Date;
//import java.util.List;
//
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.Setter;
//
//public class XlsTestSample {
//
//    private List<DemoData> data() {
//        List<DemoData> list = ListUtils.newArrayList();
//        for (int i = 0; i < 10; i++) {
//            DemoData data = new DemoData();
//            data.setString("字符串" + i);
//            data.setDate(new Date());
//            data.setDoubleData(0.56);
//            list.add(data);
//        }
//        return list;
//    }
//
//    @Test
//    public void simpleWrite() {
//        String fileName = FileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
//        System.out.println(fileName);
//        EasyExcel.write(fileName, DemoData.class)
//                .sheet("模板")
//                .doWrite(() -> {
//                    // 分页查询数据
//                    return data();
//                });
//    }
//
//    @Getter
//    @Setter
//    @EqualsAndHashCode
//    public class DemoData {
//        @ExcelProperty("字符串标题")
//        private String string;
//        @ExcelProperty("日期标题")
//        private Date date;
//        @ExcelProperty("数字标题")
//        private Double doubleData;
//        /**
//         * 忽略这个字段
//         */
//        @ExcelIgnore
//        private String ignore;
//    }
//}
