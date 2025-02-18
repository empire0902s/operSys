package com.shq.oper.controller.mongo;



import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.PriceReductionGoodsMongo;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.HomePageModule;
import com.shq.oper.model.domain.mongo.PriceReductionGoods;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.PriceReductionGoodsDto;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.service.primarydb.PriceReductionGoodsService;

import groovyjarjarcommonscli.ParseException;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* 爱之家首页模块附属模块
* 项目名称：operSys   
* 类名称：HomePageModuleAttachedController   
* 类描述：   
* 创建人：ljz   
* 创建时间：2018年8月22日 下午5:54:18   
* 修改人：ljz   
* 修改时间：2018年8月22日 下午5:54:18   
* 修改备注：   
* @version    
*
 */
@RestController
@Slf4j
@RequestMapping("/mongo/priceReductionGoods")
public class PriceReductionGoodsController extends BaseController {

	
	@Autowired
	private PriceReductionGoodsMongo priceReductionGoodsMongo;
	
	@Autowired
	private DistributionProductMapper distributionProductMapper;
	
	@Autowired
	private PriceReductionGoodsService priceReductionGoodsService;

	@Autowired
	SystemProperties systemProperties;

	@RequestMapping("/bargaingoodsSetting")
	public ModelAndView bargaingoodsSetting(HttpServletRequest request) {
		return toPage(request);
	}
	
	@RequestMapping("/bindingToGoods")
	private ModelAndView bindingToGoods(HttpServletRequest request,PriceReductionGoods priceReductionGoods
			,String operateType) {
		if("1".equals(operateType)) {
			request.setAttribute("operateType", 1);
			request.setAttribute("id", null);
		}else {
			PriceReductionGoods priceReductionGoodsTemp = priceReductionGoodsMongo.findById(priceReductionGoods.getId(), PriceReductionGoods.class);
			DistributionProduct entity=new DistributionProduct();
			entity.setProductCode(priceReductionGoods.getGoodsCode());
			DistributionProduct result = distributionProductMapper.selectOne(entity);
			result.setProductDetails("");
			request.setAttribute("product", result);
			request.setAttribute("operateType", 2);
			request.setAttribute("priceReductionGoodsTemp",priceReductionGoodsTemp);
		}
		return this.toPage(request);
	}
	
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<PriceReductionGoodsDto> list(PriceReductionGoods entity,String productName, PageDto page) throws ParseException {
		PageInfo<PriceReductionGoodsDto> entitys=priceReductionGoodsService.queryPriceReductionGoodsList(entity,productName,page);
		return Data.ok(entitys);
	}
	
	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, PriceReductionGoods priceReductionGoods) {
		if(priceReductionGoods.getSortNum()!=1000) {
			PriceReductionGoods tempPriceReductionGoods=new PriceReductionGoods();
			tempPriceReductionGoods.setSortNum(priceReductionGoods.getSortNum());
			Query query = new Query();
				query.addCriteria(Criteria.where("sortNum").is(priceReductionGoods.getSortNum()));
			PageInfo<PriceReductionGoods>resultPage = priceReductionGoodsMongo.findByQueryPage(query,new PageDto(),tempPriceReductionGoods);
			if(resultPage.getList().size()>0) {
				for (PriceReductionGoods tmp : resultPage.getList()) {
					//排序设置为1000
					tmp.setSortNum(1000);
				}
				//更新数据
				PriceReductionGoods result = resultPage.getList().get(0);
				result.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
				result.setUpdateTime(LocalDateTime.now().toString());
				priceReductionGoodsMongo.update(result);
			}
		}
		//判断是否已添加该商品
		if (StringUtils.isEmpty(priceReductionGoods.getId())) {
			PriceReductionGoods temp=new PriceReductionGoods();
			temp.setGoodsCode(priceReductionGoods.getGoodsCode());
			temp.setSortNum(null);
			temp.setReductionGoodsNum(null);
			temp.setReductionGoodsPrice(null);
			temp.setReductionNumRule(null);
			temp.setBrokerageMoney(null);
			temp.setShowState(null);
			temp.setFinanceState(null);
			temp.setFinanceAuditTime(null);
			temp.setFinanceAuditor(null);
			temp.setSyncStateNet(null);
			PageInfo<PriceReductionGoods> entity = priceReductionGoodsMongo.findByEntity(temp, new PageDto());
			if(entity.getList().size()>0) {
				return Msg.error("该商品已存在列表中");
			}
		}
		priceReductionGoods.setUpdateTime(LocalDateTime.now().toString());
		priceReductionGoods.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		if (ObjectUtils.isEmpty(priceReductionGoods.getId())) {
			priceReductionGoods.setId(null);
			priceReductionGoods.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			priceReductionGoods.setCreateTime(LocalDateTime.now().toString());
			priceReductionGoodsMongo.insert(priceReductionGoods);
		} else {
			priceReductionGoodsMongo.update(priceReductionGoods);
		}
		return Msg.ok("保存成功");
	}
	@RequestMapping("/updateShowState")
	public Msg<String> updateShowState(HttpServletRequest request, PriceReductionGoods entity) throws Exception {
		Query query = Query.query(Criteria.where("id").is(entity.getId()));
        Update update = new Update().set("showState", entity.getShowState())
        		.set("updateTime", LocalDateTime.now().toString())
        		.set("updateAdmin", this.getCreateOrUpdateAdminName(request));
        priceReductionGoodsMongo.updateFirst(query, update, PriceReductionGoods.class);
		return Msg.ok("操作成功");
	}

	
}