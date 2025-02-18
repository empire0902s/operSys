package com.shq.oper.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.shq.oper.enums.ResourceTypeEnum;
import com.shq.oper.model.domain.primarydb.Admin;
import com.shq.oper.model.domain.primarydb.Resource;
import com.shq.oper.model.dto.AdminDto;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.NavMenuDto;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.service.primarydb.AdminService;
import com.shq.oper.service.primarydb.RedisService;
import com.shq.oper.util.AES;
import com.shq.oper.util.Constant;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录
 * 
 * @author tjun
 */
@RestController
@Slf4j
public class LoginController extends BaseController {
	@Autowired
	private AdminService adminService;

	@Autowired
	private SystemProperties systemProperties;
	
    
    @Autowired
    private RedisService redisService;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		AdminDto adminDto = (AdminDto) request.getSession().getAttribute(Constant.OPER_USER);
		log.debug("====={}==>进入首页", adminDto.getName());
		return toPage(request);
	}

	@GetMapping("/login")
	public ModelAndView login(HttpServletRequest request) {
		HttpSession session = request.getSession();
		// 已登录，带上token直接跳回
		AdminDto adminDto = (AdminDto) session.getAttribute(Constant.OPER_USER);
		String ucToken = (String) session.getAttribute(Constant.OPER_TOKEN);
		if (adminDto != null) {
			return new ModelAndView(new RedirectView(request.getContextPath()+"/index"), "ucToken", ucToken);
		} else {
			return toPage(request);
		}
	}

	/**
	 * 退出登录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return new ModelAndView(new RedirectView(request.getContextPath() + "/login"));
	}

	@PostMapping("/loginAdmin")
	public Msg<String> login(HttpServletRequest request, @Valid Admin admin, BindingResult result,String verificationCode) {
		log.debug("====={}==>用户登录", admin.getName());
		if (result.hasErrors()) {
			return Msg.error(result.getFieldError().getDefaultMessage());
		}
		
		//验证码登陆
		if(!StringUtils.isEmpty(admin.getPhone())&&!StringUtils.isEmpty(verificationCode)) {
			if(StringUtils.isEmpty(redisService.get(Constant.OPERSYS_USER_LOGIN_VERIFICATION_CODE+admin.getPhone()))) {
				return Msg.error("验证码错误。");
			}
			if(!redisService.get(Constant.OPERSYS_USER_LOGIN_VERIFICATION_CODE+admin.getPhone()).equals(verificationCode)) {
				return Msg.error("验证码错误。");
			}
			List<Admin> loginAdmin = adminService.select(admin);
			if(loginAdmin.size()<=0) {
				return Msg.error("账号不存在。");
			}
			
			Admin tmpAdmin = loginAdmin.get(0);
			HttpSession session = request.getSession();
			AdminDto entityDto = adminService.queryResourceByAdmin(tmpAdmin);
			entityDto.setSessionId(session.getId());
			session.setAttribute(Constant.OPER_USER, entityDto);//

			String ucToken = AES.encryptMysql(session.getId().toString(), Constant.OPER_USER_AES_KEY);
			log.info("登录成功：{}, 生成token: {}", tmpAdmin.getName(), ucToken);
			session.setAttribute(Constant.OPER_TOKEN, ucToken);// session redisa 保存

			return Msg.ok();
		}
		
		
		// 验证用户名密码
		Admin entity = new Admin();
		entity.setName(admin.getName());
		entity.setPwd(DigestUtils.md5DigestAsHex(admin.getPwd().concat(systemProperties.getMd5Key()).getBytes()));
		entity = adminService.selectOne(entity);
		if (entity == null) {
			return Msg.error("用户名或密码不正确。");
		} else if (!entity.getName().equals(admin.getName()) || !entity.getPwd()
				.equals(DigestUtils.md5DigestAsHex(admin.getPwd().concat(systemProperties.getMd5Key()).getBytes()))) {
			return Msg.error("用户名或密码不正确。");
		} else if (entity.getIsDisabled()) {
			return Msg.error("用户已被禁用。");
		}
		HttpSession session = request.getSession();
		AdminDto entityDto = adminService.queryResourceByAdmin(entity);
		entityDto.setSessionId(session.getId());
		session.setAttribute(Constant.OPER_USER, entityDto);//

		String ucToken = AES.encryptMysql(session.getId().toString(), Constant.OPER_USER_AES_KEY);
		log.info("登录成功：{}, 生成token: {}", admin.getName(), ucToken);
		session.setAttribute(Constant.OPER_TOKEN, ucToken);// session redisa 保存

		return Msg.ok();
	}

	@ApiOperation(value = "获取左侧导航菜单")
	@RequestMapping("/navMenus")
	public List<NavMenuDto> navMenuDtos(HttpServletRequest request) {
		List<Resource> resources = getAdmin(request).getResources();
		// 排除其他系统，和非菜单资源
		Map<Long, NavMenuDto> navMenuDtoMap = new HashMap<Long, NavMenuDto>();
		resources.stream().filter(r -> Constant.OPER_SYSTEM_CODE_VALUE.equals(r.getSystemCode())
				&& ResourceTypeEnum.MENU.is(r.getType().intValue())).forEach(r -> {
					NavMenuDto navMenuDto = new NavMenuDto();
					navMenuDto.setId(r.getId());
					navMenuDto.setHref(r.getUrl());
					navMenuDto.setTitle(r.getName());
					navMenuDto.setChildren(new ArrayList<NavMenuDto>());
					navMenuDto.setIcon(ObjectUtils.getDisplayString(r.getIcon()));
					navMenuDtoMap.put(r.getId(), navMenuDto);
				});
		// 构造层级菜单，并取出顶级菜单
		List<NavMenuDto> navMenuDtos = new ArrayList<NavMenuDto>();
		resources.stream().filter(r -> navMenuDtoMap.containsKey(r.getId())).forEach(r -> {
			NavMenuDto navMenuDto = navMenuDtoMap.get(r.getId());
			NavMenuDto parentNavMenuDto = navMenuDtoMap.get(r.getParentId());
			if (parentNavMenuDto == null || StringUtils.isEmpty(r.getParentId())) {
				navMenuDtos.add(navMenuDto);
			} else {
				parentNavMenuDto.getChildren().add(navMenuDto);
			}
		});
		return navMenuDtos;
	}
}