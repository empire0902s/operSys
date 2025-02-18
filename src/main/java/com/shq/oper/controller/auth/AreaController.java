package com.shq.oper.controller.auth;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.primarydb.AreaMapper;
import com.shq.oper.model.domain.primarydb.Area;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.service.primarydb.AreaService;

import lombok.extern.slf4j.Slf4j;
@RestController
@Slf4j
@RequestMapping("/auth/area")
public class AreaController extends BaseController{
	
	@Autowired private AreaService areaService;
	@Autowired private AreaMapper areaMapper;
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request){
		return toPage(request);
	}
    
	/**
	 * 添加/修改
	 */
	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, Area area) {
		try{		
//			Area areas=areaMapper.queryWhinChilds(area);
			if (ObjectUtils.isEmpty(area.getId())) {						
				areaService.insert(area);
			} else {				
//				if (ObjectUtils.isEmpty(areas.getParentCode())){              
//                    area.setParentCode("100000");
//                    area.setParentName("中国");
//                }
				areaService.update(area);
			}
			return Msg.ok("保存成功");
		}catch (Exception e){
            e.printStackTrace();
            return Msg.error("保存失败");
        }
	}

	@RequestMapping("/areaDetails")
	public ModelAndView adminDetails(HttpServletRequest request, Area area) throws Exception {
		Area areaDetails = areaService.selectOne(area);
		request.setAttribute("areaDetails", areaDetails);
		return toPage(request);
	}
	
	/**
	 * 禁用单条数据
	 */	
	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, Area area) {		
		Area selectOne = areaService.selectOne(area);
		selectOne.setIsDisabled(true);
		areaService.update(selectOne);		
		return Msg.ok("保存成功");
	}
	
	/**
	 * 搜索查询的条件当前页
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Data<Area> list(Area area, PageDto pageDto){
		/*PageInfo<Area> entitys = areaService.selectPageAndCount(area, page.getPage(), page.getLimit());*/
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<Area> datas = areaMapper.selectTOAreaAll(area);
    	return Data.ok(datas);
    }
	
	@GetMapping(value = "/selectAreaValue")
	public List<SelectValueData<Long>> areaList(Area area, boolean isCode) {
		Page<Area> datas = areaMapper.selectTOAreaAll(area);
		List<SelectValueData<Long>> list = 
				datas.stream()
				.map(d -> new SelectValueData<Long>(
						/*d.getParentName()!= null ? d.getName()+"["+d.getParentName()+"]" : d.getName()+"["+d.getName()+"]",*/
						d.getParentName()!= null ? d.getParentName() : d.getName(),d.getId()
						)
					).collect(Collectors.toList()); 
		return list;
	}
	
	
}
