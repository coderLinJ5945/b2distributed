package com.lin.service.impl;

import com.lin.common.ServerResponse;
import com.lin.dao.ProductMapper;
import com.lin.pojo.Product;
import com.lin.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iProductService")
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;

    //新增和修改方法
    public ServerResponse poductSaveOrUpdate(Product product){
        if (product != null) {
            //主图片特殊字段需要处理保存
            if(StringUtils.isNotBlank(product.getSubImages())){
                String [] subImagesArr =  product.getSubImages().split(",");
                if(subImagesArr.length > 0){
                    product.setMainImage(subImagesArr[0]);
                }
            }
            if (product.getId() != null) {
                //update
                int count = productMapper.updateByPrimaryKeySelective(product);
                if(count > 0){
                    ServerResponse.createBySuccess("商品修改成功");
                }else{
                    ServerResponse.createByError("商品修改失败");
                }
            }else{
                //insert
                int count = productMapper.insertSelective(product);
                if(count > 0){
                    ServerResponse.createBySuccess("商品新增成功");
                }else{
                    ServerResponse.createByError("商品新增失败");
                }
            }
        }
        return ServerResponse.createByError("商品参数有误");
    }

    //更新商品状态
    public ServerResponse setProductStatus(Product product){
        if (product != null) {
            if (product.getId() != null && product.getStatus() !=null) {
                int count =  productMapper.updateStatusByPrimaryKey(product.getId(),product.getStatus());
                if(count > 0){
                    return ServerResponse.createBySuccess("商品状态更新成功");
                }else{
                    return ServerResponse.createByError("商品状态更新失败");
                }
            }
        }
        return ServerResponse.createByError("商品参数有误");
    }
}
