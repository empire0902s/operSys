package com.shq.oper.service.impl.primarydb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.primarydb.BannerModelGoodsDetailMapper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.domain.primarydb.BannerModelGoodsDetail;
import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.model.domain.primarydb.Product;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.BannerModelGoodsDetailService;
import com.shq.oper.service.primarydb.BannerService;
import com.shq.oper.service.primarydb.DictService;

@Service("bannerModelGoodsDetailService")
public class BannerModelGoodsDetailServiceImpl extends BaseServiceImpl<BannerModelGoodsDetail, Long>
		implements BannerModelGoodsDetailService {

	@Autowired
	private DistributionProductMapper distributionProductMapper;

	@Autowired
	private BannerModelGoodsDetailService bannerModelGoodsDetailService;
	
	@Autowired
	private BannerModelGoodsDetailMapper bannerModelGoodsDetailMapper;
	
	@Autowired
	private BannerService bannerService;
	@Autowired
	private DictService dictService;

	@Transactional
	@Override
	public Msg<String> saveBannerModelGoodsDetail(BannerModelGoodsDetail bannerModelGoodsDetail,
			DistributionProduct distributionProduct) {
		try {
			//根据productCode查询bannerModelGoodsDetail
			BannerModelGoodsDetail temp = new BannerModelGoodsDetail();
			temp.setGoodsCode(distributionProduct.getProductCode());
			List<BannerModelGoodsDetail> resultList = bannerModelGoodsDetailService.select(temp);
			if(!StringUtils.isEmpty(resultList)&&resultList.size()>0) {
				for (BannerModelGoodsDetail tmp : resultList) {
					if(tmp.getBannerId().equals(bannerModelGoodsDetail.getBannerId())) {
						return Msg.error("商品["+distributionProduct.getProductName()+"]已存在该栏目中，不可重新添加！");
					}
				}
				//根据bannerId查询banner
				Banner banner=new Banner();
				banner.setId(bannerModelGoodsDetail.getBannerId());
				Banner selectOne = bannerService.selectOne(banner);
				Dict dict=new Dict();
				dict.setDictValue(selectOne.getType());
				Dict resultDict = dictService.selectOne(dict);
				if(!resultDict.getType()){
					return Msg.error("商品["+distributionProduct.getProductName()+"]已存在其他栏目中，本栏目不支持添加已被添加过的商品！");
				}
			}
			bannerModelGoodsDetail.setGoodsCode(distributionProduct.getProductCode());
			bannerModelGoodsDetail.setFirstClassId(distributionProduct.getCategoryid().toString());
			bannerModelGoodsDetail.setSecondClassId(distributionProduct.getGenreid().toString());
			bannerModelGoodsDetail.setThreeClassId(distributionProduct.getThreeid().toString());
			bannerModelGoodsDetail.setFourClassId(distributionProduct.getFourid().toString());
			bannerModelGoodsDetailService.insert(bannerModelGoodsDetail);
			return Msg.ok("保存成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Msg.error("保存失败!");
		}
	}

	@Override
	public Data<BannerModelGoodsDetail> queryList(BannerModelGoodsDetail entity, PageDto page) {
		try {
			List<BannerModelGoodsDetail> select = bannerModelGoodsDetailService.select(entity);
			List<String> productIdList = new ArrayList<>();
			if (select.size() > 0) {
				for (BannerModelGoodsDetail tmp : select) {
					productIdList.add(tmp.getGoodsCode());
				}
			}
			if (productIdList.size() <= 0) {
				return Data.error("没有数据");
			}
			Set<BannerModelGoodsDetail>tmpSet=new HashSet<>();
			Page<DistributionProduct> productList =distributionProductMapper.queryDiatributionProductByCodeList(productIdList);
			com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
			Page<BannerModelGoodsDetail> result = bannerModelGoodsDetailMapper.queryBannerModelGoodsDetail(entity);
			if(productList.getResult().size()>0&&result.getResult().size()>0) {
				for (BannerModelGoodsDetail bannerModelGoodsDetail : result.getResult()) {
					for (DistributionProduct distributionProduct : productList.getResult()) {
						if(bannerModelGoodsDetail.getGoodsCode().equals(distributionProduct.getProductCode())) {
							bannerModelGoodsDetail.setProductName(distributionProduct.getProductName()==null?"未知":distributionProduct.getProductName());
						}
					}
				}
				if(!StringUtils.isEmpty(entity.getProductName())) {
					Page<DistributionProduct> pageProductName = distributionProductMapper.queryDiatributionProductByProductName(entity.getProductName());
					if(pageProductName.getResult().size()>0) {
						for (BannerModelGoodsDetail bannerModelGoodsDetail : result.getResult()) {
							for (DistributionProduct distributionProduct : pageProductName.getResult()) {
								if(!bannerModelGoodsDetail.getProductName().equals(distributionProduct.getProductName())) {
									tmpSet.add(bannerModelGoodsDetail);
								}
							}
						}
					}else {
						return Data.error("没有数据");
					}
					if(tmpSet.size()>0) {
						for (BannerModelGoodsDetail bannerModelGoodsDetail : tmpSet) {
							result.getResult().remove(bannerModelGoodsDetail);
						}
					}
				}
			}else {
				return Data.error("没有数据");
			}
			return Data.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return Data.error("查询数据失败");
		}
		
	}
}