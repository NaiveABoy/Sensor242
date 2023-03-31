package com.jingge.sensorcollect.util;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressUtil {

    /**
     * @Author Jingge
     * @Date 2023/3/31
     * @Param input 要压缩的文件路径字符串（以|分隔）
     * output 输出的压缩文件路径
     * name 指定压缩文件名
     * @Return
     */
    public static void zip(@NonNull String input, String output, String name) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(output));
        String[] paths = input.split("\\|");
        File[] files = new File[paths.length];
        byte[] buffer = new byte[1024];
        for (int i = 0; i < paths.length; i++) {
            files[i] = new File(paths[i]);
        }
        for (File file : files) {
            FileInputStream fis = new FileInputStream(file);
            if (files.length == 1 && name != null) {
                out.putNextEntry(new ZipEntry(name));
            } else {
                out.putNextEntry(new ZipEntry(file.getName()));
            }
            int len;
            // 读入需要下载的文件的内容，打包到zip文件
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            fis.close();
        }
        out.close();
    }
}
