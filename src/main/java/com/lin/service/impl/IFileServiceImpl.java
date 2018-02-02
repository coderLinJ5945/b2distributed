package com.lin.service.impl;

import com.google.common.collect.Lists;
import com.lin.service.IFileService;
import com.lin.util.FTPUtil;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class IFileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(IFileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        String fileName =  file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        String newFileName = UUID.randomUUID().toString() + "."+ suffix;
        File fileDir = new File(path+"/"+newFileName);
        if(!fileDir.exists()){
            fileDir.mkdirs();
            fileDir.setWritable(true);//文件写入权限
        }
        try {
            file.transferTo(fileDir);
            //fileDir.delete();
        } catch (IOException e) {
            logger.info("文件上传失败：", e);
            return null;
        }
        return fileDir.getName();

    }

    public String upload2(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        //扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        try {
            //将页面文件写入到硬盘中，再讲硬盘中的文件写入到ftp服务器
            file.transferTo(targetFile);
            //图片文件已经上传成功了
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到ftp服务器上
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
