package com.shq.oper.controller.orderingsystem;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.model.domain.primarydb.PubliceNum;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.orderingsystem.ListBusinessStoresDto;
import com.shq.oper.model.dto.api.orderingsystem.OSBrasndSquareDto;
import com.shq.oper.model.dto.api.orderingsystem.OSBrasndSquareRecomendDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.OsBrandSquareService;
import com.shq.oper.service.primarydb.PubliceNumService;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *@author ljz
 *@date 2018/11/24 14:40
 */
@RestController
@Slf4j
@RequestMapping("/orderingsystem/brandsquare")
public class OSBrandSquareController extends BaseController {

	
    
	@Autowired
    private PubliceNumService publiceNumService;
	
	@Autowired
	private OsBrandSquareService osBrandSquareService;
	
	@Autowired
	private ShqApi ShqApi;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        return toPage(request);
    }

	/**
	 * 新增/修改数据
	 * @param request
	 * @param resource
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
    @RequestMapping(value = "/save", method = {RequestMethod.GET, RequestMethod.POST})
    public Msg<String> save(HttpServletRequest request, OSBrasndSquareDto entity) throws IllegalAccessException, InvocationTargetException{
    	
    	
    	if(StringUtils.isEmpty(entity.getStoresName())) {
    		return Msg.error("品牌商名称不能为空");
    	}
    	if(StringUtils.isEmpty(entity.getStoresUrl())) {
    		return Msg.error("图片路径不能为空");
    	}
    	if(StringUtils.isEmpty(entity.getSort())) {
    		return Msg.error("排序不能为空");
    	}
    	if(StringUtils.isEmpty(entity.getContentUrl())) {
    		return Msg.error("请更换当前品牌商后重试,或联系管理员");
    	}
    	if(StringUtils.isEmpty(entity.getDealerId())) {
    		return Msg.error("dealerId不能为空");
    	}
    	Map<String,Object> resultMap=new HashMap<>();
    	if(!StringUtils.isEmpty(entity.getStoresUrl())) {
    		entity.setStoresUrl(entity.getStoresUrl().substring(entity.getStoresUrl().indexOf("/UploadFile/"),entity.getStoresUrl().length()));
    	}
    	
    	String urlParam=entity.getStoresName()+"And"+entity.getContentUrl();
    	//判断品牌商是否是真实数据
		String urlSec = ShqApi.getGetOsbrandSquareUrl()+"api/Home/GetListShopperUserName?condition=userNameAndseq&strKeyWord="+urlParam;
		String resultJsonSec =	HttpClientUtil.doGet(urlSec);
		if(StringUtils.isEmpty(resultJsonSec)) {
			return Msg.error("接口访问异常");
		}
		Map<String,Object> resultMapSec = JsonUtils.parse( resultJsonSec,HashMap.class);
		Object resultCode = resultMapSec.get("Code");
		if(resultCode==null||resultCode.toString().equals("0")) {
			return Msg.error("品牌商名称与Seq不匹配");
		}
		Object objectSec = resultMapSec.get("Resultdata");
		if(StringUtils.isEmpty(objectSec)) {
			return Msg.error("品牌商名称与Seq不匹配");
		}
		List<OSBrasndSquareRecomendDto> resultListSec = JsonUtils.parseList(JsonUtils.toDefaultJson(objectSec), OSBrasndSquareRecomendDto.class);
		if(resultListSec.size()<=0) {
			return Msg.error("品牌商名称与Seq不匹配");
		}
		String url = ShqApi.getGetOsbrandSquareUrl()+"api/Home/AddEditDeleteHomeInfo";
		List<OSBrasndSquareDto> paramList=new ArrayList<>();
		Map<String, Object> params=new HashMap<>();
		if (StringUtils.isEmpty(entity.getId())) {//新增
			entity.setId(null);
			String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(entity));
			resultMap = JsonUtils.parse( resultJson,HashMap.class);
		}else {
			OSBrasndSquareDto entityTmp=new OSBrasndSquareDto();
			BeanUtils.copyProperties(entityTmp, entity);
			String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(entityTmp));
			resultMap = JsonUtils.parse( resultJson,HashMap.class);
		}
		String object = resultMap.get("Code")+"";
		if(object.equals("200")) {
			return Msg.ok("保存成功");
		}else {
			String errorStr="";
			Map mapType = JSON.parseObject(resultMap.get("Resultdata")+"",Map.class);
			   for (Object obj : mapType.keySet()){  
				   errorStr=mapType.get(obj)+"";
		        }  
			   if(StringUtils.isEmpty(errorStr)) {
				   return Msg.error("保存失败");
			   }
			return Msg.error(errorStr);
		}
		
		
    }

    @RequestMapping(value = "/details", method = {RequestMethod.GET})
	public ModelAndView resourceDetails(HttpServletRequest request,Long id) throws Exception {
    	
    	OSBrasndSquareDto osbrasndSquareDtoDetials=new OSBrasndSquareDto();
    	List<OSBrasndSquareDto> tmptList =new ArrayList<>();
    	String url = ShqApi.getGetOsbrandSquareUrl()+"api/Home/GetHomeInfo";
		String resultJson = HttpClientUtil.doGet(url);
		//调用接口出错，返回null
		if(StringUtils.isEmpty(resultJson)) {
			request.setAttribute("osbrasndSquareDtoDetials", "");
			return toPage(request);
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		Object object = resultMap.get("Resultdata");
		ListBusinessStoresDto data = JsonUtils.parse(JsonUtils.toDefaultJson(object), ListBusinessStoresDto.class);
		tmptList = data.getListBusinessStores();
		if(tmptList.size()==0) {
			request.setAttribute("osbrasndSquareDtoDetials", "");
			return toPage(request);
		}
		for (OSBrasndSquareDto osBrasndSquareDto : tmptList) {
			if(id.equals(osBrasndSquareDto.getId())){
				osbrasndSquareDtoDetials=osBrasndSquareDto;
			}
		}
		request.setAttribute("osbrasndSquareDtoDetials", osbrasndSquareDtoDetials);
		return toPage(request);

	}
    

    /**
     * 订货系统品牌广场数据
     * @param entity
     * @param page
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Data<OSBrasndSquareDto> list(OSBrasndSquareDto entity, PageDto page) {
    	return osBrandSquareService.queryList(entity,page);
    }
	@RequestMapping("/delete")
	public Msg<String> deleteById(HttpServletRequest request, String id) {
		if(StringUtils.isEmpty(id)) {
			return Msg.error("数据有误,请稍后重试");
		}
		
		OSBrasndSquareDto entityTmp=new OSBrasndSquareDto();
		entityTmp.setId(Long.parseLong(id));
		String url = ShqApi.getGetOsbrandSquareUrl()+"api/Home/AddEditDeleteHomeInfo";
		String resultJson =	HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(entityTmp));
		if(StringUtils.isEmpty(resultJson)) {
			return Msg.error("接口调用错误");
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("Code")+"";
		if(object.equals("200")) {
			return Msg.ok("删除成功");
		}else {
			return Msg.error(resultMap.get("message")+"");
		}
	}
	
	
	@RequestMapping("/getListShopperUserNameByStoresName")
	public String getListShopperUserNameByStoresName(HttpServletRequest request, String strUserName) {
		if(StringUtils.isEmpty(strUserName)) {
			return "";
		}
		
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put("code", 0);
		dataMap.put("msg", "");
		
		OSBrasndSquareDto entityTmp=new OSBrasndSquareDto();
		String url = ShqApi.getGetOsbrandSquareUrl()+"api/Home/GetListShopperUserName?condition=userName&strKeyWord="+strUserName;
		String resultJson =	HttpClientUtil.doGet(url);
		if(StringUtils.isEmpty(resultJson)) {
			dataMap.put("data", "");
			return JsonUtils.toDefaultJson(dataMap); 
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		Object resultCode = resultMap.get("Code");
		if(resultCode.toString().equals("0")) {
			dataMap.put("data", "");
			return JsonUtils.toDefaultJson(dataMap); 
		}
		Object object = resultMap.get("Resultdata");
		if(StringUtils.isEmpty(object)) {
			dataMap.put("data", "");
			return JsonUtils.toDefaultJson(dataMap); 
		}
		List<OSBrasndSquareRecomendDto> resultList = JsonUtils.parseList(JsonUtils.toDefaultJson(object), OSBrasndSquareRecomendDto.class);
		if(resultList.size()<=0) {
			dataMap.put("data", "");
			return JsonUtils.toDefaultJson(dataMap); 
		}
		dataMap.put("count", resultList.size());
		dataMap.put("data", resultList);
		return JsonUtils.toDefaultJson(dataMap);
	}
	
	

}
