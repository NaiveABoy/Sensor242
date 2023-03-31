package com.jingge.sensorcollect;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class compressTest {

    @Test
    public void ziptest() throws Exception {
        String output = "/D:/test.zip";
        String input = "/D:/1.txt|/D:/2.txt|";
        System.out.println(Arrays.toString(input.split("\\|")));
//        zip(input, output, null);
    }

    public static void zip(String input, String output, String name) throws Exception {
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

    public static void z7z(String input, String output, String name) throws Exception {
        try {
            SevenZOutputFile sevenZOutput = new SevenZOutputFile(new File(output));
            SevenZArchiveEntry entry = null;

            String[] paths = input.split("\\|");
            File[] files = new File[paths.length];
            for (int i = 0; i < paths.length; i++) {
                files[i] = new File(paths[i].trim());
            }
            for (int i = 0; i < files.length; i++) {
                BufferedInputStream instream = new BufferedInputStream(new FileInputStream(paths[i]));
                if (name != null) {
                    entry = sevenZOutput.createArchiveEntry(new File(paths[i]), name);
                } else {
                    entry = sevenZOutput.createArchiveEntry(new File(paths[i]), new File(paths[i]).getName());
                }
                sevenZOutput.putArchiveEntry(entry);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = instream.read(buffer)) > 0) {
                    sevenZOutput.write(buffer, 0, len);
                }
                instream.close();
                sevenZOutput.closeArchiveEntry();
            }
            sevenZOutput.close();
        } catch (IOException ioe) {
            System.out.println(ioe.toString() + "  " + input);
        }
    }
}
