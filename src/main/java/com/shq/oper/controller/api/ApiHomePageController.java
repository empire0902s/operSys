package com.shq.oper.controller.api;

import com.github.pagehelper.PageInfo;
import com.shq.oper.dao.mongo.ApiRequestDataLogMongo;
import com.shq.oper.dao.mongo.GoodsMongo;
import com.shq.oper.enums.BannerTypeEnum;
import com.shq.oper.enums.ProductTypeEnum;
import com.shq.oper.enums.api.HomePageReceiveCodeEnums;
import com.shq.oper.enums.api.ResponseResultCodeEnums;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.domain.mongo.channel.Goods;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.BaseHomePageParamDto;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.model.dto.api.banner.BannerAdvAppDto;
import com.shq.oper.model.dto.api.homepage.HomePageInvitationPageDataDto;
import com.shq.oper.model.dto.api.homepage.PriceReductionGoodsExamineParamDto;
import com.shq.oper.model.dto.api.req.HomePageInvitationPageParamDto;
import com.shq.oper.service.primarydb.ActivityGoodsRuleDetailsService;
import com.shq.oper.service.primarydb.BannerService;
import com.shq.oper.service.primarydb.PriceReductionGoodsService;
import com.shq.oper.util.CommRequestUtil;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.JsonUtils;
import com.shq.oper.util.MemoryViewUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
*    
* 项目名称：operSys   
* 类名称：ApiHomePageController   
* 类描述：   
* 创建人：ljz   
* 创建时间：2018年8月22日 上午9:40:25   
* 修改人：ljz   
* 修改时间：2018年8月22日 上午9:40:25   
* 修改备注：   
* @version    
*
 */
@Api(value = "爱之家首页接口文档", tags = { "爱之家首页接口" })
@RestController
@Slf4j
@RequestMapping("/api/")
public class ApiHomePageController {
	
	
	@Autowired
	private ApiRequestDataLogMongo apiRequestDataLogMongo;
	
	@Autowired
	private BannerService bannerService;
	@Autowired
	private PriceReductionGoodsService priceReductionGoodsService;

	@Autowired
	private GoodsMongo goodsMongo;
	
	@Autowired
	private ActivityGoodsRuleDetailsService activityGoodsRuleDetailsService;
	
