package com.lin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lin.common.Constant;
import com.lin.common.ServerResponse;
import com.lin.dao.CategoryMapper;
import com.lin.dao.ProductMapper;
import com.lin.pojo.Category;
import com.lin.pojo.Product;
import com.lin.service.ICategoryService;
import com.lin.service.IProductService;
import com.lin.util.DatetimeUtils;
import com.lin.util.PropertiesUtil;
import com.lin.util.SqlAssemBleUtils;
import com.lin.vo.ProductViewObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    private final static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;


    //新增和修改方法
    public ServerResponse poductSaveOrUpdate(Product product) {
        if (product != null) {
            //主图片特殊字段需要处理保存
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImagesArr = product.getSubImages().split(",");
                if (subImagesArr.length > 0) {
                    product.setMainImage(subImagesArr[0]);
                }
            }
            if (product.getId() != null) {
                //update
                int count = productMapper.updateByPrimaryKeySelective(product);
                if (count > 0) {
                    return ServerResponse.createBySuccess("商品修改成功");
                } else {
                    return ServerResponse.createByError("商品修改失败");
                }
            } else {
                //insert
                int count = productMapper.insertSelective(product);
                if (count > 0) {
                    return ServerResponse.createBySuccess("商品新增成功");
                } else {
                    return ServerResponse.createByError("商品新增失败");
                }
            }
        }
        return ServerResponse.createByError("商品参数有误");
    }

    //更新商品状态
    public ServerResponse setProductStatus(Product product) {
        if (product != null) {
            if (product.getId() != null && product.getStatus() != null) {
                int count = productMapper.updateStatusByPrimaryKey(product.getId(), product.getStatus());
                if (count > 0) {
                    return ServerResponse.createBySuccess("商品状态更新成功");
                } else {
                    return ServerResponse.createByError("商品状态更新失败");
                }
            }
        }
        return ServerResponse.createByError("商品参数有误");
    }

    //根据商品id获取商品详细信息
    public ServerResponse getProductDetail(Integer productId) {
        if (productId != null) {
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product != null) {
                ProductViewObject productViewObject = assembleProductViewObject(product);
                return ServerResponse.createBySuccess(productViewObject);
            } else {
                return ServerResponse.createByError("商品已下架或者被和删除");
            }
        }
        return ServerResponse.createByError("商品已下架或者被和删除");
    }

    private ProductViewObject assembleProductViewObject(Product product) {
        ProductViewObject productViewObject = new ProductViewObject();
        productViewObject.setId(product.getId());
        productViewObject.setName(product.getName());
        productViewObject.setSubtitle(product.getSubtitle());
        productViewObject.setMainImage(product.getMainImage());
        productViewObject.setSubImages(product.getSubImages());
        productViewObject.setDetail(product.getDetail());
        productViewObject.setPrice(product.getPrice());
        productViewObject.setStock(product.getStock());
        productViewObject.setStatus(product.getStatus());
        productViewObject.setCreateTime(DatetimeUtils.dateToString(product.getCreateTime()));
        productViewObject.setUpdateTime(DatetimeUtils.dateToString(product.getUpdateTime()));

        productViewObject.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productViewObject.setCategoryId(product.getCategoryId());
        //根据当前商品分类id 查询parentId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category != null) {
            if (category.getParentId() != null) {
                productViewObject.setParentCategoryId(category.getParentId());
            } else {
                productViewObject.setParentCategoryId(0);//0是根节点
            }
        }
        return productViewObject;
    }


    //todo 后台分页查询商品接口
    public ServerResponse searchList(String productName, Integer productId, Integer pageNumber, Integer pageSize) {
        productName = SqlAssemBleUtils.assembleSqlLike(productName);
        if (pageNumber != null && pageSize != null) {
            //分页
            PageHelper.startPage(pageNumber, pageSize);
            List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
            List<ProductViewObject> productViewObjectList = new ArrayList<>();
            if (productList != null) {
                for (Product product : productList) {
                    ProductViewObject productViewObject = assembleProductViewObject(product);
                    productViewObjectList.add(productViewObject);
                }
            }
            PageInfo pageresult = new PageInfo(productList);
            pageresult.setList(productViewObjectList);
            return ServerResponse.createBySuccess(pageresult);
        } else {
            //List<ProductViewObject> productList = productMapper.selectByNameAndProductId(productName,productId);
            //todo 先看看后面视频不分页查询所有数据的接口如何编写，然后再来编写这里的代码
            return null;
        }
    }

    //前台用户搜索商品功能
    public ServerResponse searchProduct(String searchContent, Integer[] categoryIds, Integer pageNum, Integer pageSize, String[] orderBys) {
        try {
            //组装like
            searchContent = SqlAssemBleUtils.assembleSqlLike(searchContent);
            //todo 写一个方法根据分类id获取所有的子id ，然后根据数组来使用
            List<Integer> categoryList = new ArrayList<Integer>();
            if (categoryIds != null && categoryIds.length > 0) {
                for (Integer categoryId : categoryIds) {
                    //递归获取商品分类的所有id
                    List<Category> category = new ArrayList<Category>();
                    category = iCategoryService.getCategoryAndDeepChildrenCategory(categoryId).getData();
                    if (category != null) {
                        for (Category category1 : category) {
                            categoryList.add(category1.getId());
                        }
                    }
                }
            }
            //多个orderby 排序 todo 后面应该思考一下排序代码应该如何设计通用
            if (orderBys != null && orderBys.length > 0) {
                StringBuffer sbOrderBys = new StringBuffer();
                for (String orderBy : orderBys) {
                    if (Constant.ProductListOrderBy.PRODUCT_ASC_DESC.contains(orderBy)) {
                        String[] orderArr = orderBy.split("_");
                        sbOrderBys.append(orderArr[0] + " ");
                        sbOrderBys.append(orderArr[1] + ",");
                    }
                }
                if (sbOrderBys.length() > 0) {
                    //System.out.println(sbOrderBys.toString().substring(0, sbOrderBys.lastIndexOf(",")));
                    PageHelper.orderBy(sbOrderBys.toString().substring(0, sbOrderBys.lastIndexOf(",")));
                }
            }
            //分页
            PageHelper.startPage(pageNum, pageSize);
            List<Product> productList = productMapper.selectByNameAndCategoryIds(searchContent, categoryList);
            List<ProductViewObject> productViewObjectList = Lists.newArrayList();
            for (Product product : productList) {
                productViewObjectList.add(assembleProductViewObject(product));
            }
            PageInfo pageInfo = new PageInfo(productList);
            pageInfo.setList(productViewObjectList);
            return ServerResponse.createBySuccess(pageInfo);
        } catch (Exception e) {
            logger.info("商品查询请求参数错误", e);
            return ServerResponse.createByError("商品查询请求参数错误");
        }
    }

}
