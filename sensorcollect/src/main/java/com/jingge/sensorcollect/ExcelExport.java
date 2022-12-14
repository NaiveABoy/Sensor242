package com.jingge.sensorcollect;

import android.content.Context;

import com.jingge.sensorcollect.util.ExcelUtil;

import java.io.File;
import java.util.List;

public class ExcelExport {


    public static void exportExcel(String path, String fileName, Class dataClass, List<?> dataList, Context context) {
        String[] title;
        String sheetName = dataClass.getSimpleName();

        //根据传感器类型获取文件存储子目录，若不存在则创建
        path = path + File.separatorChar + sheetName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        //文件的真实存储路径
        path = path + File.separatorChar + fileName + ".xlsx";

        switch (sheetName) {
            case "LocationData":
                title = new String[]{"timestamp", "经度", "纬度", "海拔", "定位精度（米）", "定位来源"};
                break;
            case "AccData":
                title = new String[]{"timestamp", "X轴加速度", "Y轴加速度", "Z轴加速度"};
                break;
            case "GravityData":
                title = new String[]{"timestamp", "X轴重力", "Y轴重力", "Z轴重力"};
                break;
            case "GyroData":
                title = new String[]{"timestamp", "X轴旋转速率", "Y轴旋转速率", "Z轴旋转速率"};
                break;
            case "HumidData":
                title = new String[]{"timestamp", "环境相对湿度"};
                break;
            case "LightData":
                title = new String[]{"timestamp", "光照强度"};
                break;
            case "MagneData":
                title = new String[]{"timestamp", "X轴磁场强度", "Y轴磁场强度", "Z轴磁场强度"};
                break;
            case "OrientData":
                title = new String[]{"timestamp", "设备绕X轴旋转度数", "设备绕Y轴旋转度数", "设备绕Z轴旋转度数"};
                break;
            case "PressData":
                title = new String[]{"timestamp", "环境压强"};
                break;
            case "ProxData":
                title = new String[]{"timestamp", "接近屏幕距离"};
                break;
            case "RotationData":
                title = new String[]{"timestamp", "旋转矢量x*sin(θ/2)", "旋转矢量y*sin(θ/2)", "旋转矢量z*sin(θ/2)", "旋转矢量cos(θ/2)"};
                break;
            case "TempData":
                title = new String[]{"timestamp", "环境温度"};
                break;
            default:
                title = new String[]{"timestamp", "value1", "value2", "value3", "value4"};
                break;
        }

        ExcelUtil.initExcel(path, sheetName, title);
        ExcelUtil.writeObjListToExcel(dataList, sheetName, path, context);
    }
}
