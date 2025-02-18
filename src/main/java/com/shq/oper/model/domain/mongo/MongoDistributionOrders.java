package com.shq.oper.model.domain.mongo;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分销订单
 *
 * @author liuyaoxi
 *
 */
@Data
@Document(collection = "distribution_orders")
public class MongoDistributionOrders  {
	
	private String orderno;// 订单编号
	private List<OrderDetail> listOrderDetails;

}
