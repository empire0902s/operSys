package com.shq.oper.service.impl.primarydb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.shq.oper.dao.mongo.AdminActionLogMongo;
import com.shq.oper.mapper.primarydb.ResourceMaper;
import com.shq.oper.model.domain.mongo.AdminActionLog;
import com.shq.oper.model.domain.primarydb.Admin;
import com.shq.oper.model.domain.primarydb.Resource;
import com.shq.oper.model.dto.AdminDto;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.service.primarydb.AdminService;
import com.shq.oper.service.primarydb.RedisService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("adminService")
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long> implements AdminService {

	@Autowired
	private RedisService redisService;	
	
    @Autowired
    private ResourceMaper resourceMaper;
    
    @Autowired
	private AdminActionLogMongo adminActionLogMongo;
	
    
    @Override
    public Admin queryAdminByLogin(String name) {
    	Admin entity = new Admin();
    	entity.setName(name);
		Admin cacheAdmin = this.selectOne(entity);
    	return cacheAdmin;
    }
    
    
	@Override
	public AdminDto queryResourceByAdmin(Admin admin) {
		AdminDto entityDto = new AdminDto();
		BeanUtils.copyProperties(admin, entityDto);//复制属性
		
		Resource resource = new Resource();
		resource.setAdminId(admin.getId());
		Page<Resource> resourceList = resourceMaper.queryResourceeByAdmin(resource);
		entityDto.setResources(resourceList);
		
		return entityDto;
	}


	@Override
	public Msg<String> clearCacheByConstantKey(String cacheKey) {
		if(StringUtils.isEmpty(cacheKey)) {
			return Msg.error("无效操作");
		}
		String fromMethodName = "";
		StackTraceElement[] arrStack = Thread.currentThread().getStackTrace();
		int shqClassNum = 0;
		for( StackTraceElement eleStack : arrStack ) {
			String methodName = eleStack.getMethodName();
			String className = eleStack.getClassName();
			int lineNumber = eleStack.getLineNumber();
			if(className.startsWith("com.shq") && !className.contains("$") ) {
				shqClassNum ++ ;
				if(shqClassNum == 2) {//记录调用方法的上层 方法
					fromMethodName = className +"__[" + methodName+"]__"+lineNumber;
					break;
				}
			}
		}
		
		List<String> cacheKeyList = new ArrayList<String>(); 
		cacheKeyList.add(CacheKeyConstant.BASE_OPERSYS_KEY_SET);//所有key 基础
		cacheKeyList.add(CacheKeyConstant.BASE_ACTIVITY_QUERY);
		cacheKeyList.add(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ);
		cacheKeyList.add(CacheKeyConstant.BASE_COUPONS_QUERY);
		cacheKeyList.add(CacheKeyConstant.BASE_GOODS_RULE_QUERY);

		cacheKeyList.add(CacheKeyConstant.BASE_ORDER_STATISTICS);
		cacheKeyList.add(CacheKeyConstant.BASE_SUPILER_STATISTICS);

		cacheKeyList.add(CacheKeyConstant.BASE_BANNER_APP_ADV_API);
		cacheKeyList.add(CacheKeyConstant.BASE_CLOUMN_GOODS_API);
		cacheKeyList.add(CacheKeyConstant.BASE_ORDER_EVALUATE);
		cacheKeyList.add(CacheKeyConstant.BASE_CIRCLE_LIST);
		cacheKeyList.add(CacheKeyConstant.BASE_DEDUCTIBLE_USERSEQ);

		boolean isContains = false;
		for(String keys : cacheKeyList) {
			if(cacheKey.startsWith(keys)) {
				isContains = true;
				break;
			}
		}
		if(!isContains) {
			return Msg.error(DateUtils.now()+"[清除缓存 失败]  "+cacheKey);
		}
		
		String msgStr = "[remove 出错]";
		if(CacheKeyConstant.BASE_OPERSYS_KEY_SET.equals(cacheKey)){
			cacheKeyList.forEach( key -> {
				redisService.removeLikes(key.concat("*"));
			});
			msgStr = "[清除所有缓存成功] ";
		}else {
			String key = cacheKey.concat("*");
			redisService.removeLikes(key);
			msgStr = "[清除缓存成功] "+cacheKey;
		}
		msgStr = DateUtils.now() + msgStr;
		
		AdminActionLog adminActionLog = new AdminActionLog();
		adminActionLog.setAction(fromMethodName);
		adminActionLog.setRefAction("adminService/clearCacheByConstantKey");
		adminActionLog.setAdminId(0L);
		adminActionLog.setAdminName("admin");
		adminActionLog.setCreateTime(LocalDateTime.now());
		adminActionLog.setIp("0.0.0.0");
		adminActionLog.setActionPara(msgStr+"【"+cacheKey+"】");
		adminActionLogMongo.insert(adminActionLog);
		
		log.debug("-----"+msgStr);
		return Msg.ok(msgStr);
	}
	
	
}