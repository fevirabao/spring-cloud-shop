package quick.pager.shop.seller.mapper;

import quick.pager.shop.model.seller.Seller;

public interface SellerMapper {

    int insertSelective(Seller record);

    Seller selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Seller record);

}