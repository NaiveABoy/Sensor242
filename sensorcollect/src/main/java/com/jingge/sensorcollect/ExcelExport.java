package com.jingge.sensorcollect;

import android.content.Context;

import com.jingge.sensorcollect.util.ExcelUtil;

import java.io.File;
import java.util.List;

public class ExcelExport {


    public static void exportExcel(String path, String fileName, Class dataClass, List<?> dataList, Context context, int action_no) {
        String[] title;
        String sheetName = dataClass.getSimpleName();

        //根据采集时间获取文件存储子目录，若不存在则创建
        path = path + File.separatorChar + fileName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        //文件的真实存储路径
        path = path + File.separatorChar + sheetName + ".xlsx";

        switch (sheetName) {
            case "LocationData":
                title = new String[]{"timestamp", "经度", "纬度", "海拔", "定位精度（米）", "定位来源", "动作类型"};
                break;
            case "AccData":
                title = new String[]{"timestamp", "X轴加速度", "Y轴加速度", "Z轴加速度", "动作类型"};
                break;
            case "GravityData":
                title = new String[]{"timestamp", "X轴重力", "Y轴重力", "Z轴重力", "动作类型"};
                break;
            case "GyroData":
                title = new String[]{"timestamp", "X轴旋转速率", "Y轴旋转速率", "Z轴旋转速率", "动作类型"};
                break;
            case "HumidData":
                title = new String[]{"timestamp", "环境相对湿度", "动作类型"};
                break;
            case "LightData":
                title = new String[]{"timestamp", "光照强度", "动作类型"};
                break;
            case "MagneData":
                title = new String[]{"timestamp", "X轴磁场强度", "Y轴磁场强度", "Z轴磁场强度", "动作类型"};
                break;
            case "OrientData":
                title = new String[]{"timestamp", "设备绕X轴旋转度数", "设备绕Y轴旋转度数", "设备绕Z轴旋转度数", "动作类型"};
                break;
            case "PressData":
                title = new String[]{"timestamp", "环境压强", "动作类型"};
                break;
            case "ProxData":
                title = new String[]{"timestamp", "接近屏幕距离", "动作类型"};
                break;
            case "RotationData":
                title = new String[]{"timestamp", "旋转矢量x*sin(θ/2)", "旋转矢量y*sin(θ/2)", "旋转矢量z*sin(θ/2)", "旋转矢量cos(θ/2)", "动作类型"};
                break;
            case "TempData":
                title = new String[]{"timestamp", "环境温度", "动作类型"};
                break;
            default:
                title = new String[]{"timestamp", "value1", "value2", "value3", "value4", "动作类型"};
                break;
        }

        ExcelUtil.initExcel(path, sheetName, title);
        ExcelUtil.writeObjListToExcel(dataList, sheetName, path, context, action_no);
    }
}
