package com.shq.oper.controller.baidu;

import com.github.pagehelper.Page;
import com.github.pagehelper.util.StringUtil;
import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.primarydb.AreaMapper;
import com.shq.oper.mapper.primarydb.BaiduPoiMapper;
import com.shq.oper.model.domain.primarydb.Area;
import com.shq.oper.model.domain.primarydb.BaiduPoi;
import com.shq.oper.model.domain.primarydb.MessageSendDetail;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.model.dto.api.baidu.ResBaiduGeoCoderDto;
import com.shq.oper.model.dto.api.baidu.ResCreateBaiduPoiDto;
import com.shq.oper.service.primarydb.AreaService;
import com.shq.oper.service.primarydb.BaiduPoiService;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/baidu/setting")
public class BaiduPoiController extends BaseController{

	@Autowired
	private BaiduPoiMapper baiduPoiMapper;

	@Autowired
	private BaiduPoiService baiduPoiService;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request){
		return toPage(request);
	}


    @RequestMapping("/hotmap")
    public ModelAndView hotmap(HttpServletRequest request){
        return toPage(request);
    }

    /**
	 * 添加/修改
	 */
	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, BaiduPoi baiduPoi) {
		String admin=this.getAdmin(request).getName();
		LocalDateTime localDateTime=LocalDateTime.now();
		if (StringUtils.isEmpty(baiduPoi.getId())){
			baiduPoi.setCreateAdmin(admin);
			baiduPoi.setCreateTime(localDateTime);
			baiduPoi.setUpdateAdmin(admin);
			baiduPoi.setUpdateTime(localDateTime);
			return baiduPoiService.saveBaiduPoi(baiduPoi);
		}else {
			if (StringUtils.isEmpty(baiduPoi.getId())){
				return Msg.error("请输入id");
			}
			if (StringUtils.isEmpty(baiduPoi.getGeoId())){
				return Msg.error("请输入poiId");
			}
			baiduPoi.setUpdateAdmin(admin);
			return baiduPoiService.updateBaiduPoi(baiduPoi);
		}

	}


	@RequestMapping("/list")
	public Data<BaiduPoi> list(BaiduPoi entity, PageDto page) {

		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
		Page<BaiduPoi> entitys = baiduPoiMapper.queryPageSort(entity);
		return Data.ok(entitys);
	}

	/**
	 * 
	 * @param request
	 * @param baiduPoi
	 * @return
	 */
	@RequestMapping("/delete")
	public Msg<String> update(HttpServletRequest request, BaiduPoi baiduPoi) {

		String admin=this.getAdmin(request).getName();
		baiduPoi.setUpdateAdmin(admin);

		if (StringUtils.isEmpty(baiduPoi.getId())){
			return Msg.error("请输入id");
		}
		if (StringUtils.isEmpty(baiduPoi.getGeoId())){
			return Msg.error("请输入poiId");
		}
		return baiduPoiService.deleteBaiduPoi(baiduPoi);

	}

}
