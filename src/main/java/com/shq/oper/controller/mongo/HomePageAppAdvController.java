package com.shq.oper.controller.mongo;

import java.time.LocalDateTime;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.shq.oper.service.primarydb.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.HomePageModuleMongo;
import com.shq.oper.enums.BannerTypeEnum;
import com.shq.oper.enums.HomePageModuleAvtiveEnum;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.util.Constant;

import lombok.extern.slf4j.Slf4j;

/**
 * 爱之家首页模块
 * 
 * @author linagjinzhao
 */
/**
 */
/**
 * 
 * 项目名称：operSys 类名称：HomePageModuleController 类描述： 创建人：ljz 创建时间：2018年8月21日
 * 下午2:40:31 修改人：ljz 修改时间：2018年8月21日 下午2:40:31 修改备注：
 * 
 * @version
 * 
 */
@RestController
@Slf4j
@RequestMapping("/mongo/homepageappadv")
public class HomePageAppAdvController extends BaseController {

	@Autowired
	private HomePageModuleMongo mongoDao;
	
	@Autowired
	private BannerService bannerService;
	@Autowired
	private BannerAppAdvService bannerAppAdvService;
	
	@RequestMapping("/appAdv")
	public ModelAndView appAdvPage(HttpServletRequest request) {
		Map<String, Object> homePageModuleAvtiveEnumMap = HomePageModuleAvtiveEnum.getObjectMap();
		HttpSession session = request.getSession();
		Object adminDto = session.getAttribute(Constant.OPER_USER);
		request.setAttribute("homePageModuleAvtiveEnumMap", homePageModuleAvtiveEnumMap);
		request.setAttribute("adminDto", adminDto);
		return toPage(request);
	}
	/**
	 * app首页广告的详情
	 * @param request
	 * @param banner
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/appAdvDetails")
	public ModelAndView appAdvDetails(HttpServletRequest request, Banner banner) throws Exception {
		Banner bannerDetails = bannerService.selectOne(banner);
		request.setAttribute("bannerDetails", bannerDetails);
		return toPage(request);
	}
	

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, Banner banner) {
		
		Msg<String>result=Msg.ok("保存成功");
		banner.setUpdateTime(LocalDateTime.now());
		banner.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		if (ObjectUtils.isEmpty(banner.getId())) {
			banner.setCreateTime(LocalDateTime.now());
			banner.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			banner.setType(BannerTypeEnum.ADVAPP.getCode());
			result = bannerAppAdvService.saveAdvAppBanner(banner);
		} else {
			bannerService.update(banner);
			result=Msg.ok("保存成功");
		}
		return result;
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Banner> list(Banner entity, PageDto page) {
		//只过滤是启动页广告的banner
		entity.setType(BannerTypeEnum.ADVAPP.getCode());
		Page<Banner> entitys=bannerService.queryAppAdvBanner(entity);
		return Data.ok(entitys);
	}

}