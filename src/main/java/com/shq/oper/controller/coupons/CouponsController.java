package com.shq.oper.controller.coupons;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.enums.CouponsTypeEnum;
import com.shq.oper.enums.CouponsValiDayTypeEnum;
import com.shq.oper.enums.SendTypeEnum;
import com.shq.oper.enums.UseRangeTypeEnum;
import com.shq.oper.enums.api.CouponsFromSysEnums;
import com.shq.oper.mapper.primarydb.CouponsMapper;
import com.shq.oper.mapper.primarydb.CouponsUserMapper;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.ImageData;
import com.shq.oper.model.dto.ImageUploadResultData;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.ValiDayDetailDto;
import com.shq.oper.model.dto.ValidityDetailDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.impl.primarydb.AdminServiceImpl;
import com.shq.oper.service.primarydb.CouponsService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author ljz
 * @date 2018年4月24日
 */
@RestController
@Slf4j
@RequestMapping("/coupons/coupons")
public class CouponsController extends BaseController {

	@Autowired
	SystemProperties systemProperties;

	@Autowired
	CouponsService couponsService;
	@Autowired
	CouponsMapper couponsMapper;
	@Autowired
	private ShqApi ShqApi;
	@Autowired
	CouponsUserMapper couponsUserMapper;
	private Map<String, Object> result;

	
	
