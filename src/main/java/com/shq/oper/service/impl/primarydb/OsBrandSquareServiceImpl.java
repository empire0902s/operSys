package com.shq.oper.service.impl.primarydb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.shq.oper.model.domain.primarydb.PubliceNum;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.orderingsystem.ListBusinessStoresDto;
import com.shq.oper.model.dto.api.orderingsystem.OSBrasndSquareDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.OsBrandSquareService;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

@Service("osBrandSquareService")
public class OsBrandSquareServiceImpl implements OsBrandSquareService {

	
	@Autowired
	private ShqApi ShqApi;
	
	
	@Override
	public Data<OSBrasndSquareDto> queryList(OSBrasndSquareDto entity, PageDto page) {
		
		List<OSBrasndSquareDto> tmptList =new ArrayList<>();
    	String url = ShqApi.getGetOsbrandSquareUrl()+"api/Home/GetHomeInfo";
		String resultJson = HttpClientUtil.doGet(url);
		//调用接口出错，返回null
		if(StringUtils.isEmpty(resultJson)) {
			return Data.ok(new Page<>());
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		Object object = resultMap.get("Resultdata");
		if(object==null|object=="") {
			return Data.ok(new Page<>());
		}
		ListBusinessStoresDto data = JsonUtils.parse(JsonUtils.toDefaultJson(object), ListBusinessStoresDto.class);
		tmptList = data.getListBusinessStores();
		if(tmptList.size()==0) {
			return Data.ok(new Page<>());
		}
		//过滤数据
		List<OSBrasndSquareDto> resultList=new ArrayList<>();
		for (OSBrasndSquareDto osBrasndSquareDto : tmptList) {
			if(!StringUtils.isEmpty(osBrasndSquareDto.getDealerId())) {
				if(osBrasndSquareDto.getDealerId().equals(1L)) {
					resultList.add(osBrasndSquareDto);
				}
			}
		}
		if(resultList.size()==0) {
			return Data.ok(new Page<>());
		}
		if(!StringUtils.isEmpty(entity.getStoresName())) {
			resultList = resultList.stream().filter(p->p.getStoresName().contains(entity.getStoresName().trim())).collect(Collectors.toList());
		}
		if(resultList.size()==0) {
			return Data.ok(new Page<>());
		}
		for (OSBrasndSquareDto tmp : resultList) {
			tmp.setStoresUrl(ShqApi.getImageAddrUrl()+tmp.getStoresUrl());
		}
		PageInfo<OSBrasndSquareDto>result=new PageInfo<>();
		int start=0,end=0;
		start=(page.getPage()-1)*page.getLimit();
        List<OSBrasndSquareDto> listGoods1=new ArrayList<>();
        if (resultList.size()>=start+page.getLimit()){
            listGoods1=resultList.subList(start,start+page.getLimit());
        }else if(resultList.size()>=start){
            listGoods1=resultList.subList(start,resultList.size());
        }
        result.setList(listGoods1);
		result.setPageNum(page.getPage());
		result.setPageSize(page.getLimit());
		result.setTotal(resultList.size());
		return Data.ok(result);
	}


}
