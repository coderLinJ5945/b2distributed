package com.lin.controller.backstage;

import com.lin.common.Constant;
import com.lin.common.ResponseCodeEnum;
import com.lin.common.ServerResponse;
import com.lin.pojo.Product;
import com.lin.pojo.User;
import com.lin.service.IFileService;
import com.lin.service.IProductService;
import com.lin.service.IUserService;
import com.lin.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 *  商品相关接口（后台）
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    /**
     * 商品保存方法（新增、修改）ok
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "productSave.do", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        System.out.println();
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if(response.isSuccess()){
            //执行更新操作
            return iProductService.poductSaveOrUpdate(product);
        }
        return ServerResponse.createByError("无权限操作");
    }

    /**
     * 更新商品状态（逻辑删除）ok
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "setProductStatus.do", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ServerResponse setProductStatus(HttpSession session, Product product){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if(response.isSuccess()){
            //更新商品状态
            return iProductService.setProductStatus(product);
        }
        return ServerResponse.createByError("无权限操作");
    }

    /**
     * 产品 SearchList ,不传分页参数，默认是查询所有
     * @param session
     * @param productName
     * @param productId
     * @param pageNumber  用int的原因，设置了默认值，所以不可能为null，如果不设置默认值建议用Integer
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "searchList.do", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ServerResponse searchList(HttpSession session, String productName, Integer productId ,@RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if (response.isSuccess()) {
            return iProductService.searchList(productName,productId,pageNumber,pageSize);
        }
        return ServerResponse.createByError("无权限操作");
    }

    /**
     * 产品详情  ok
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail.do", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ServerResponse detail(HttpSession session , Integer productId){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if (response.isSuccess()) {
            return iProductService.getProductDetail(productId);
        }
        return ServerResponse.createByError("无权限操作");
    }

    /*
     富文本上传编辑器: wangEditor 、simditor 、bootstrap-wysiwyg 、CKEditor 、tinymce
     */

    //3.富文本上传： 打算使用：wangEditor
    //4.文件上传（主要是图片），前端打算用bootstrap-fileInput
    /*
        文件上传思路： 打算做两种：a、配置tomcat 虚拟路径 b、FTP服务器
        1、验证登录和权限
        2、执行上传操作
        参数：file、path
         2.1：上传操作：
            a.获取文件真实名称
            b.切割后缀名和和前缀名（前缀名用于组成新的文件名，后缀名希望用于文件类型保存）
            c. 根据path 创建tomcat的文件目录
            d.创建新命名的文件，并执行写入操作
            e1：写入到FTP服务器 ，需要编写FTP文件写入操作相关的代码
            e2 ：如果是写入到虚拟路径，则无序操作
            f ：上传完成之后返回 文件信息：文件访问目录 、新文件名称（我的理解是这里不需要和业务挂机，所以这里不做信息保存处理，将这个信息返回，做后续操作的时候再将上传数据信息做保存操作）
        3、上传成功之后返回包含上传成功之后的url地址和新文件名称的map集合（后续业务操作，保存时会将信息保存到数据库中，个人觉得应该是将路径分开存储方便以后迁移，不过好像老师的代码中没有将FTP路径存储到数据库中，只存储了文件名称，这里还需考虑）
     */
    @RequestMapping(value = "upload.do",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ServerResponse upload(HttpSession session ,@RequestParam(value = "upload_file") MultipartFile file){//多文件上传实际上还是单文件上传
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getProductDesc());
        }
        ServerResponse response = iUserService.checkAdminRole(user);
        if (response.isSuccess()) {
            if(!file.isEmpty()){
                //String path = session.getServletContext().getRealPath("upload");//tomcat的目录
                //这里暂时使用tomcat虚拟路径代替ftp服务器
                //<Context docBase="D:/linj/upload/" path="/virtualPath" />
                String path = PropertiesUtil.getProperty("tomcat.docBase");
                String newFileName = iFileService.upload(file,path);
                Map resultMap = new HashMap();
                resultMap.put("fileName",newFileName);
                //这里的url直接传tomcat配置的虚拟路径的path吧
                resultMap.put("url",PropertiesUtil.getProperty("tomcat.virtualPath","http://localhost:8080/virtualPath/") );

                return ServerResponse.createBySuccess(resultMap);
            }
        }
        return ServerResponse.createByError("上传文件失败");
    }




}
