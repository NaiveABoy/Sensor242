package com.jingge.sensorcollect.util;

import com.jingge.sensorcollect.util.data.*;

import java.io.File;
import java.util.List;

import okhttp3.OkHttpClient;

public class UploadUtil {

    public static final String SERVICE_URL = "http://10.3.244.162:8000";
    public static final String FILE_SERVER_ROOT = "http://10.3.244.162:8082";

    public void testUploadFile() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "caojingge4@foxmail.com", "Wxmlcy6220015.");
        File file1 = new File("D:/test.xlsx");
        String uploadLink = api.getUploadLink(client, token, "6e58654f-0705-453a-943f-305c22c433c8", "/");
        List<UploadFileRes> uploadFileResList = api.uploadFile(client, token, uploadLink, "/", "曹竞舸/", file1);
        System.out.println(uploadFileResList.size());
    }
}
