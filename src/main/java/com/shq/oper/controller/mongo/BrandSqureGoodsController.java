package com.shq.oper.controller.mongo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.MongoDao;
import com.shq.oper.dao.mongo.HotSaleGoodsMongo;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.brand.BrandSquareModular;
import com.shq.oper.model.domain.mongo.brand.HotSaleGoods;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.ImageData;
import com.shq.oper.model.dto.ImageUploadResultData;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.brandsquare.GoodsProStandard;
import com.shq.oper.model.dto.api.brandsquare.HotSaleGoodsDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.BannerAuxiliaryService;
import com.shq.oper.service.primarydb.BannerModelGoodsDetailService;
import com.shq.oper.service.primarydb.HotSaleGoodsService;
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
@RequestMapping("/mongo/brandsquaregoods")
public class BrandSqureGoodsController extends BaseController {
	@Autowired
	private BannerAuxiliaryService bannerAuxiliaryService;
	@Autowired
	private DistributionProductMapper distributionProductMapper;
	@Autowired
	private BannerModelGoodsDetailService bannerModelGoodsDetailService;
	@Autowired
	private ShqApi ShqApi;

	@Autowired
	private HotSaleGoodsMongo hotSaleGoodsMongo;


	@Autowired
	private HotSaleGoodsService hotSaleGoodsService;

	@Autowired
	private MongoDao<BrandSquareModular> brandSquareModularMongo;


	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		BrandSquareModular entity=new BrandSquareModular();
		Query query = new Query();
		query.addCriteria(Criteria.where("columnType").is(2));
		List<BrandSquareModular> brandSquareModularList =brandSquareModularMongo.findByCollectionNameAll(query, BrandSquareModular.class, "brandSquareModular");
		request.setAttribute("brandSquareModularList",brandSquareModularList);
		return toPage(request);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<HotSaleGoodsDto> list(HotSaleGoods entity,String productName, PageDto page) {

		PageInfo<HotSaleGoodsDto> entitys=new PageInfo<>();
		try {
			entitys=hotSaleGoodsService.queryHotSaleGoodsList(entity,productName,page);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("热卖商品error", e.getMessage());
			return Data.ok(new 	PageInfo<HotSaleGoodsDto>());
		}
		return Data.ok(entitys);
	}

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, HotSaleGoods entity) {

		if(StringUtils.isEmpty(entity.getGoodsCode())) {
			return Msg.error("商品Code不能为空");
		}
		if(StringUtils.isEmpty(entity.getColumnId())) {
			return Msg.error("模块Id不能为空");
		}
		if(StringUtils.isEmpty(entity.getSortNum())) {
			return Msg.error("排序不能为空");
		}

		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/hotSaleGoodsAction/addOrUpdateHotSaleGoods";
		List<HotSaleGoods> paramList=new ArrayList<>();
		Map<String, Object> params=new HashMap<>();
		if (StringUtils.isEmpty(entity.getId())) {//新增
			entity.setId(null);
			paramList.add(entity);
			String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(paramList));
			Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
			String object = resultMap.get("code")+"";
			if(object.equals("200")) {
				return Msg.ok("保存成功");
			}else {
				return Msg.error(resultMap.get("message")+"");
			}
		}else {
			HotSaleGoods entityTmp=new HotSaleGoods();
			entityTmp.setId(entity.getId());
			entityTmp.setSortNum(entity.getSortNum());
			entityTmp.setColumnId(entity.getColumnId());
			paramList.add(entityTmp);
			String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(paramList));
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
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/hotSaleGoodsAction/deleteHotSaleGoods?hotSaleGoodsId="+id;
		String resultJson = HttpClientUtil.doGet(url);
		if(StringUtils.isEmpty(resultJson)) {
			return Msg.error("接口调用错误");
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("code")+"";
		if(object.equals("200")) {
			return Msg.ok("删除成功");
		}else {
			return Msg.error(resultMap.get("message")+"");
		}
	}

	@RequestMapping("/bindingToGoods")
	private ModelAndView bindingToGoods(HttpServletRequest request,HotSaleGoods hotSaleGoods
			,String operateType) {
/*		if("1".equals(operateType)) {
			request.setAttribute("operateType", 1);
			request.setAttribute("id", null);
		}else {
			DistributionProduct entity=new DistributionProduct();
			entity.setProductCode(hotSaleGoods.getGoodsCode());
			Msg<HotSaleGoodsDto> findByCode = this.findByCode(entity);
			request.setAttribute("product", result);
			request.setAttribute("operateType", 2);
			request.setAttribute("priceReductionGoodsTemp",findByCode.getValue());
		}*/
		return this.toPage(request);
	}

	@RequestMapping("/findByCode")
	private Msg<HotSaleGoodsDto> findByCode(DistributionProduct entity) {

		DistributionProduct resultTmp = distributionProductMapper.selectOne(entity);
		if (StringUtils.isEmpty(resultTmp)) {
			return Msg.error("查询失败，没有此商品", null);
		}

		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/brandShopInfoAction/findGoodsCodeIsExist?goodsCode="+entity.getProductCode();
		String resultJson = HttpClientUtil.doGet(url);
		if(StringUtils.isEmpty(resultJson)) {
			return Msg.error("查询接口错误", null);
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("code")+"";
		if(!object.equals("200")) {
			return Msg.error("该商品不是品牌商品", null);
		}
		Object data = resultMap.get("data");
		HotSaleGoodsDto result =JsonUtils.parse(JsonUtils.toDefaultJson(data), HotSaleGoodsDto.class);
		if(result.getGoodsProStandard().size()>0) {
			GoodsProStandard goodsProStandard = result.getGoodsProStandard().get(0);
			if(!StringUtils.isEmpty(goodsProStandard.getGoodsPrice())) {
				result.setGoodsPrice(goodsProStandard.getGoodsPrice());
			}
		}else {
			result.setGoodsPrice(result.getDistributionPrice());
		}
		if(!StringUtils.isEmpty(resultTmp.getCompanyName())) {
			result.setCompanyName(resultTmp.getCompanyName());
		}
		return Msg.ok(result);
	}

	@RequestMapping("/updateShowState")
	public Msg<String> updateShowState(HttpServletRequest request, HotSaleGoods entity) throws Exception {

		if(StringUtils.isEmpty(entity.getId())||StringUtils.isEmpty(entity.getActivateStatus())) {
			return Msg.error("数据异常");
		}
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/hotSaleGoodsAction/addOrUpdateHotSaleGoods";
		HotSaleGoods entityTmp=new HotSaleGoods();
		entityTmp.setId(entity.getId());
		entityTmp.setActivateStatus(entity.getActivateStatus());
		List<HotSaleGoods>paramList=new ArrayList<>();
		paramList.add(entityTmp);
		String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(paramList));
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("code")+"";
		if(object.equals("200")) {
			return Msg.ok("操作成功");
		}else {
			return Msg.error(resultMap.get("message")+"");
		}
	}
}
