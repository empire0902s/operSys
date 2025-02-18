package com.shq.oper.controller.mongo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.BrandSquareBannerMongo;
import com.shq.oper.dao.mongo.HomePageModuleMongo;
import com.shq.oper.mapper.primarydb.BannerMapper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.brand.BrandSquareBanner;
import com.shq.oper.model.domain.mongo.brand.BrandSquareModular;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.domain.primarydb.BannerAuxiliary;
import com.shq.oper.model.domain.primarydb.BannerModelGoodsDetail;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.ImageData;
import com.shq.oper.model.dto.ImageUploadResultData;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.PriceReductionGoodsDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.BannerAuxiliaryService;
import com.shq.oper.service.primarydb.BannerModelGoodsDetailService;
import com.shq.oper.service.primarydb.BannerService;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ljz
 *
 */
@RestController
@Slf4j
@RequestMapping("/mongo/brandsquarebanner")
public class BrandSqureBannerController extends BaseController {
	@Autowired
	private BannerAuxiliaryService bannerAuxiliaryService;
	@Autowired
	private DistributionProductMapper distributionProductMapper;
	@Autowired
	private BannerModelGoodsDetailService bannerModelGoodsDetailService;
	@Autowired
	private ShqApi ShqApi;

	@Autowired
	private BrandSquareBannerMongo mongoDao;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<BrandSquareBanner> list(BrandSquareBanner entity, PageDto page) {
		Query query = new Query();
		// 升序排序
		query.with(new Sort(new Order(Direction.ASC, "sortNum")));
		if (!StringUtils.isEmpty(entity.getTitle())) {
			query.addCriteria(Criteria.where("title").regex(".*?" +entity.getTitle()+ ".*"));
		}
		if (!StringUtils.isEmpty(entity.getIdentifyType())) {
			query.addCriteria(Criteria.where("identifyType").is(entity.getIdentifyType()));
		}
		PageInfo<BrandSquareBanner> entitys = mongoDao.findByQueryPage(query, page, entity);
		if(!StringUtils.isEmpty(entitys.getList())&&entitys.getList().size()>0) {
			for (BrandSquareBanner brandSquareBanner : entitys.getList()) {
				brandSquareBanner.setImageLocation(ShqApi.getImageAddrUrl()+brandSquareBanner.getImageLocation());
			}
		}
		return Data.ok(entitys);
	}

	@RequestMapping("/bannerDetails")
	public ModelAndView bannerDetails(HttpServletRequest request, String id) throws Exception {
		BrandSquareBanner brandBannerDetails =new BrandSquareBanner();
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/brandSquareAction/listAllBrandSquareBanner?identifyType=7";
		String resultJson = HttpClientUtil.doGet(url);
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		Object object = resultMap.get("data");
		List<BrandSquareBanner> resultList = JSONObject.parseArray(JsonUtils.toDefaultJson(object), BrandSquareBanner.class);
		if(resultList.size()>0) {
			for (BrandSquareBanner brandSquareBanner : resultList) {
				if(id.equals(brandSquareBanner.getId())) {
					brandSquareBanner.setImageLocation(ShqApi.getImageAddrUrl()+brandSquareBanner.getImageLocation());
					brandBannerDetails=brandSquareBanner;
					break;
				}
			}
		}
		request.setAttribute("bannerDetails", brandBannerDetails);
		return toPage(request);
	}

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, BrandSquareBanner brandSquareBanner) {

		if(StringUtils.isEmpty(brandSquareBanner.getIdentifyType())) {
			return Msg.error("识别标识(identifyType)不能为空");
		}
		if(StringUtils.isEmpty(brandSquareBanner.getImageLocation())) {
			return Msg.error("图片链接不能为空");
		}
		if(StringUtils.isEmpty(brandSquareBanner.getSortNum())) {
			return Msg.error("排序不能为空");
		}

		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/brandSquareAction/addOrUpdateBrandSquareBanner";
		Map<String, Object> params=new HashMap<>();
		if (StringUtils.isEmpty(brandSquareBanner.getId())) {//新增
			params.put("imageLocation",brandSquareBanner.getImageLocation());
			params.put("identifyType",brandSquareBanner.getIdentifyType());
			params.put("sortNum",brandSquareBanner.getSortNum());
			params.put("title",brandSquareBanner.getTitle());
			params.put("jumpTarget",brandSquareBanner.getJumpTarget());
			params.put("content",brandSquareBanner.getContent());
			String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(params));
			if(StringUtils.isEmpty(resultJson)) {
				return Msg.error("调用接口错误");
			}
			Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
			String object = resultMap.get("code")+"";
			if(object.equals("200")) {
				return Msg.ok("保存成功");
			}else {
				return Msg.error(resultMap.get("message")+"");
			}
		}else {
			params.put("id",brandSquareBanner.getId());
			params.put("imageLocation",brandSquareBanner.getImageLocation().substring(brandSquareBanner.getImageLocation().indexOf("/UploadFile/"), brandSquareBanner.getImageLocation().length()));
			params.put("identifyType",brandSquareBanner.getIdentifyType());
			params.put("sortNum",brandSquareBanner.getSortNum());
			params.put("title",brandSquareBanner.getTitle());
			params.put("jumpTarget",brandSquareBanner.getJumpTarget());
			params.put("content",brandSquareBanner.getContent());
			String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(params));
			if(StringUtils.isEmpty(resultJson)) {
				return Msg.error("调用接口错误");
			}
			Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
			String object = resultMap.get("code")+"";
			if(object.equals("200")) {
				return Msg.ok("保存成功");
			}else {
				return Msg.error(resultMap.get("message")+"");
			}
		}
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
		String imageUrlResult = HttpClientUtil.doPostJson(ShqApi.getImageUploadUrl(),
				imageBase64String);
		if (!"".equals(imageUrlResult)) {
			String[] imageUrlResultArray = imageUrlResult.split(",");
			String imagePath = imageUrlResultArray[1].substring(1, imageUrlResultArray[1].length() - 1)
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


	@RequestMapping("/delete")
	public Msg<String> enableById(HttpServletRequest request, String id) {
		if(StringUtils.isEmpty(id)) {
			return Msg.error("数据有误,请稍后重试");
		}
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/brandSquareAction/deleteBrandSquareBanner?bannerId="+id;
		String resultJson = HttpClientUtil.doGet(url);
		if(StringUtils.isEmpty(resultJson)) {
			return Msg.error("调用接口错误");
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("code")+"";
		if(object.equals("200")) {
			return Msg.ok("删除成功");
		}else {
			return Msg.error(resultMap.get("message")+"");
		}
	}
}
