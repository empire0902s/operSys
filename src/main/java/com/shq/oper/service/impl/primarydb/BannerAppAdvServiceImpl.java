package com.shq.oper.service.impl.primarydb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.shq.oper.enums.BannerTypeEnum;
import com.shq.oper.mapper.primarydb.BannerMapper;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.model.dto.api.banner.BannerAdvAppDto;
import com.shq.oper.model.dto.api.banner.BannerAdvAppJsonDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.BannerAppAdvService;
import com.shq.oper.service.primarydb.BannerService;
import com.shq.oper.service.primarydb.DictService;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service("bannerAppAdvService")
@Slf4j
public class BannerAppAdvServiceImpl extends BaseServiceImpl<Banner, Long> implements BannerAppAdvService {

	public final static Integer HOME_PAGE_BANNER_COUNTS = 5;
	@Autowired
	private BannerMapper bannerMapper;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private DictService dictService;
	@Autowired
	private ShqApi ShqApi;
	
	@Transactional
	@Override
	public Msg<String> saveAdvAppBanner(Banner banner) {
		
		Banner tmp=new Banner();
		tmp.setType(BannerTypeEnum.ADVAPP.getCode());
		int count = bannerMapper.selectCount(tmp);
		if (count >= HOME_PAGE_BANNER_COUNTS) {
			return Msg.error("APP启动页广告数量已满(共" + count + "条)，不可再添加");
		}
		//将已启用的app广告设置为禁用状态的
		bannerMapper.updateBannerStatusToDisabled(BannerTypeEnum.ADVAPP.getCode());
		//更新数据
		bannerMapper.insertSelective(banner);
		return Msg.ok("保存成功");
	}

	@Transactional
	@Override
	public Msg<String> dataInitialization(String createOrUpdateAdminName) {
		
		Dict dict=new Dict();
		dict.setCode("systemUrl");
		dict.setDictKey("appAdvDataInitialization");
		List<Dict> select = dictService.select(dict);
		if(select.size()<=0) {
			return Msg.error("数据初始化失败，原因是【查询不到字典】");
		}
		Dict entity = select.get(0);
		if(StringUtils.isEmpty(entity.getDictValue())) {
			return Msg.error("数据初始化失败，原因是【字典值不存在】");
		}
		String url = entity.getDictValue();
		String resultJson = HttpClientUtil.doGet(url);
		BaseResponseResultDto parse = JsonUtils.parse(resultJson, BaseResponseResultDto.class);
		if(!StringUtils.isEmpty(parse)) {
			//清空全部app启动页广告
			Banner tempBanner=new Banner();
			tempBanner.setType(BannerTypeEnum.ADVAPP.getCode());
			bannerService.delete(tempBanner);
			BannerAdvAppJsonDto result = JsonUtils.parse(JsonUtils.toDefaultJson(parse.getResult()), BannerAdvAppJsonDto.class);
			if(result.getPowerPointList().size()>0) {
				//先将原来启用的banner设置为禁用
				bannerMapper.updateBannerStatusToDisabled(BannerTypeEnum.ADVAPP.getCode());
				BannerAdvAppDto temp = result.getPowerPointList().get(0);
					Banner banner=new Banner();
					banner.setBannerName("app启动页广告");
					banner.setCreateTime(LocalDateTime.now());
					banner.setUpdateTime(LocalDateTime.now());
					banner.setCreateAdmin(createOrUpdateAdminName);
					banner.setUpdateAdmin(createOrUpdateAdminName);
					banner.setType(BannerTypeEnum.ADVAPP.getCode());
					banner.setSort(1000);
					banner.setIsDisabled(false);
					banner.setImgPath(ShqApi.getImageAddrUrl()+temp.getAppPictureUrl());
					banner.setUrl(temp.getActivityUrl());
				bannerService.insert(banner);
			}
		}else {
			return Msg.error("数据初始化失败，原因是【app启动页广告接口地址错误】");
		}
		return Msg.ok("保存成功");
	}
}