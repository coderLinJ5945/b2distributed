package com.lin.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lin.common.Constant;
import com.lin.common.ResponseCodeEnum;
import com.lin.common.ServerResponse;
import com.lin.dao.CartMapper;
import com.lin.dao.ProductMapper;
import com.lin.pojo.Cart;
import com.lin.pojo.Product;
import com.lin.service.ICartService;
import com.lin.util.BigDecimalUtil;
import com.lin.util.PropertiesUtil;
import com.lin.vo.CartProductViewObject;
import com.lin.vo.CartViewObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    //购物车list
    public ServerResponse<CartViewObject> list(Integer userId){
        /**
         * 1、传入userid，根据userid在cart表中 查找购物车信息
         * 2、不管购物车有没有东西，都要返回购物车的CartViewObject对象
         * 3、如果购物车有东西，循环遍历Cart，拿到商品id ，根据商品Id获取商品需要展示的信息
         * 3.1主要是对a 库存和 b 购物车该商品的数量做对比， if b>a --->a LimitQuantity限制数量， 库存充足true 不足fasle
         * 3.2 计算勾选的总价钱
         * todo  业务保留，这里循环查询后面改成join
         */
        CartViewObject cartViewObject = new CartViewObject();
        BigDecimal totalPrice = new BigDecimal("0.00");
        Integer checkedNumber = new Integer(0);
        List<CartProductViewObject>  cartProductViewObjectList = Lists.newArrayList();
        //执行方法填充 cartViewObject
        //根据用户id获取该用户购物车信息
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        if(CollectionUtils.isNotEmpty(cartList)){
            //循环获取详细数据
            for (Cart cart : cartList) {
                CartProductViewObject cartProductViewObject = new CartProductViewObject();
                cartProductViewObject.setId( cart.getId());
                cartProductViewObject.setUserId(cart.getUserId());
                cartProductViewObject.setProductId(cart.getProductId());
                cartProductViewObject.setProductChecked(cart.getChecked());
                Product  product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product != null) {
                    cartProductViewObject.setProductName(product.getName());
                    cartProductViewObject.setProductSubtitle(product.getSubtitle());
                    cartProductViewObject.setProductMainImage(product.getMainImage());
                    cartProductViewObject.setProductStatus(product.getStatus());
                    cartProductViewObject.setProductPrice(product.getPrice());
                    cartProductViewObject.setProductStock(product.getStock());//库存数量
                    //这里需要根据库存数量来约束获取实际可购买数量
                    if(product.getStock() >= cart.getQuantity()){
                        //库存充足
                        cartProductViewObject.setQuantity(cart.getQuantity());//数量
                        cartProductViewObject.setLimitQuantity(Constant.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        cartProductViewObject.setQuantity(product.getStock());//获取库存最大数量
                        cartProductViewObject.setLimitQuantity(Constant.Cart.LIMIT_NUM_FAIL);
                        //更新购物车中商品的个数,有可能会漏掉的操作
                        cart.setQuantity(product.getStock());
                        cartMapper.updateByPrimaryKeySelective(cart);
                    }
                    BigDecimal result = BigDecimalUtil.multiply(cartProductViewObject.getProductPrice().doubleValue(),cartProductViewObject.getQuantity().doubleValue());
                    cartProductViewObject.setProductTotalPrice(result);

                    //判断商品是否勾选，用来计算购物车总价格
                    if(cart.getChecked() == Constant.Cart.CHECKED){
                        checkedNumber = checkedNumber + cartProductViewObject.getQuantity();
                        totalPrice = BigDecimalUtil.add(totalPrice.doubleValue(),cartProductViewObject.getProductTotalPrice().doubleValue());
                    }
                    cartProductViewObjectList.add(cartProductViewObject);
                }
            }
        }
        cartViewObject.setCarCkeckedProductTotalPrice(totalPrice);
        cartViewObject.setCheckedNumber(checkedNumber);
        cartViewObject.setCartProductViewObjectList(cartProductViewObjectList);
        cartViewObject.setAllChecked(selectCartProductAllCheckedByUserId(userId));
        cartViewObject.setImageHost(PropertiesUtil.getProperty("tomcat.virtualPath","http://localhost:8080/virtualPath/"));

        return ServerResponse.createBySuccess(cartViewObject);
    }


    //获取购物车中商品是否全选中
    private boolean selectCartProductAllCheckedByUserId(Integer userId){
        if (userId != null) {
            return  cartMapper.selectCartProductAllCheckedByUserId(userId) ==0;
        }
        return false;
    }


    //商品添加到购物车
    public ServerResponse<CartViewObject> add(Integer userId, Integer productId, Integer count){
        //1、判断参数
        if(userId == null || productId == null || count == null){
            return ServerResponse.createByError(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        //2、根据 userId 和 productId
        try {
            Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
            if (cart != null) {
                //编辑操作
                count = count + cart.getQuantity();
                cart.setQuantity(count);
                cartMapper.updateByPrimaryKeySelective(cart);
            } else {
                //新增操作
                Cart item = new Cart();
                item.setUserId(userId);
                item.setProductId(productId);
                item.setQuantity(count);
                item.setChecked(Constant.Cart.CHECKED);
                cartMapper.insertSelective(item);
            }

            return this.list(userId);
        }catch (Exception e){
            return ServerResponse.createByError();
        }
    }

    //商品移除购物车
    public ServerResponse delCartProducts(Integer userId,Integer[] productIds){
        try {
            if (productIds != null || productIds.length > 0) {
                Set<Integer> productIdSet = Sets.newHashSet(productIds);
                cartMapper.deleteByProductIdsAndUserId(productIdSet, userId);
                return this.list(userId);
            }
        }catch (Exception e){
            return ServerResponse.createByError(ResponseCodeEnum.LOGICAL_EXCEPTION.getCode(),ResponseCodeEnum.LOGICAL_EXCEPTION.getProductDesc());
        }
        return ServerResponse.createByError(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),ResponseCodeEnum.ILLEGAL_ARGUMENT.getProductDesc());
    }

    //单选选中或取消
    public ServerResponse selectOrUnSelect(Integer userId,Integer productId,Integer checked){
        if(checked == Constant.Cart.CHECKED || checked == Constant.Cart.UNCHECKED){
            cartMapper.selectOrUnSelect(userId,productId,checked);
        }
        return this.list(userId);
    }
}