	@ApiOperation(value = "爱之家App启动页广告列表,邀请页数据列表接口", notes = "爱之家首页接口", httpMethod = "POST")
	@RequestMapping(value = "/homePageApi", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody BaseResponseResultDto advAppBannerApi(HttpServletRequest request,
			@RequestBody BaseHomePageParamDto params) {
		log.debug(MemoryViewUtil.showMemoryStr());
		String refIp = CommRequestUtil.getRemortIP(request);

		BaseResponseResultDto resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.FAIL_CODE);
		
		PriceReductionGoodsExamineParamDto dto = null;
		HomePageInvitationPageParamDto homePageInvitationPageParamDto=null;
		try {
			if (StringUtils.isEmpty(params)) {
				resultDto.setMsg("请求参数为空。");
				return resultDto;
			}
			log.debug("========后台日志==preHandle==log===[" + refIp + "]用户request来自：" + request.getRequestURI() + "==="
					+ params);

			HomePageReceiveCodeEnums enumDtos = HomePageReceiveCodeEnums.getByCode(params.getCode());
			if (enumDtos == null) {
				resultDto.setMsg("请求参数Code[" + params.getCode() + "]有误");
				return resultDto;
			}
			switch (enumDtos) {
			case ADV_APP_LIST://
				Banner entity=new Banner();
				//过滤启动页广告banner
				entity.setType(BannerTypeEnum.ADVAPP.getCode());
				//过滤禁用状态的banner
				entity.setIsDisabled(false);
				List<Banner> result = bannerService.queryAppAdvBannerByApi(entity);
				
				Map<String, Object> dataMaps_5001 = new HashMap<>();
				List<BannerAdvAppDto>PowerPointList=new ArrayList<BannerAdvAppDto>();
				if(result.size()>0) {
					for (Banner banner : result) {
						BannerAdvAppDto temp=new BannerAdvAppDto();
						temp.setId(banner.getId());
						temp.setPType(0L);
						if(!StringUtils.isEmpty(banner.getUrl())
						   &&!StringUtils.isEmpty(banner.getImgPath())) {
							temp.setActivityUrl(banner.getUrl());
							String imgPath = banner.getImgPath();
							temp.setAppPictureUrl(banner.getImgPath());
							//截取字符串
							temp.setAppPictureUrl(imgPath.substring(imgPath.indexOf("/UploadFile/"), imgPath.length()));
						}
						PowerPointList.add(temp);
					}
				}
				dataMaps_5001.put("PowerPointList", PowerPointList);
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				resultDto.setResult(dataMaps_5001);
				break;
			case LOVE_HOME_INVITATION_PAGE_DATA_LIST:
				homePageInvitationPageParamDto=JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()), HomePageInvitationPageParamDto.class);
			/*	if (StringUtils.isEmpty(homePageInvitationPageParamDto)) {
					resultDto.setMsg("请求参数为空");
					break;
				} else if (StringUtils.isEmpty(homePageInvitationPageParamDto.getFromSys())) {
					resultDto.setMsg("调用接口出错,来源系统参数不能为空");
					break;
				} else if (StringUtils.isEmpty(homePageInvitationPageParamDto.getFromSysCode())) {
					resultDto.setMsg("调用接口出错,来源系统Code参数不能为空");
					break;
				}*/
				List<HomePageInvitationPageDataDto> loveOfHomeInvitationPageDataList = activityGoodsRuleDetailsService.queryApiLoveOfHomeInvitationPageDataList(homePageInvitationPageParamDto);
				Map<String, Object> dataMaps_5002 = new HashMap<>();
				dataMaps_5002.put("loveOfHomeInvitationPageDataList", loveOfHomeInvitationPageDataList);
				dataMaps_5002.put("fromSys",homePageInvitationPageParamDto.getFromSys());
				dataMaps_5002.put("fromSysCode",homePageInvitationPageParamDto.getFromSysCode());
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				resultDto.setResult(dataMaps_5002);
				break;
			case RESPRICE_REDUCTION_GOODS_EXAMINE:
				dto = JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()), PriceReductionGoodsExamineParamDto.class);
				if(dto.getExamineParam().size()<=0)
				{
					resultDto.setMsg("请求参数为空");
					break;
				}
				Msg<String>msg=priceReductionGoodsService.goodsExamine(dto);
				if (msg.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
				}
				resultDto.setMsg(msg.getMsg());
				break;

			case VIP_COMMODITY_ENUM:
				homePageInvitationPageParamDto=JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()), HomePageInvitationPageParamDto.class);

				if (StringUtils.isEmpty(homePageInvitationPageParamDto.getLimit())){
					resultDto.setMsg("请输入码数");
					break;
				}
				if (StringUtils.isEmpty(homePageInvitationPageParamDto.getPage())){
					resultDto.setMsg("请输入页数");
					break;
				}

				Query query=new Query();
				query.addCriteria(Criteria.where("from").is(ProductTypeEnum.ADVAPP.getCode()));
				query.with(new Sort(Sort.Direction.ASC, "sortNum"));

				PageDto page=new PageDto();
				page.setLimit(homePageInvitationPageParamDto.getLimit());
				page.setPage(homePageInvitationPageParamDto.getPage());
				Goods goods=new Goods();

				PageInfo<Goods> entitys=goodsMongo.findByQueryPage(query,page,goods);

				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				resultDto.setResult(entitys);
				resultDto.setMsg("操作成功");
				break;

			default:
				resultDto.setMsg("未知方法");
				break;
			}
		} catch (Exception e) {
			String err = "调用接口出错," + e.getLocalizedMessage();
			log.error(err, e);
			resultDto.setMsg(err);
		} finally {
			resultDto.setResponseTime(DateUtils.now());// 设置成功时间

			String action = request.getRequestURI();
			String referer = request.getHeader("Referer");

			ApiRequestDataLog apiLog = new ApiRequestDataLog();
			apiLog.setAction(action);
			apiLog.setRefAction(referer);
			apiLog.setRefIp(refIp);
			apiLog.setCreateTime(LocalDateTime.now());
			apiLog.setActionCode(params.getCode());
			apiLog.setActionSource(params.getSource());
			apiLog.setActionDeviceType(params.getDeviceType());
			apiLog.setActionPara(JsonUtils.toJson(params));
			apiLog.setReturnData(JsonUtils.toJson(resultDto));
			apiRequestDataLogMongo.insert(apiLog);
			if (!StringUtils.isEmpty(resultDto.getMsg()) && resultDto.getMsg().startsWith("调用接口出错,")) {
				resultDto.setMsg("获取后台信息服务出错[" + apiLog.getId() + "]");
			}
		}

		log.debug("---resultDto---" + params.getCode() + "==" + resultDto.getMsg());
		return resultDto;
	}
	
	
	
	
	

}
