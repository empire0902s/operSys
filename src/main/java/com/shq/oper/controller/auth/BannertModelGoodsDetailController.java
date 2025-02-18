package com.shq.oper.controller.auth;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.primarydb.BannerModelGoodsDetail;
import com.shq.oper.model.domain.primarydb.Product;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.BannerModelGoodsDetailService;

import lombok.extern.slf4j.Slf4j;

/**
 * Banner
 * 
 * @author Administrator
 *
 */
@RestController
@Slf4j
@RequestMapping("/banner/bannerGoodsDetail")
public class BannertModelGoodsDetailController extends BaseController {

	@Autowired
	private DistributionProductMapper distributionProductMapper;

	@Autowired
	private BannerModelGoodsDetailService bannerModelGoodsDetailService;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<BannerModelGoodsDetail> list(BannerModelGoodsDetail entity,PageDto page) {
		return bannerModelGoodsDetailService.queryList(entity,page);
	}

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, BannerModelGoodsDetail bannerModelGoodsDetail,
			DistributionProduct distributionProduct) {
		bannerModelGoodsDetail.setUpdateTime(LocalDateTime.now());
		bannerModelGoodsDetail.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		if(!StringUtils.isEmpty(bannerModelGoodsDetail.getId())) {
			bannerModelGoodsDetailService.update(bannerModelGoodsDetail);
			return Msg.ok("保存成功");
		}
		Msg<String> result = bannerModelGoodsDetailService.saveBannerModelGoodsDetail(bannerModelGoodsDetail,distributionProduct);
		return result;
	}

	@RequestMapping("/findByCode")
	private Msg<DistributionProduct> findByCode(DistributionProduct entity) {
		DistributionProduct result = distributionProductMapper.selectOne(entity);
		if (StringUtils.isEmpty(result)) {
			return Msg.error("查询失败，没有此商品", null);
		}
		return Msg.ok(result);
	}

}
