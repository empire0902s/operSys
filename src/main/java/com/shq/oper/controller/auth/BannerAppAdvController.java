package com.shq.oper.controller.auth;

import com.alibaba.druid.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.enums.BannerTypeEnum;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.dto.*;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.BannerAppAdvService;
import com.shq.oper.service.primarydb.BannerService;
import com.shq.oper.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Banner
 * 
 * @author Administrator
 *
 */
@RestController
@Slf4j
@RequestMapping("/auth/banner/app/adv")
public class BannerAppAdvController extends BaseController {

	@Autowired
	private BannerService bannerService;
	@Autowired
	private BannerAppAdvService bannerAppAdvService;
	@Autowired
	private ShqApi ShqApi;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
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

	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, Banner banner) {
		banner.setIsDisabled(true);
		banner.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		banner.setUpdateTime(LocalDateTime.now());
		bannerService.disabledById(banner);
		return Msg.ok("保存成功");
	}

	@RequestMapping("/enableBy")
	public Msg<String> enableById(HttpServletRequest request, Banner banner) {
		LocalDateTime dtNow = LocalDateTime.now();
		banner.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		banner.setUpdateTime(dtNow);
		bannerService.enableBannerById(banner);
		return Msg.ok("保存成功");
	}
	@RequestMapping("/bannerDetails")
	public ModelAndView bannerDetails(HttpServletRequest request, Banner banner) throws Exception {
		Banner bannerDetails = bannerService.selectOne(banner);
		request.setAttribute("bannerDetails", bannerDetails);
		return toPage(request);
	}
	@RequestMapping("/dataInitialization")
	public Msg<String> dataInitialization(HttpServletRequest request) {
		String createOrUpdateAdminName = this.getCreateOrUpdateAdminName(request);
		Msg<String>msg=bannerAppAdvService.dataInitialization(createOrUpdateAdminName);
		return msg;
	}

}
