package com.lin.controller.portal;

import com.lin.common.ServerResponse;
import com.lin.pojo.Category;
import com.lin.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 前台商品接口  todo 目前都未包含店铺信息，后续可以将店铺信息加入，店铺信息表设计？
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /*
        1、产品搜索功能
         模糊搜索、排序（多组数据排序）、类型搜索（多类型）
     */

    /**
     *
     * @param searchContent 模糊搜索的内容
     * @param categoryIds  商品类型数组 : [1,2,3]
     * @param pageSize 分页大小
     * @param pageNum 页数
     * @param orderBys 排序字段 ：orderBys[] : price_desc,name_desc
     * @return
     */
    @RequestMapping(value = "list.do", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "searchContent", required = false) String searchContent,
                               @RequestParam(value="categoryIds[]", required = false) Integer[] categoryIds,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                               @RequestParam(value ="pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "orderBys[]",required = false) String[] orderBys) {
        return iProductService.searchProduct(searchContent , categoryIds, pageNum, pageSize, orderBys);
    }
}
