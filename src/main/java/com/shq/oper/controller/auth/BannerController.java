package com.shq.oper.controller.auth;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.primarydb.BannerMapper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.domain.primarydb.BannerAuxiliary;
import com.shq.oper.model.domain.primarydb.BannerModelGoodsDetail;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.ImageData;
import com.shq.oper.model.dto.ImageUploadResultData;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.BannerAuxiliaryService;
import com.shq.oper.service.primarydb.BannerModelGoodsDetailService;
import com.shq.oper.service.primarydb.BannerService;
import com.shq.oper.util.HttpClientUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Banner
 * 
 * @author Administrator
 *
 */
@RestController
@Slf4j
@RequestMapping("/auth/banner")
public class BannerController extends BaseController {

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
	private ShqApi ShqApi;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/manageIndex")
	public ModelAndView manageIndex(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/goodsSetting")
	public ModelAndView goodsSetting(HttpServletRequest request, String id) {
		request.setAttribute("id", id);
		return toPage(request);
	}

	@RequestMapping("/bindingToGoods")
	public ModelAndView addGoods(HttpServletRequest request, String bannerId,String operateType,
			String productCode,Long id) {
		if("1".equals(operateType)) {
			request.setAttribute("operateType", 1);
			request.setAttribute("id", null);
		}else {
			BannerModelGoodsDetail btmp=new BannerModelGoodsDetail();
			btmp.setBannerId(Long.parseLong(bannerId));
			btmp.setGoodsCode(productCode);
			BannerModelGoodsDetail selectOne = bannerModelGoodsDetailService.selectOne(btmp);
			DistributionProduct entity=new DistributionProduct();
			entity.setProductCode(productCode);
			DistributionProduct result = distributionProductMapper.selectOne(entity);
			result.setProductDetails("");
			request.setAttribute("product", result);
			request.setAttribute("operateType", 2);
			request.setAttribute("id", id);
			request.setAttribute("sortNo", selectOne.getSortNo());
			request.setAttribute("isDisabled", selectOne.getIsDisabled());
		}
		request.setAttribute("bannerId", bannerId);
		return toPage(request);
	}

	@RequestMapping("/detailsSetting")
	public ModelAndView detailsSetting(HttpServletRequest request, Banner banner) {

		BannerAuxiliary entity = new BannerAuxiliary();
		entity.setParentId(banner.getId());
		BannerAuxiliary bannerAuxiliary = bannerAuxiliaryService.selectOne(entity);

		request.setAttribute("banner", banner);
		request.setAttribute("bannerAuxiliary", bannerAuxiliary);
		return toPage(request);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Banner> list(Banner entity, PageDto page) {
		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
		Page<Banner> entitys = bannerMapper.queryHomePageBanner(entity);
		return Data.ok(entitys);
	}

	@RequestMapping("/bannerDetails")
	public ModelAndView bannerDetails(HttpServletRequest request, Banner banner) throws Exception {
		Banner bannerDetails = bannerService.selectOne(banner);
		request.setAttribute("bannerDetails", bannerDetails);
		return toPage(request);
	}

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, Banner banner) {

		banner.setUpdateTime(LocalDateTime.now());
		banner.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		if (ObjectUtils.isEmpty(banner.getId())) {
			banner.setCreateTime(LocalDateTime.now());
			banner.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			bannerService.insert(banner);
		} else {
			bannerService.update(banner);
		}
		return Msg.ok("保存成功");
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

	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, Banner banner) {
		banner.setIsDisabled(true);
		banner.setUpdateTime(LocalDateTime.now());
		bannerService.disabledById(banner);
		return Msg.ok("保存成功");
	}

	@RequestMapping("/enableBy")
	public Msg<String> enableById(HttpServletRequest request, Banner banner) {
		LocalDateTime dtNow = LocalDateTime.now();
		banner.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		banner.setUpdateTime(dtNow);
		bannerService.enableById(banner);
		return Msg.ok("保存成功");
	}
}
