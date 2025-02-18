package com.shq.oper.controller.stagemanagemenet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.shq.oper.mapper.primarydb.FieldMapper;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.domain.primarydb.ticket.Field;
import com.shq.oper.service.primarydb.FieldService;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
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
@RequestMapping("/stagemanagemenet/field")
public class FieldController extends BaseController{

	@Autowired
	private FieldService fieldService;

	@Autowired
	private FieldMapper fieldMapper;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request){
		return toPage(request);
	}
    
	/**
	 * 添加/修改
	 */
	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, Field field) {
		
		if(field.getBeginTime().isAfter(field.getEndTime())) {
			return Msg.error("开始时间必须小于结束时间");
		}
		
		String admin=this.getAdmin(request).getName();
		LocalDateTime now=LocalDateTime.now();
		if (StringUtils.isEmpty(field.getId())){
			field.setUpdateAdmin(admin);
			field.setCreateAdmin(admin);
			field.setCreateTime(now);
			field.setUpdateTime(now);
			fieldService.insert(field);
		}else {
			field.setUpdateAdmin(admin);
			field.setUpdateTime(now);
			fieldService.update(field);
		}
		return Msg.ok("操作成功");
	}

	@RequestMapping("/list")
	public Data<Field> save(PageDto pageDto, Field field) {
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<Field> fieldPage=fieldMapper.queryPageSort(field);
		return Data.ok(fieldPage);
	}

	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, Field field) {
		LocalDateTime dtNow = LocalDateTime.now();
		field.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		field.setUpdateTime(dtNow);
		field.setIsDisabled(true);

		fieldMapper.disabledById(field);

		return Msg.ok("禁用成功");
	}


	@RequestMapping("/enableBy")
	public Msg<String> enableBy(HttpServletRequest request, Field field) {
		LocalDateTime dtNow = LocalDateTime.now();
		field.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		field.setUpdateTime(dtNow);
		field.setIsDisabled(false);

		fieldMapper.enableById(field);

		return Msg.ok("启用成功");
	}
	@GetMapping(value = "/selectValue")
	public List<SelectValueData<Long>> dict(Field field, boolean isCode) {
		//过滤被禁用，进行中和已经结束的场次
		field.setIsDisabled(false);
		Short status=0;
		field.setStatus(status);
		Page<Field> fieldPage=fieldMapper.queryPageSort(field);
		List<SelectValueData<Long>> list = new ArrayList<>();
		if(!StringUtils.isEmpty(fieldPage.getResult())&&fieldPage.getResult().size()>0) {
			List<Field> result = fieldPage.getResult();
		 list = result.stream().map(d -> new SelectValueData<Long>(d.getName(), d.getId()))
					.collect(Collectors.toList());
		}
		return list;
	}
	
	@GetMapping(value = "/searchSelectValue")
	public List<SelectValueData<Long>> searchSelectValue(Field field, boolean isCode) {
		Page<Field> fieldPage=fieldMapper.queryPageSort(field);
		List<SelectValueData<Long>> list = new ArrayList<>();
		if(!StringUtils.isEmpty(fieldPage.getResult())&&fieldPage.getResult().size()>0) {
			List<Field> result = fieldPage.getResult();
		 list = result.stream().map(d -> new SelectValueData<Long>(d.getName(), d.getId()))
					.collect(Collectors.toList());
		}
		return list;
	}

}
