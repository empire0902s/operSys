package com.shq.oper.controller.auth;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.primarydb.AdminMapper;
import com.shq.oper.model.domain.primarydb.Admin;
import com.shq.oper.model.domain.primarydb.AdminRole;
import com.shq.oper.model.domain.primarydb.Role;
import com.shq.oper.model.dto.AdminDto;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.impl.primarydb.AdminServiceImpl;
import com.shq.oper.service.primarydb.AdminRoleService;
import com.shq.oper.service.primarydb.AdminService;
import com.shq.oper.service.primarydb.RedisService;
import com.shq.oper.service.primarydb.RoleService;
import com.shq.oper.util.CacheKeyConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录
 * 
 * @author tjun
 */
@RestController
@Slf4j
@RequestMapping("/auth/admin")
public class AdminController extends BaseController {
	@Autowired
	private AdminService adminService;
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private AdminRoleService adminRoleService;
	@Autowired
	private RedisService redisService;	
	

	@Autowired
	SystemProperties systemProperties;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}


	@RequestMapping("/checkRoles")
	public ModelAndView checkRolesPage(HttpServletRequest request, Admin admin) throws Exception {
		Admin selectOne = adminService.selectOne(admin);
		List<Admin> adminList = adminService.select(new Admin());
		List<Long> adminIds = new ArrayList<>();
		for (Admin tmpAdmin : adminList) {
			adminIds.add(tmpAdmin.getId());
		}
		List<Role> rolesOfAdmin = getRolesByAdminId(selectOne);
		List<Role> roleList = roleService.select(new Role());
		request.setAttribute("rolesOfAdmin", rolesOfAdmin);
		request.setAttribute("adminList", adminList);
		request.setAttribute("roleList", roleList);
		request.setAttribute("checkAdmin", selectOne);
		return toPage(request);
	}

	@RequestMapping("/adminDetails")
	public ModelAndView adminDetails(HttpServletRequest request, Admin admin) throws Exception {
		Admin adminDetails = adminService.selectOne(admin);
		request.setAttribute("adminDetails", adminDetails);
		return toPage(request);
	}

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, Admin admin) {
		admin.setPwd(DigestUtils.md5DigestAsHex(admin.getPwd().concat(systemProperties.getMd5Key()).getBytes()));
		LocalDateTime dtNow = LocalDateTime.now();
		admin.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		admin.setUpdateTime(dtNow);
		if (ObjectUtils.isEmpty(admin.getId())) {
			if(!StringUtils.isEmpty(admin.getName())) {
				Admin tmp=new Admin();
				tmp.setName(admin.getName());
				Admin selectOne = adminService.selectOne(tmp);
				if(!StringUtils.isEmpty(selectOne)) {
					return Msg.error("保存失败，该用户名已存在，请修改后重新保存");
				}
			}
			if(!StringUtils.isEmpty(admin.getPhone())) {
				Admin tmp=new Admin();
				tmp.setPhone(admin.getPhone());
				List<Admin> select = adminService.select(tmp);
				//Admin selectOne = adminService.selectOne(tmp);
				if(select.size()>0) {
					return Msg.error("保存失败，该手机号已被使用，请修改后重新保存");
				}
			}
			admin.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			admin.setCreateTime(dtNow);
			adminService.insert(admin);
		} else {
			if(!StringUtils.isEmpty(admin.getName())) {
				Admin tmp=new Admin();
				tmp.setName(admin.getName());
				Admin selectOne = adminService.selectOne(tmp);
				if(!StringUtils.isEmpty(selectOne)) {
					if(!selectOne.getId().equals(admin.getId())) {
						return Msg.error("保存失败，该用户名已存在，请修改后重新保存");
					}
				}
			}
			if(!StringUtils.isEmpty(admin.getPhone())) {
				Admin tmp=new Admin();
				tmp.setPhone(admin.getPhone());
				List<Admin> select = adminService.select(tmp);
				if(select.size()==1) {
					Admin adminTmp = select.get(0);
					if(!adminTmp.getId().equals(admin.getId())) {
						return Msg.error("保存失败，该手机号已被使用，请修改后重新保存");
					}
				}
				if(select.size()>=2) {
					boolean flag=false;
					for (Admin adminTmp : select) {
						if(adminTmp.getId().equals(admin.getId())) {
							flag=true;
							break;
						}
					}
					if(!flag) {
						return Msg.error("保存失败，该手机号已被使用，请修改后重新保存");
					}
				}
				
			}
			adminService.update(admin);
		}
		return Msg.ok("保存成功");
	}

	@RequestMapping("/updateInfo")
	public ModelAndView updateInfo(HttpServletRequest request) {
		AdminDto adminDto = this.getAdmin(request);
		request.setAttribute("adminDto", adminMapper.selectByPrimaryKey(adminDto.getId()));
		return toPage(request);
	}
	
	@RequestMapping("/updateInfoBySms")
	public Msg<String> updateInfoBySms(HttpServletRequest request, Admin admin) {
		LocalDateTime dtNow = LocalDateTime.now();
		
		AdminDto adminDto = this.getAdmin(request);
		if (ObjectUtils.isEmpty(adminDto.getId())) {
			return Msg.error("超市操作。请刷新,重新登录");
		} else {
			Admin updateAdmin = new Admin();
			updateAdmin.setId(adminDto.getId());
			updateAdmin.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
			updateAdmin.setUpdateTime(dtNow);
			
			if(! admin.getPwd().equals(admin.getPwdConfirm())) {
				return Msg.error("两次密码不一致,请重新输入.");
			}
			updateAdmin.setName(admin.getName());
			updateAdmin.setPwd(DigestUtils.md5DigestAsHex(admin.getPwd().concat(systemProperties.getMd5Key()).getBytes()));
			updateAdmin.setPhone(admin.getPhone());
			updateAdmin.setRealName(admin.getRealName());
			
			int num = adminService.update(updateAdmin);
			if(num != 1 ) {
				return Msg.error("保存失败。");
			}
			request.getSession().invalidate();
		}
		return Msg.ok("保存成功");
	}

	
	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, Admin admin) {
		LocalDateTime dtNow = LocalDateTime.now();
		admin.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		admin.setUpdateTime(dtNow);
		adminService.disabledById(admin);
		return Msg.ok("保存成功");
	}
	@RequestMapping("/enableBy")
	public Msg<String> enableById(HttpServletRequest request, Admin admin) {
		LocalDateTime dtNow = LocalDateTime.now();
		admin.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		admin.setUpdateTime(dtNow);
		adminService.enableById(admin);
		return Msg.ok("保存成功");
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Admin> list(HttpServletRequest request,Admin entity, PageDto page) {
		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
		if(this.getAdmin(request).getName().equals("admin")) {//隐藏admin 用户。。
			entity.setShowAdmin(true);
		}
		Page<Admin> entitys = adminMapper.queryByExcludeAdmin(entity);
		return Data.ok(entitys);
	}

	
	@RequestMapping(value = "/redisCacheList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Admin> redisCacheList(HttpServletRequest request,String cacheKey) {
		if(this.getAdmin(request).getName().equals("admin")) {//仅admin用户可以查看缓存
			return null;
		}else {
			return Data.error("查询出错.");
		}
	}
	//清除缓存
	@RequestMapping(value = "/clearRedisCache", method = { RequestMethod.GET, RequestMethod.POST })
	public Msg<String> clearRedisCache(HttpServletRequest request,String cacheKey) {
		if(this.getAdmin(request).getName().equals("admin")) {//仅admin用户可以查看缓存
			
			Msg<String> msg= SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_OPERSYS_KEY_SET);
			
			return msg;
			
		}else {
			return Msg.error("查询出错.");
		}
	}

	@RequestMapping(value = "/bindRoleToAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public Msg<String> bindRoleToAdmin(HttpServletRequest request, Admin admin) {
		LocalDateTime dtNow = LocalDateTime.now();
		if (!(admin.getRoleIds() != null && !StringUtils.isEmpty(admin.getId()))) {
			try {
				adminRoleService.deleteRelationOfadminAndRole(admin);
				return Msg.ok("授权成功");
			} catch (Exception e) {
				e.printStackTrace();
				return Msg.error("授权失败");
			}
		
		}
		// 获取该admin的中间表数据
		List<AdminRole> adminRolesByAdmin = getAdminRolesListByAdminId(admin);
		// 清空admin和role之间的关系
		if (adminRolesByAdmin == null) {
			return Msg.error("授权失败");
		}
		// 根据复选框选择的角色id查询对应的角色
		List<Role> rolesList = roleService.selectByIds(admin.getRoleIds());
		List<AdminRole> adminRoles = new ArrayList<>();
		// 遍历角色集合
		for (Role role : rolesList) {
			AdminRole tmpAdminRole = new AdminRole();
			tmpAdminRole.setAdminId(admin.getId());
			tmpAdminRole.setRoleId(role.getId());
			tmpAdminRole.setCreateTime(dtNow);
			tmpAdminRole.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			adminRoles.add(tmpAdminRole);
		}
		boolean result;
		try {
			result = adminRoleService.updateAdminAndRole(adminRoles, admin);
			if (result) {
				return Msg.ok("授权成功");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Msg.error("授权失败");
		}
		return Msg.error("授权失败");
	}
	/**
	 * 根据admin查询对应的中间表数据
	 * 
	 * @param admin
	 * @return
	 */
	public List<AdminRole> getAdminRolesListByAdminId(Admin admin) {
		if (!StringUtils.isEmpty(admin.getId())) {
			AdminRole adminRole = new AdminRole();
			adminRole.setAdminId(admin.getId());
			List<AdminRole> adminRoleListByAdmin;
			adminRoleListByAdmin = adminRoleService.select(adminRole);
			if (StringUtils.isEmpty(adminRoleListByAdmin)) {
				return null;
			}
			return adminRoleListByAdmin;
		}
		return null;
	}

	/**
	 * 根据admin获取关联的角色数据
	 * 
	 * @param admin
	 * @return
	 */
	public List<Role> getRolesByAdminId(Admin admin) {
		List<AdminRole> adminRoleListByAdmin;
		adminRoleListByAdmin = getAdminRolesListByAdminId(admin);
		if (adminRoleListByAdmin != null) {
			List<Long> roleIds = new ArrayList<>();
			for (AdminRole AdminRole : adminRoleListByAdmin) {
				roleIds.add(AdminRole.getRoleId());
			}
			if (roleIds.size() <= 0 || StringUtils.isEmpty(roleIds)) {
				return null;
			}
			List<Role> rolesList = roleService.selectByIds(roleIds);
			return rolesList;
		}
		return null;
	}

}