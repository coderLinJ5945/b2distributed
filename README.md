# b2distributed
这是一个企业级分布式项目（转idea）

### 模块介绍
整合SSM框架（Spring+SpringMVC+Mybatis），采用前后端分离的架构方式开发的电商平台项目，服务器端源代码。

### 环境搭建
开发环境：
* window10
* Jdk7+
* Mysql
* idea
* FTP服务器搭建：

    1、解压FTPServer.zip，以管理员权限运行FTPServer.exe
    2、设置用户名、密码、共享目录启动

* Nginx反向代理服务：
 
    1、解压nginx压缩包
    2、修改如下文件：
    ```
     nginx.conf 文件
         server {
             listen       80; #端口号，如果被系统占用，可以修改一个没有被使用的端口
             server_name  image.linj.com; # ftp服务名称
             location / {
                 root   C:\ftpfile\image;
                 add_header Access-Control-Allow-Origin *; #允许访问所有类型
             }         
         }
     
     hosts 文件
     127.0.0.1    image.linj.com
     
    ```
    、

idea配置： 
1、修改

    
