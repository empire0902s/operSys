package com.shq.oper.service.impl.primarydb;

import com.shq.oper.mapper.primarydb.ProductPriceMapper;
import com.shq.oper.service.primarydb.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shq.oper.model.domain.primarydb.ProductPrice;
import com.shq.oper.service.primarydb.ProductPriceService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service("priceService")
public class ProductPriceImpl extends BaseServiceImpl<ProductPrice, Long> implements ProductPriceService {

    @Autowired
    private ProductPriceMapper productPriceMapper;

    @Transactional
    @Override
    public void saveProductPriceList(String adminName, List<ProductPrice> productPriceList){
        LocalDateTime dtNow = LocalDateTime.now();

        productPriceList.forEach(
                p->{
                    p.setUpdateTime(dtNow);
                    p.setUpdateAdmin(adminName);
                    productPriceMapper.updateByPrimaryKey(p);
                }
        );
    }
	
 
}
