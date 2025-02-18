package com.shq.oper.model.dto.api.orderingsystem;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "订货系统品牌广场DTO")
@lombok.Data
public class ListBusinessStoresDto implements Serializable {
	private static final long serialVersionUID = 1L;

	public List<OSBrasndSquareDto> listBusinessStores;


}