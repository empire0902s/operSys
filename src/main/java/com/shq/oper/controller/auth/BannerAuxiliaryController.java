package com.shq.oper.controller.auth;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shq.oper.controller.BaseController;
import com.shq.oper.model.domain.primarydb.BannerAuxiliary;
import com.shq.oper.model.dto.ImageData;
import com.shq.oper.model.dto.ImageUploadResultData;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.BannerAuxiliaryService;
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
@RequestMapping("/auth/bannerAuxiliary")
public class BannerAuxiliaryController extends BaseController {

	@Autowired
	private BannerAuxiliaryService bannerAuxiliaryService;
	@Autowired
	private ShqApi ShqApi;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, BannerAuxiliary bannerAuxiliary) {
		bannerAuxiliary.setUpdateTime(LocalDateTime.now());
		bannerAuxiliary.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		if (ObjectUtils.isEmpty(bannerAuxiliary.getId())) {
			bannerAuxiliaryService.insert(bannerAuxiliary);
		} else {
			bannerAuxiliaryService.update(bannerAuxiliary);
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
}
