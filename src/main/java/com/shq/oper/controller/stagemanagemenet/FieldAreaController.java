package com.shq.oper.controller.stagemanagemenet;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.primarydb.FieldAreaMapper;
import com.shq.oper.mapper.primarydb.FieldMapper;
import com.shq.oper.model.domain.primarydb.ticket.Field;
import com.shq.oper.model.domain.primarydb.ticket.FieldArea;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.FieldAreaService;
import com.shq.oper.service.primarydb.FieldService;

import lombok.extern.slf4j.Slf4j;
@RestController
@Slf4j
@RequestMapping("/stagemanagemenet/fieldarea")
public class FieldAreaController extends BaseController{

	@Autowired
	private FieldAreaService fieldAreaService;
	
	@Autowired
	private FieldService fieldService;

	@Autowired
	private FieldAreaMapper fieldAreaMapper;
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request){
		return toPage(request);
	}
    
	/**
	 * 添加/修改
	 */
	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, FieldArea entity) {
		
		if (!StringUtils.isEmpty(entity.getFieldId())){
			Field field=new Field();
			field.setId(entity.getFieldId());
			Field one = fieldService.selectOne(field);
			if(!StringUtils.isEmpty(one)) {
				if(!StringUtils.isEmpty(one.getIsDisabled())) {
					if(one.getIsDisabled()==true) {
						return Msg.error("该场次已被禁用,请刷新当前页面");
					}
				}
			}else {
				return Msg.error("该场次已不存在,请刷新当前页面");
			}
		}
		entity.setUpdateAdmin(this.getAdmin(request).getName());
		entity.setUpdateTime(LocalDateTime.now());
		if (StringUtils.isEmpty(entity.getId())){
			entity.setCreateAdmin(this.getAdmin(request).getName());
			entity.setCreateTime(LocalDateTime.now());
			fieldAreaService.insert(entity);
		}else {
			fieldAreaService.update(entity);
		}
		return Msg.ok("操作成功");
	}

	@RequestMapping("/list")
	public Data<FieldArea> save(PageDto pageDto, FieldArea entity) {
		
		PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
//		Page<FieldArea> entitys=fieldAreaService.queryFieldAreaList(entity);
//		return Data.ok(entitys);
		return null;
	}

	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, FieldArea fieldArea) {
		LocalDateTime dtNow = LocalDateTime.now();
		fieldArea.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		fieldArea.setUpdateTime(dtNow);
		fieldArea.setIsDisabled(true);

		fieldAreaMapper.disabledById(fieldArea);

		return Msg.ok("禁用成功");
	}


	@RequestMapping("/enableBy")
	public Msg<String> enableBy(HttpServletRequest request, FieldArea fieldArea) {
		LocalDateTime dtNow = LocalDateTime.now();
		fieldArea.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		fieldArea.setUpdateTime(dtNow);
		fieldArea.setIsDisabled(false);

		fieldAreaMapper.enableById(fieldArea);

		return Msg.ok("启用成功");
	}


}
