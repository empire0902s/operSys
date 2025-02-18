package com.shq.oper.controller.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.MongoDao;
import com.shq.oper.enums.api.CouponsFromSysEnums;
import com.shq.oper.mapper.primarydb.BannerMapper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.brand.BrandDto;
import com.shq.oper.model.domain.mongo.brand.BrandRecommend;
import com.shq.oper.model.domain.mongo.brand.BrandSquareModular;
import com.shq.oper.model.domain.primarydb.Activity;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.ImageData;
import com.shq.oper.model.dto.ImageUploadResultData;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.BannerAuxiliaryService;
import com.shq.oper.service.primarydb.BannerModelGoodsDetailService;
import com.shq.oper.service.primarydb.BannerService;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Banner
 *
 * @author Administrator
 *
 */
@RestController
@Slf4j
@RequestMapping("/mongo/brandrecommend")
public class BrandRecommendController extends BaseController {
	@Autowired
	private BannerService bannerService;
	@Autowired
	private BannerMapper bannerMapper;
	@Autowired
	private BannerAuxiliaryService bannerAuxiliaryService;
	@Autowired
	private DistributionProductMapper distributionProductMapper;
	@Autowired
	private BannerModelGoodsDetailService bannerModelGoodsDetailService;
	@Autowired
	private MongoDao<BrandRecommend> brandRecommendMongo;
	@Autowired
	private MongoDao<BrandSquareModular> brandSquareModularMongo;
	@Autowired
	private ShqApi ShqApi;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		BrandSquareModular entity=new BrandSquareModular();
		Query query = new Query();
		query.addCriteria(Criteria.where("columnType").is(1));
		List<BrandSquareModular> brandSquareModularList =brandSquareModularMongo.findByCollectionNameAll(query, BrandSquareModular.class, "brandSquareModular");
		request.setAttribute("brandSquareModularList",brandSquareModularList);
		return toPage(request);
	}
	@RequestMapping("/addbrandrecommend")
	public ModelAndView addBrandreCommend(HttpServletRequest request,long array) {
		Integer sortNum=0;
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/brandRecommendAction/listAllBrandRecommend";
		String resultJson = HttpClientUtil.doGet(url);
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		Object object = resultMap.get("data");
		List<BrandRecommend> resultList = JSONObject.parseArray(JsonUtils.toDefaultJson(object), BrandRecommend.class);

		if(resultList.size()>0) {
			BrandRecommend brandRecommend = resultList.get(resultList.size()-1);
			if(StringUtils.isEmpty(brandRecommend.getSortNum())) {
				sortNum=brandRecommend.getSortNum();
			}
		}
		request.setAttribute("array", array);
		request.setAttribute("sortNum", sortNum);
		return toPage(request);
	}
	@RequestMapping("/addbrandrecommendwindow")
	public ModelAndView addBrandreCommendWindow(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<BrandRecommend> list(BrandRecommend entity, PageDto page) {

		Criteria cr=new Criteria();
		Criteria c1=Criteria.where("shopSeq").is(null);
		Criteria c2=Criteria.where("shopSeq").is("");
		Query query = new Query(cr.orOperator(c1,c2));
		// 升序排序
		query.with(new Sort(new Order(Direction.ASC, "sortNum")));
		if (!StringUtils.isEmpty(entity.getBrandName())) {
			query.addCriteria(Criteria.where("brandName").regex(".*?" +entity.getBrandName()+ ".*"));
		}
		PageInfo<BrandRecommend> entitys = brandRecommendMongo.findByQueryPage(query, page, entity);
		if(!StringUtils.isEmpty(entitys.getList())&&entitys.getList().size()>0) {
			for (BrandRecommend brandRecommend : entitys.getList()) {
				brandRecommend.setBrandLogoImg(ShqApi.getImageAddrUrl()+brandRecommend.getBrandLogoImg());
			}
		}
		return Data.ok(entitys);
	}

	@RequestMapping("/brandrecommendDetails")
	public ModelAndView bannerDetails(HttpServletRequest request, String id) throws Exception {
		BrandRecommend brandrecommendDetails =new BrandRecommend();
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/brandRecommendAction/listAllBrandRecommend";
		String resultJson = HttpClientUtil.doGet(url);
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		Object object = resultMap.get("data");
		List<BrandRecommend> resultList = JSONObject.parseArray(JsonUtils.toDefaultJson(object), BrandRecommend.class);
		if(resultList.size()>0) {
			for (BrandRecommend brandRecommend : resultList) {
				if(id.equals(brandRecommend.getId())) {
					brandRecommend.setBrandLogoImg(ShqApi.getImageAddrUrl()+brandRecommend.getBrandLogoImg());
					brandrecommendDetails=brandRecommend;
					break;
				}
			}
		}
		request.setAttribute("brandrecommendDetails", brandrecommendDetails);
		return toPage(request);
	}

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, BrandDto brandRecommend) {

		List<String> brandLogoImgList = removeNull(brandRecommend.getBrandLogoImg());
		List<String> brandNameList = removeNull(brandRecommend.getBrandName());
		List<String> jumpTargetList= removeNull(brandRecommend.getJumpTarget());
		List<Integer> sortNumList = removeNull(brandRecommend.getSortNum());
		List<String> columnIdList = removeNull(brandRecommend.getColumnId());

		if(brandLogoImgList.size()!=brandNameList.size()
				||brandNameList.size()!=jumpTargetList.size()
				||jumpTargetList.size()!=sortNumList.size()
				||sortNumList.size()!=columnIdList.size()) {
			return Msg.error("保存失败,原因是未完善全部信息");
		}
		if(brandLogoImgList.size()>8
				||brandNameList.size()>8
				||jumpTargetList.size()>8
				||sortNumList.size()>8
				||columnIdList.size()>8) {
			return Msg.error("一次最多添加8个品牌推荐信息");
		}

		if(brandLogoImgList.contains("")||brandNameList.contains("")||jumpTargetList.contains("")||sortNumList.contains("")||columnIdList.contains("")) {
			return Msg.error("保存失败,原因是未完善全部信息");
		}
		//将list放入set中对其去重
		Set<Integer> set = new HashSet<>(sortNumList);
		//获得list与set的差集
		Collection rs = CollectionUtils.disjunction(sortNumList,set);
		//将collection转换为list
		List<String> list1 = new ArrayList<>(rs);
		if(list1.size()>0) {
			return Msg.error("存在相同的sortNum编号："+JsonUtils.toDefaultJson(list1));
		}
		if(brandLogoImgList.contains(null)) {
			return Msg.error("保存失败,请上传图片并等待图片上传成功");
		}


		List<BrandRecommend> brandRecommendList =new ArrayList<>();
		for (int i=0;i<brandLogoImgList.size();i++) {
			BrandRecommend brandRecommendTmp=new BrandRecommend();
			brandRecommendList.add(brandRecommendTmp);
		}

		for (int i=0;i<brandRecommendList.size();i++) {
			BrandRecommend brandRecommendTmp = brandRecommendList.get(i);
			brandRecommendTmp.setBrandLogoImg(brandLogoImgList.get(i).substring(brandLogoImgList.get(i).indexOf("/UploadFile/"),brandLogoImgList.get(i).length()));
			brandRecommendTmp.setBrandName(brandNameList.get(i));
			brandRecommendTmp.setJumpTarget(jumpTargetList.get(i));
			brandRecommendTmp.setSortNum(sortNumList.get(i));
			brandRecommendTmp.setColumnId(columnIdList.get(i));
		}
		String url=ShqApi.getGetBannerUrl()+"localQuickPurchase/brandRecommendAction/addOrUpdateBrandRecommend";
		String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(brandRecommendList));
		if(StringUtils.isEmpty(resultJson)) {
			return Msg.error("调用接口出错");
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("code")+"";
		if(object.equals("200")) {
			return Msg.ok("保存成功");
		}else {
			return Msg.error(resultMap.get("message")+"");
		}
	}


	@RequestMapping("/editSave")
	public Msg<String> editSave(HttpServletRequest request, BrandRecommend brandRecommend) {

		if(StringUtils.isEmpty(brandRecommend.getId())||StringUtils.isEmpty(brandRecommend.getBrandLogoImg())
				||StringUtils.isEmpty(brandRecommend.getBrandName())||StringUtils.isEmpty(brandRecommend.getJumpTarget())
				||StringUtils.isEmpty(brandRecommend.getSortNum())) {
			return Msg.error("数据错误");
		}
		brandRecommend.setBrandLogoImg(brandRecommend.getBrandLogoImg().substring(brandRecommend.getBrandLogoImg().indexOf("/UploadFile/"),brandRecommend.getBrandLogoImg().length()));
		List<BrandRecommend>paramList=new ArrayList<>();
		paramList.add(brandRecommend);
		String url=ShqApi.getGetBannerUrl()+"localQuickPurchase/brandRecommendAction/addOrUpdateBrandRecommend";
		String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(paramList));
		if(StringUtils.isEmpty(resultJson)) {
			return Msg.error("调用接口出错");
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("code")+"";
		if(object.equals("200")) {
			return Msg.ok("保存成功");
		}else {
			return Msg.error(resultMap.get("message")+"");
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
		if(StringUtils.isEmpty(imageUrlResult)) {
			result.setCode(1);
			result.setMsg("图片上传失败");
			result.setData(null);
			return result;
		}
		if (!"".equals(imageUrlResult)) {
			String[] imageUrlResultArray = imageUrlResult.split(",");
			String imagePath =ShqApi.getImageAddrUrl()+imageUrlResultArray[1].substring(1, imageUrlResultArray[1].length() - 1)
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
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/brandRecommendAction/deleteBrandRecommend?brandRecommendId="+id;
		String resultJson = HttpClientUtil.doGet(url);
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("code")+"";
		if(object.equals("200")) {
			return Msg.ok("删除成功");
		}else {
			return Msg.error(resultMap.get("message")+"");
		}
	}
	/**
	 *系统来源 下拉框数据 
	 **/
	@GetMapping(value = "/selectValue")
	public List<SelectValueData<String>> fromsysDict(HttpServletRequest request) {

		BrandSquareModular entity=new BrandSquareModular();
		Query query = new Query();
		query.addCriteria(Criteria.where("columnType").is(1));
		List<BrandSquareModular> brandSquareModularList =brandSquareModularMongo.findByCollectionNameAll(query, BrandSquareModular.class, "brandSquareModular");
		if(brandSquareModularList.size()<=0) {
			return new ArrayList<SelectValueData<String>>();
		}
		for (BrandSquareModular brandSquareModular : brandSquareModularList) {
			if(StringUtils.isEmpty(brandSquareModular.getId())||StringUtils.isEmpty(brandSquareModular.getModularName())) {
				return new ArrayList<SelectValueData<String>>();
			}
		}
		Map<String, Object> operSysMap = new HashMap<>();
		List<SelectValueData<String>> list=new ArrayList<>();
		for(BrandSquareModular key : brandSquareModularList) {
			list.add(new SelectValueData<String>(key.getModularName(),key.getId()));
		}
		return list;
	}
	public static <T> List<T> removeNull(List<? extends T> oldList) {

		// 你没有看错，真的是有 1 行代码就实现了
		oldList.removeAll(Collections.singleton(null));
		return (List<T>) oldList;
	}
}
