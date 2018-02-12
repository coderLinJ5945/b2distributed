package com.lin.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author linj
 * 购物车view ，个人理解这里的cart是单个商品为一个单位（包含数量）
 */
public class CartViewObject {
    private List<CartProductViewObject> cartProductViewObjectList; /*商品的list集合*/
    private Integer checkedNumber;//已选中商品的数量
    private BigDecimal carCkeckedProductTotalPrice;/*购物车中商品勾选的商品总价格，这个字段是通过计算得来，不需要存储在购物车表中*/
    private Boolean allChecked; /*是否全部勾选*/
    private String  imageHost;/*图片服务器地址*/

    public List<CartProductViewObject> getCartProductViewObjectList() {
        return cartProductViewObjectList;
    }

    public void setCartProductViewObjectList(List<CartProductViewObject> cartProductViewObjectList) {
        this.cartProductViewObjectList = cartProductViewObjectList;
    }

    public BigDecimal getCarCkeckedProductTotalPrice() {
        return carCkeckedProductTotalPrice;
    }

    public void setCarCkeckedProductTotalPrice(BigDecimal carCkeckedProductTotalPrice) {
        this.carCkeckedProductTotalPrice = carCkeckedProductTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public Integer getCheckedNumber() {
        return checkedNumber;
    }

    public void setCheckedNumber(Integer checkedNumber) {
        this.checkedNumber = checkedNumber;
    }
}