	/**
	 * 角色页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		Map<String, Object> operSysList = CouponsFromSysEnums.getObjectMap();
		request.setAttribute("operSysList", operSysList);
		return toPage(request);
	}

	@RequestMapping("/add")
	public ModelAndView add(HttpServletRequest request, String type, Long id) throws Exception {
		// 获取当前时间日期字符串
		String tmpBatch = nowTimeString();
		Map<String, Object> couponsTypeList = CouponsTypeEnum.getMap();
		Map<String, Object> useRangeTypeList = UseRangeTypeEnum.getMap();
		Map<String, Object> sendTypeList = SendTypeEnum.getMap();
		Map<String, Object> valiDayTypeList = CouponsValiDayTypeEnum.getMap();
		request.setAttribute("couponsTypeList", couponsTypeList);
		request.setAttribute("useRangeTypeList", useRangeTypeList);
		request.setAttribute("sendTypeList", sendTypeList);
		request.setAttribute("valiDayTypeList", valiDayTypeList);
		request.setAttribute("tmpBatch", tmpBatch);

		if ("1".equals(type)) {
			request.setAttribute("editType", "1");
		} else if ("2".equals(type)) {
			Coupons coupons = new Coupons();
			coupons.setId(id);
			Coupons couponsData = couponsService.selectOne(coupons);
			ValidityDetailDto parse = JsonUtils.parse(couponsData.getValiDayTypeDetail(), ValidityDetailDto.class);
			request.setAttribute("editType", "2");
			request.setAttribute("vali_day_num", parse.getVali_day_num());
			request.setAttribute("vali_day_start", parse.getVali_day_start());
			request.setAttribute("vali_day_end", parse.getVali_day_end());
			couponsData.setValiDayTypeDetail("");
			request.setAttribute("couponDes", couponsData.getCouponDes());
			couponsData.setCouponDes("");
			request.setAttribute("couponsData", couponsData);

		}
		return toPage(request);
	}

	@RequestMapping("/couponsDetails")
	public ModelAndView couponsDetails(HttpServletRequest request, Coupons coupons) throws Exception {
		Coupons couponsDetails = couponsService.selectOne(coupons);
		ValidityDetailDto dto = JSON.parseObject(couponsDetails.getValiDayTypeDetail(), ValidityDetailDto.class);
		String valiDayTypeDetail = "";
		if (CouponsValiDayTypeEnum.USE_AFTER_DAYS.getCode() == couponsDetails.getValiDayType()) {
			valiDayTypeDetail = "领取优惠券" + dto.getVali_day_num() + "天后结束";
		} else {
			valiDayTypeDetail = dto.getVali_day_start() + "——" + dto.getVali_day_end();
		}
		int sellNum = couponsUserMapper.queryCouponsSellNum(coupons.getId());
		couponsDetails.setValiDayTypeDetail(null);
		if(!StringUtils.isEmpty(couponsDetails.getCouponDes())) {
			couponsDetails.setCouponDes(delHtmlTag(couponsDetails.getCouponDes()));
		}
		request.setAttribute("couponsDetails", couponsDetails);
		request.setAttribute("valiDayTypeDetail", valiDayTypeDetail);
		request.setAttribute("sellNum", sellNum);
		return toPage(request);
	}
		public static String delHtmlTag(String str){ 
	    String newstr = ""; 
	    newstr = str.replaceAll("<[.[^>]]*>","");
	    newstr = newstr.replaceAll(" ", ""); 
	    return newstr;
		}
	/**
	 * 添加优惠券
	 * 
	 * @param request
	 * @param coupons
	 * @param valiDayDetailDto
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.GET, RequestMethod.POST })
	public Msg<String> save(HttpServletRequest request, Coupons coupons, ValiDayDetailDto valiDayDetailDto)
			throws IOException {
		if (coupons.getSendTimeStart().isAfter(coupons.getSendTimeEnd())) {
			return Msg.error("抱歉，优惠券开始时间不能大于优惠券结束时间");
		}
		if (coupons.getValiDayType() == 0) {
			if (valiDayDetailDto.getTimeBegin().isAfter(valiDayDetailDto.getTimeEnd())) {
				return Msg.error("抱歉，优惠券发放时间不能大于优惠券截至时间");
			}
		}
		result = new HashMap<>();
		// 判断有效期的类型，然后设置对应的有效期详情
		if (StringUtils.isEmpty(valiDayDetailDto.getDays())) {
			// 有效期天数为空，则类型为指定时间
			result.put("vali_day_start", DateUtils.to(valiDayDetailDto.getTimeBegin()));
			result.put("vali_day_end", DateUtils.to(valiDayDetailDto.getTimeEnd()));
			coupons.setValiDayTypeDetail(JsonUtils.toDefaultJson(result));
		} else {
			result.put("vali_day_num", valiDayDetailDto.getDays());
			// 有效期设置为指定天数
			coupons.setValiDayTypeDetail(JsonUtils.toDefaultJson(result));
		}

		try {// 用户领取规则限制
			if (!coupons.getReceiveNumRule().matches("^[0-9]*$")) {
				return Msg.error("请正确输入用户领取数量");
			}
			/*
			 * int recNum = Integer.parseInt(coupons.getReceiveNumRule());
			 * Map<String,Integer> userReceiveRuleMap = new HashMap<>();
			 * userReceiveRuleMap.put(CouponsUserReceiveRuleEnum.RECEIVE_ALL_NUM.getKey(),
			 * recNum );
			 */
			coupons.setReceiveNumRule(coupons.getReceiveNumRule());
		} catch (Exception e) {
			return Msg.error("请输入正确的用户领取数量!");
		}

		LocalDateTime dtNow = LocalDateTime.now();
		coupons.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		coupons.setUpdateTime(dtNow);
		coupons.setUsePlatform("520shq");
		// 判断是新增还是修改
		if (ObjectUtils.isEmpty(coupons.getId())) {
			coupons.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			coupons.setCreateTime(dtNow);
			couponsService.insert(coupons);
		} else {
			if(StringUtils.isEmpty(coupons.getCouponsHrefUrl())) {
				coupons.setCouponsHrefUrl(" ");
			}
			couponsService.update(coupons);
			// 编辑优惠券。。说明文字。清除缓存
			SpringContextHolder.getBean(AdminServiceImpl.class)
					.clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ);
		}

		// 清除Coupons-query- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class)
				.clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONS_QUERY);

		return Msg.ok("保存成功");
	}

	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, Coupons coupons) {
		LocalDateTime dtNow = LocalDateTime.now();
		coupons.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		coupons.setUpdateTime(dtNow);
		couponsService.disabledCouponsById(coupons);

		// 清除Coupons-query- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class)
				.clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONS_QUERY);
		return Msg.ok("保存成功");
	}

	@RequestMapping("/enableBy")
	public Msg<String> enableById(HttpServletRequest request, Coupons coupons) {
		LocalDateTime dtNow = LocalDateTime.now();
		coupons.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		coupons.setUpdateTime(dtNow);
		couponsService.enableById(coupons);

		// 清除Coupons-query- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class)
				.clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONS_QUERY);

		return Msg.ok("保存成功");
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Coupons> list(Coupons entity, PageDto pageDto) {
		if (!StringUtils.isEmpty(entity.getTimeRange())){
			String[] strs=entity.getTimeRange().split("~");
			entity.setSendTimeStart(DateUtils.parse(strs[0].trim()));
			entity.setSendTimeEnd(DateUtils.parse(strs[1].trim()));
		}
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<Coupons> entitys = couponsMapper.queryCouponsAndCouponsCategoryRuleAndCouponsTypeRule(entity);
		return Data.ok(entitys);
	}

	
	@PostMapping("/upload")
	public ImageUploadResultData uploadImgSingle(@RequestParam("file") MultipartFile imgFile,
			HttpServletRequest request) throws Exception {

		ImageUploadResultData result = new ImageUploadResultData();
		ImageData data = new ImageData();

		String originalFilename = imgFile.getOriginalFilename();
		int index = originalFilename.lastIndexOf(".");
		String imagTypeName = originalFilename.substring(index, originalFilename.length());
		if (!(".jpg".equals(imagTypeName) || ".bmp".equals(imagTypeName) || ".png".equals(imagTypeName)
				|| ".jpeg".equals(imagTypeName))) {
			data.setSrc("");
			data.setTitle("");
			result.setCode(1);
			result.setMsg("请上传符合规定的图片类型文件！");
			result.setData(null);
			return result;
		}
		// "data:image/png;base64,",图片转Base64字符串后的前缀
		String imageBase64String = "data:image/png;base64," + Base64.byteArrayToBase64(imgFile.getBytes());
		Map<String, Object> param = new HashMap<>();
		param.put("imgBase64", imageBase64String);
		imageBase64String = new ObjectMapper().writeValueAsString(param);
		String imageUrlResult = HttpClientUtil.doPostJson(ShqApi.getImageUploadUrl(), imageBase64String);
		if (!"".equals(imageUrlResult)) {
			String[] imageUrlResultArray = imageUrlResult.split(",");
			String imagePath = ShqApi.getImageAddrUrl()
					+ imageUrlResultArray[1].substring(1, imageUrlResultArray[1].length() - 1)
					+ imageUrlResultArray[0].substring(1, imageUrlResultArray[0].length());
			data.setSrc(imagePath);
			data.setTitle(imageUrlResultArray[0]);
			result.setCode(0);
			result.setMsg("图片上传成功");
			result.setData(data);
			return result;
		} else {
			result.setCode(1);
			result.setMsg("图片上传失败");
			result.setData(null);
			return result;
		}

	}

	/**
	 * 将当前时间转为日期字符串
	 * 
	 * @return
	 */
	public String nowTimeString() {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		String ma = "yyyyMMddHHmmss";
		SimpleDateFormat forma = new SimpleDateFormat(ma);
		String nwdate = forma.format(date);
		return nwdate;
	}
}