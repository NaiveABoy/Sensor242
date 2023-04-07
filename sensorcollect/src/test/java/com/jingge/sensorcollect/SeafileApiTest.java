package com.jingge.sensorcollect;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingge.sensorcollect.data.*;

import org.junit.Test;

import okhttp3.OkHttpClient;

import java.io.File;
import java.util.List;


public class SeafileApiTest {
    public static final String SERVICE_URL = "http://10.3.244.162:8000";
    public static final String FILE_SERVER_ROOT = "http://10.3.244.162:8082";

    @Test
    public void testPing() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);

        String ping = api.ping(client);
        System.out.println("ping = " + ping);
    }

    @Test
    public void testObtainAuthToken() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);

        String token = api.obtainAuthToken(client, "caojingge4@foxmail.com", "Wxmlcy6220015.");
        System.out.println("token = " + token);
    }

    @Test
    public void testCheckAccountInfo() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);

        String token = api.obtainAuthToken(client, "caojingge4@foxmail.com", "Wxmlcy6220015.");
        JSONObject accountInfo = api.checkAccountInfo(client, token);
        System.out.println("account info = " + JSON.toJSONString(accountInfo));
    }

    @Test
    public void testGetServerInformation() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        JSONObject serverInformation = api.getServerInformation(client);
        System.out.println("server info = " + JSON.toJSONString(serverInformation));
    }

    @Test
    public void testListLibraries() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "caojingge4@foxmail.com", "Wxmlcy6220015.");
        List<Library> list = api.listLibraries(client, token);
        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    public void testGetUploadLink() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "caojingge4@foxmail.com", "Wxmlcy6220015.");
        String uploadLink = api.getUploadLink(client, token, "6e58654f-0705-453a-943f-305c22c433c8", "/曹竞舸/");
        System.out.println(uploadLink);
    }

    @Test
    public void testListAllDirEntries() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "caojingge4@foxmail.com", "Wxmlcy6220015.");
        List<DirectoryEntry> dirs = api.listAllDirEntries(client, token, "6e58654f-0705-453a-943f-305c22c433c8");
        for (DirectoryEntry dir: dirs){
            System.out.println(dir.getName());
        }
    }

    @Test
    public void testListDirEntriesByP() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "caojingge4@foxmail.com", "Wxmlcy6220015.");
        List<DirectoryEntry> dirs = api.listDirEntriesByP(client, token, "6e58654f-0705-453a-943f-305c22c433c8", "/吴旭老师/");
        for (DirectoryEntry dir: dirs){
            System.out.println(dir.getName());
        }
    }

    @Test
    public void testUploadFile() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "caojingge4@foxmail.com", "Wxmlcy6220015.");
        File file1 = new File("D:/test.xlsx");
        String uploadLink = api.getUploadLink(client, token, "6e58654f-0705-453a-943f-305c22c433c8", "/");
        List<UploadFileRes> uploadFileResList = api.uploadFile(client, token, uploadLink, "/", "曹竞舸/", file1);
        System.out.println(uploadFileResList.size());
    }


    public void testCreateFile() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);

        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.createFile(client, token, "0de4d65b-732a-4d13-8304-d4f2bc26437e", "/aaa/aa1.txt");
    }

    public void testCreateNewLibrary() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");

        JSONObject jsonObject = api.createNewLibrary(client, token, "Personal", "我的个人仓库", "Passw0rd");
        System.out.println(JSON.toJSONString(jsonObject));
    }

    public void testDeleteLibrary() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.deleteLibrary(client, token, "0bcd60f9-3a9a-4498-b1b8-8a365b0c1428");
    }

    public void testGetFileDownloadLink() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        String downloadLink = api.getFileDownloadLink(client, token, "0de4d65b-732a-4d13-8304-d4f2bc26437e", "/删除所有索引.txt", true);
        System.out.println(downloadLink);
    }

    public void testCreateNewAccount() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.createNewAccount(client, token, "hahaha@gmail.com", "Passw0rd");
    }

    public void testDeleteAccount() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.deleteAccount(client, token, "wwb@gmail.com");
    }

    public void testListStarredFiles() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        List<StarredFile> starredFiles = api.listStarredFiles(client, token);
        System.out.println(starredFiles.size());
    }

    public void testGetLibraryInfo() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        Library library = api.getLibraryInfo(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a");
        System.out.println(library.getName());
    }

    public void testGetLibraryHistory() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        List<LibraryHistory> histories = api.getLibraryHistory(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a");
        System.out.println(histories.size());
    }

    public void testGetFileDetail() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        FileDetail detail = api.getFileDetail(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/Documents/readme.txt");
        System.out.println(detail.getName());
    }

    public void testGetFileHistory() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        List<FileCommit> fileCommits = api.getFileHistory(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/Documents/readme.txt");
        System.out.println(fileCommits.size());
    }

    public void testRenameFile() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.renameFile(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/Documents/about.txt", "readme.txt");
    }

    public void testMoveFile() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.moveFile(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/seafile-tutorial.doc", "7f178180-27e3-4f97-b4b1-7f720795938a", "/Documents/");
    }

    public void testRevertFile() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.revertFile(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/Documents/readme.txt", "8621b4883bf87f2b501bdfe4edbd0cc62ed7b33d");
    }

    public void testDeleteFile() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.deleteFile(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/Documents/seafile-tutorial.doc");
    }

    public void testGetUpdateLink() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        System.out.println(api.getUpdateLink(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/Documents/"));
        System.out.println(api.getUpdateLink(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/"));
        System.out.println(api.getUpdateLink(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", ""));
    }

    public void testUpdateFile() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        String updateLink = api.getUpdateLink(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "");
        api.updateFile(client, token, updateLink, new File("E:/seafile-tutorial.doc"), "/Documents/seafile-tutorial.doc");
    }

    public void testCreateNewDir() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.createNewDir(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/Documents/literature");
    }

    public void testRenameDir() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.renameDir(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/Documents/literature", "food");
    }

    public void testDeleteDir() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.deleteDir(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/Documents/food");
    }

    public void testGetDirDownloadToken() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        String dirDownloadToken = api.getDirDownloadToken(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/", "Documents");
        System.out.println(dirDownloadToken);
    }

    public void testGetDirDownloadLink() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        String dirDownloadToken = api.getDirDownloadToken(client, token, "7f178180-27e3-4f97-b4b1-7f720795938a", "/Documents", "Documents");
        System.out.println(api.getDirDownloadLink(client, token, dirDownloadToken));
    }

}
