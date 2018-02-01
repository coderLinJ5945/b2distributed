package com.lin.service.impl;

import com.lin.service.IFileService;
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
            //todo 暂时只上传到tomcat零时文件 后续需要上传到ftp文件服务器中
            file.transferTo(fileDir);
            //fileDir.delete();
        } catch (IOException e) {
            logger.info("文件上传失败：", e);
            return null;
        }
        return fileDir.getName();

    }
}
