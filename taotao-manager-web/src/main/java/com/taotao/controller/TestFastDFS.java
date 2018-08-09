package com.taotao.controller;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.taotao.utils.FastDFSClient;

public class TestFastDFS {

    @Test
    public void uploadFile()throws Exception{
        //1、项工程中添加jar
        //2、创建一个配置文件。配置tarcker服务器地址
        //3、加载配置文件
        ClientGlobal.init("E:\\taotao\\taotao\\taotao-manager-web\\src\\main\\resources\\resource\\client.conf");
        //4、创建一个TarckerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //5、使用TarckerClient对象获得tarckerserver对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //6、创建一个StorageServer的引用null就行
        StorageServer storageServer=null;
        //7、创建StorageClient对象，tarckerserver、StorageServer两个参数
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //8、使用StorageClient对象上传文件
        String[] strings = storageClient.upload_appender_file("G:/4.jpg", "jpg", null);
        for(String string : strings) {
            System.out.println(string);
        }
    }
    
    @Test
    public void testFastDFSClient() throws Exception{
    	FastDFSClient fastDFSClient = new FastDFSClient("E:\\\\taotao\\\\taotao\\\\taotao-manager-web\\\\src\\\\main\\\\resources\\\\resource\\\\client.conf");
    	String string = fastDFSClient.uploadFile("G:/gril.jpg");
    	System.out.println(string);
    }
    
}
