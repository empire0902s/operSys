package com.shq.oper.controller.publicenum;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.model.domain.primarydb.PubliceNum;
import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduce;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.PubliceNumService;

import lombok.extern.slf4j.Slf4j;

/**
 *@author ljz
 *@date 2018/11/20 14:40
 */
@RestController
@Slf4j
@RequestMapping("/publicenum/publicenum")
public class PubliceNumController extends BaseController {

	
    
	@Autowired
    private PubliceNumService publiceNumService;


    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        return toPage(request);
    }

	/**
	 * 新增/修改公众号数据
	 * @param request
	 * @param resource
	 * @return
	 */
    @RequestMapping(value = "/save", method = {RequestMethod.GET, RequestMethod.POST})
    public Msg<String> save(HttpServletRequest request, PubliceNum publiceNum){
        try{
        	LocalDateTime dtNow = LocalDateTime.now();
        	publiceNum.setUpdateTime(dtNow);
        	publiceNum.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
            if (ObjectUtils.isEmpty(publiceNum.getId())){
            	publiceNum.setCreateTime(dtNow);
            	publiceNum.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
            	publiceNumService.insert(publiceNum);
            }else {
            	publiceNumService.update(publiceNum);
            }
            return Msg.ok("保存成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("保存失败");
        }
    }

    @RequestMapping(value = "/details", method = {RequestMethod.GET})
	public ModelAndView resourceDetails(HttpServletRequest request,long id) throws Exception {
    	PubliceNum tmpPubliceNum=new PubliceNum();
    	tmpPubliceNum.setId(id);
    	PubliceNum publiceNumDetails = publiceNumService.selectOne(tmpPubliceNum);
		request.setAttribute("publiceNumDetails", publiceNumDetails);
		return toPage(request);
	}
    

    /**
     * 禁用数据
     * @param request
     * @param deductibleIntroduce
     * @return
     */
    @RequestMapping(value = "/disabled", method = {RequestMethod.GET, RequestMethod.POST})
    public Msg<String> disabled(HttpServletRequest request, PubliceNum publiceNum) {

        LocalDateTime dtNow = LocalDateTime.now();
        publiceNum.setUpdateTime(dtNow);
        publiceNum.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
        publiceNumService.disabledById(publiceNum);
        return Msg.ok("保存成功");
    }
    
    
    /**
     * 启用数据，启用当前数据
     * @param request
     * @param deductibleIntroduce
     * @return
     */
	@RequestMapping("/enableBy")
	public Msg<String> enableById(HttpServletRequest request,PubliceNum publiceNum) {
        LocalDateTime dtNow = LocalDateTime.now();
        publiceNum.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		publiceNum.setUpdateTime(dtNow);
       return publiceNumService.enableAndDisbled(publiceNum);
		
//		LocalDateTime dtNow = LocalDateTime.now();
//		publiceNum.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
//		publiceNum.setUpdateTime(dtNow);
//
////        void updateAllToDisabled();
//		publiceNumService.enableById(publiceNum);
//		return Msg.ok("保存成功");
	}


    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Data<PubliceNum> list(PubliceNum entity, PageDto page) {
    	PageHelper.startPage(page.getPage(), page.getLimit());
    	Page<PubliceNum> entitys=publiceNumService.queryAllPubliceNum(entity);
        return Data.ok(entitys);
    }
    
	

}
