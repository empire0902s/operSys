package com.shq.oper.test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.AdminMapper;
import com.shq.oper.mapper.primarydb.DictMapper;
import com.shq.oper.mapper.primarydb.ResourceMaper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.Admin;
import com.shq.oper.model.domain.primarydb.CouponsUser;
import com.shq.oper.model.domain.shq520new.DistrFourCategoty;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.model.dto.api.goods.Distribution;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.AdminService;
import com.shq.oper.util.ExcelUtils;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * SpringBaseTest
 *
 * @author tjun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SqlserverDatasourceTes {
	
	@Autowired
	private DistributionProductMapper distributionProductMapper;
	@Autowired
	private ShopperMapper shopperMapper;
	@Autowired
	private DictMapper dictMapper;
	
	@Autowired
    private ResourceMaper resourceMaper;
	
	@Test
	public void testDs_shq520new() {
			 
		List<String> codeList = new ArrayList<>();
		codeList.add("5758557201806212115221522281893");
		codeList.add("5758557201806212021272127271499");
		//List<DistributionProduct>  listAll = distributionProductMapper.selectAll();
		Page<DistributionProduct>  list = distributionProductMapper.queryDiatributionProductByCodeList(codeList);
		System.out.println(JsonUtils.toDefaultJson(list));
		
	}
	
	
	@Test
	public void testDs_Excel() {
			
	}
	
	public static void main(String[] args) {
		 File file = new File("D://Excel消息发送.xls");
			try {
				if(file.exists()){
					List<DataExcel> list = ExcelUtils.getData(file, DataExcel.class);
					System.out.println("=========="+JsonUtils.toDefaultJson(list));
				}else {
					System.out.println("111");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}