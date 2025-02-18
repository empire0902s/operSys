package com.shq.oper.controller.deductible;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.shq.oper.mapper.primarydb.DeductibleIntroduceDetailMapper;
import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduceDetail;
import com.shq.oper.service.primarydb.DeductibleIntroduceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.model.domain.primarydb.Resource;
import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduce;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.service.primarydb.DeductibleIntroduceService;
import com.shq.oper.util.Constant;

import lombok.extern.slf4j.Slf4j;

/**
 *@author ltg
 *@date 2018/4/18 11:40
 */
@RestController
@Slf4j
@RequestMapping("/deductible/introduce")
public class DeductibleIntroduceController extends BaseController {

	
    
	@Autowired
    private DeductibleIntroduceService deductibleIntroduceService;

	@Autowired
	private DeductibleIntroduceDetailService deductibleIntroduceDetailService;

    @Autowired
    private DeductibleIntroduceDetailMapper deductibleIntroduceDetailMapper;


    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        return toPage(request);
    }


    @RequestMapping("detailsSetting")
    public ModelAndView detailsSetting(HttpServletRequest request) {
        return toPage(request);
    }

	/**
	 * 新增/修改抵扣券介绍
	 * @param request
	 * @param resource
	 * @return
	 */
    @RequestMapping(value = "/save", method = {RequestMethod.GET, RequestMethod.POST})
    public Msg<String> save(HttpServletRequest request, DeductibleIntroduce deductibleIntroduce){
        try{
        	LocalDateTime dtNow = LocalDateTime.now();
        	deductibleIntroduce.setUpdateTime(dtNow);
        	deductibleIntroduce.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
            if (ObjectUtils.isEmpty(deductibleIntroduce.getId())){
            	deductibleIntroduce.setCreateTime(dtNow);
            	deductibleIntroduce.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
            	deductibleIntroduceService.insert(deductibleIntroduce);
            }else {
            	deductibleIntroduceService.update(deductibleIntroduce);
            }
            return Msg.ok("保存成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("保存失败");
        }
    }

    @RequestMapping(value = "/saveDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public Msg<String> saveDetail(HttpServletRequest request, DeductibleIntroduceDetail deductibleIntroduceDetail){
        try{
            LocalDateTime dtNow = LocalDateTime.now();
            deductibleIntroduceDetail.setUpdateTime(dtNow);
            deductibleIntroduceDetail.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
            if (ObjectUtils.isEmpty(deductibleIntroduceDetail.getId())){
                deductibleIntroduceDetail.setCreateTime(dtNow);
                deductibleIntroduceDetail.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
                deductibleIntroduceDetailService.insert(deductibleIntroduceDetail);
            }else {
                deductibleIntroduceDetailService.update(deductibleIntroduceDetail);
            }
            return Msg.ok("保存成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("保存失败");
        }
    }


    @RequestMapping(value = "/detailList", method = {RequestMethod.GET, RequestMethod.POST})
    public Data<DeductibleIntroduceDetail> detailList(Long introduceId, PageDto page) {
        DeductibleIntroduceDetail entity=new DeductibleIntroduceDetail();
        entity.setIntroduceId(introduceId);
        PageHelper.startPage(page.getPage(), page.getLimit());
        Page<DeductibleIntroduceDetail> entitys=deductibleIntroduceDetailMapper.selectDeductibleDetailList(introduceId);

        return Data.ok(entitys);
    }

    @RequestMapping(value = "/delateDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public Msg<String> delateDetail(HttpServletRequest request, Long detailId){
        try{
            if (null==detailId){
                return Msg.error("请输入id");
            }
            DeductibleIntroduceDetail deductibleIntroduceDetail=new DeductibleIntroduceDetail();
            deductibleIntroduceDetail.setId(detailId);
            deductibleIntroduceDetailMapper.delete(deductibleIntroduceDetail);

            return Msg.ok("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("删除失败");
        }
    }

    @RequestMapping(value = "/details", method = {RequestMethod.GET})
	public ModelAndView resourceDetails(HttpServletRequest request,long id) throws Exception {
    	DeductibleIntroduce tmpDeductibleIntroduce=new DeductibleIntroduce();
    	tmpDeductibleIntroduce.setId(id);
    	DeductibleIntroduce deductibleIntroduceDetails = deductibleIntroduceService.selectOne(tmpDeductibleIntroduce);
		request.setAttribute("deductibleIntroduceDetails", deductibleIntroduceDetails);
		return toPage(request);
	}
    

    /**
     * 禁用数据
     * @param request
     * @param deductibleIntroduce
     * @return
     */
    @RequestMapping(value = "/disabled", method = {RequestMethod.GET, RequestMethod.POST})
    public Msg<String> disabled(HttpServletRequest request, DeductibleIntroduce deductibleIntroduce) {

        LocalDateTime dtNow = LocalDateTime.now();
        deductibleIntroduce.setUpdateTime(dtNow);
        deductibleIntroduce.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
        deductibleIntroduceService.disabledById(deductibleIntroduce);
        return Msg.ok("保存成功");
    }
    
    
    /**
     * 启用数据，启用当前数据，然后禁用剩下的全部数据
     * @param request
     * @param deductibleIntroduce
     * @return
     */
	@RequestMapping("/enableBy")
	public Msg<String> enableById(HttpServletRequest request, DeductibleIntroduce deductibleIntroduce) {
		
		LocalDateTime dtNow = LocalDateTime.now();
		deductibleIntroduce.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		deductibleIntroduce.setUpdateTime(dtNow);
		return deductibleIntroduceService.enableOneAndDisabledOthers(deductibleIntroduce);
	}
/*    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public Msg<String> delete(Resource resource){
        resourceService.delete(resource);
        return Msg.ok("删除成功");

    }*/


    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Data<DeductibleIntroduce> list(DeductibleIntroduce entity, PageDto page) {
    	PageHelper.startPage(page.getPage(), page.getLimit());
    	Page<DeductibleIntroduce> entitys=deductibleIntroduceService.queryAllDeductibleIntroduce(entity);
    	//PageInfo<DeductibleIntroduce> entitys = deductibleIntroduceService.selectPageAndCount(entity, page.getPage(), page.getLimit());
        return Data.ok(entitys);
    }
    
	

}
