package com.shq.oper.controller.auth;

import java.time.LocalDateTime;
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
import com.shq.oper.mapper.primarydb.ResourceMaper;
import com.shq.oper.model.domain.primarydb.Resource;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.service.primarydb.ResourceService;
import com.shq.oper.util.Constant;

import lombok.extern.slf4j.Slf4j;

/**
 *@author ltg
 *@date 2018/4/18 11:40
 */
@RestController
@Slf4j
@RequestMapping("/auth/resource")
public class ResourceController extends BaseController {

	
    
	@Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourceMaper resourceMapper;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        return toPage(request);
    }

    /**
     *@author ltg
     *@date 2018/4/18 11:39
     * @params:[request, resource]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping(value = "/save", method = {RequestMethod.GET, RequestMethod.POST})
    public Msg<String> save(HttpServletRequest request, Resource resource){
        try{
            LocalDateTime dtNow = LocalDateTime.now();
            if (ObjectUtils.isEmpty(resource.getId())){
                resource.setCreateTime(dtNow);
                resource.setUpdateTime(dtNow);
                resource.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
                resource.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));

                if (!ObjectUtils.isEmpty(resource.getParentId())){
                    Page<String> remartList=resourceMapper.queryWhinChilds(resource);
                    String remark= resourceService.getSortStr(remartList,resource.getParentId());

                    resource.setRemark(remark);
                }
                resource.setSort((short)1000);
                resource.setSystemCode(Constant.OPER_SYSTEM_CODE_VALUE);
                resourceService.insert(resource);

            }else {
                resource.setUpdateTime(dtNow);
                resource.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
                if (!ObjectUtils.isEmpty(resource.getParentId())){
                    Page<String> remarkList=resourceMapper.queryWhinChilds(resource);
                    String remark= resourceService.getSortStr(remarkList,resource.getParentId());
                    resource.setRemark(remark);
                }else {
                    resource.setRemark(" ");
                }
                resourceService.update(resource);
            }
            return Msg.ok("保存成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("保存失败");
        }
    }

    @RequestMapping(value = "/details", method = {RequestMethod.GET})
	public ModelAndView resourceDetails(HttpServletRequest request,long id) throws Exception {
    	Resource resourceDetails = resourceMapper.queryResourceDetailById(id);
		request.setAttribute("resourceDetails", resourceDetails);
		return toPage(request);
	}
    
    /**
     *@author ltg
     *@date 2018/4/18 11:38
     * @params:[request, resource]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping(value = "/disabled", method = {RequestMethod.GET, RequestMethod.POST})
    public Msg<String> disabled(HttpServletRequest request, Resource resource) {

        LocalDateTime dtNow = LocalDateTime.now();
        resource.setUpdateTime(dtNow);
        resource.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
        resourceService.disabledById(resource);
        return Msg.ok("保存成功");
    }
	@RequestMapping("/enable")
	public Msg<String> enableById(HttpServletRequest request, Resource resource) {
		LocalDateTime dtNow = LocalDateTime.now();
		resource.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		resource.setUpdateTime(dtNow);
		resourceService.enableById(resource);
		return Msg.ok("保存成功");
	}
    /**
     *@author ltg
     *@date 2018/4/18 11:39
     * @params:[resource]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public Msg<String> delete(Resource resource){
        resourceService.delete(resource);
        return Msg.ok("删除成功");

    }


    /**
     *@author ltg
     *@date 2018/4/18 11:39
     * @params:[entity, page]
     * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.Resource>
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Data<Resource> list(Resource entity, PageDto page) {
    	com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
    	Page<Resource> entitys = resourceMapper.queryWithParent(entity);
        return Data.ok(entitys);
    }
    
    
	@GetMapping(value = "/selectValue")
	public List<SelectValueData<Long>> dict(Resource entity, boolean isCode) {
		Page<Resource> datas = resourceMapper.queryWithParent(entity);
		List<SelectValueData<Long>> list = 
				datas.stream()
				.map(d -> new SelectValueData<Long>(
						d.getParentName()!= null ? d.getName()+"["+d.getParentName()+"]" : d.getName(),
						d.getId()
						)
					).collect(Collectors.toList()); 
		return list;
	}
	

}
