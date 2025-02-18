package com.shq.oper.controller.auth;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.primarydb.RoleResourceMapper;
import com.shq.oper.model.domain.primarydb.Resource;
import com.shq.oper.model.domain.primarydb.Role;
import com.shq.oper.model.domain.primarydb.RoleResource;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.MenuTreeDto;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.service.primarydb.ResourceService;
import com.shq.oper.service.primarydb.RoleResourceService;
import com.shq.oper.service.primarydb.RoleService;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author ljz
 * @date 2018年4月17日
 */
@RestController
@Slf4j
@RequestMapping("/auth/role")
public class RoleController extends BaseController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleResourceService roleResourceService;
	@Autowired
	private RoleResourceMapper roleResourceMapper;
	@Autowired
	private ResourceService resourceService;

	@Autowired
	SystemProperties systemProperties;

	/**
	 * 角色页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}

	/**
	 * 添加角色
	 * 
	 * @param request
	 * @param role
	 *            角色数据
	 * @return
	 */
	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, Role role) {
		LocalDateTime dtNow = LocalDateTime.now();
		role.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		role.setUpdateTime(dtNow);
		if (ObjectUtils.isEmpty(role.getId())) {
			role.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			role.setCreateTime(dtNow);
			roleService.insert(role);
		} else {
			roleService.update(role);
		}
		return Msg.ok("保存成功");
	}

	/**
	 * 根据主键禁用单条role数据
	 * 
	 * @param request
	 * @param role
	 * @return
	 */
	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, Role role) {
		LocalDateTime dtNow = LocalDateTime.now();
		role.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		role.setUpdateTime(dtNow);
		role.setIsDisabled(true);
		roleService.disabledById(role);
		return Msg.ok("禁用成功");
	}
	@RequestMapping("/enable")
	public Msg<String> enableById(HttpServletRequest request, Role role) {
		LocalDateTime dtNow = LocalDateTime.now();
		role.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		role.setUpdateTime(dtNow);
		roleService.enableById(role);
		return Msg.ok("启用成功");
	}
	/**
	 * 搜索
	 * 
	 * @param entity
	 *            查询的条件
	 * @param page
	 *            当前页
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Role> list(Role entity, PageDto page) {
		PageInfo<Role> entitys = roleService.selectPageAndCount(entity, page.getPage(), page.getLimit());
		return Data.ok(entitys);
	}
	
	/**
	 * 角色授权资源页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/grant")
	public ModelAndView grant(HttpServletRequest request,Role entity) {
		Role bean = roleService.selectByPk(entity.getId());
		request.setAttribute("entity", bean);
		Resource resource = new Resource();
		resource.setIsDisabled(false);
		List<Resource> listAllResources = resourceService.select(resource);
		List<Long> listRolesMenuIds = roleResourceMapper.selectByRoleId(bean.getId());
		List<MenuTreeDto> roleMenuList = MenuTreeDto.toListMenuTree(listAllResources, listRolesMenuIds);
		request.setAttribute("roleMenuList", JsonUtils.toDefaultJson(roleMenuList));
		return toPage(request);
	}
	
	@RequestMapping("/roleDetails")
	public ModelAndView roleDetails(HttpServletRequest request,Role role) throws Exception {
		Role roleDetails = roleService.selectOne(role);
		request.setAttribute("roleDetails", roleDetails);
		return toPage(request);
	}
	
	/**
	 * 角色页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/grantResource")
	public Msg<String> grantResource(HttpServletRequest request,RoleResource entity) {
		LocalDateTime dtNow = LocalDateTime.now();
		entity.setCreateTime(dtNow);
		entity.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
		Msg<String> msg = roleResourceService.saveRoleAndRecource(entity);
		return msg;
	}


}