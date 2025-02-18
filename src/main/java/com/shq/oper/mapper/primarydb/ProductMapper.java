package com.shq.oper.mapper.primarydb;
 
import org.apache.ibatis.annotations.Mapper;

import com.shq.oper.model.domain.primarydb.Product;
import com.shq.oper.util.BaseMapper;
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
