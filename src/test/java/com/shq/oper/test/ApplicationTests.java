package com.shq.oper.test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.primarydb.AdminMapper;
import com.shq.oper.model.domain.primarydb.Admin;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.req.ReqActivityListDataDto;
import com.shq.oper.model.dto.api.req.ReqUserReciveCouponsApiDataDto;
import com.shq.oper.model.dto.api.res.ResActivityListDataDto;
import com.shq.oper.service.primarydb.ActivityService;
import com.shq.oper.service.primarydb.AdminService;
import com.shq.oper.service.primarydb.CouponsUserService;
import com.shq.oper.service.primarydb.RedisService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ApplicationTests {

	
	@Resource
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private CouponsUserService couponsUserService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private AdminMapper adminMapper;
	@Autowired
	private AdminService adminService;

	@Autowired
	private ActivityService activityService;

	@Test
	public void testSetAndGet() {
		String key = "Redis-Key_中文";
		redisTemplate.opsForValue().set(key, "Redis-Value-中文");
		ValueOperations vos = redisTemplate.opsForValue();
		
		log.info("=========================" + vos.get(key));
	}

	@Test
	public void testRedisRerviceSet() {
		// System.out.println(JsonUtils.toDefaultJson(redisService.getAllKeys()));
		Admin admin = (Admin) adminMapper.selectByPrimaryKey(1L);
		System.out.println("--------" + JsonUtils.toDefaultJson(admin));
		String key = "adminredis-ljz";
		redisService.set(key, admin, 30L);// 30秒过期 admin 失败
		Admin adminTmp = (Admin) redisService.get(key);
		System.out.println("-------objobjobj-----------------" + adminTmp);
	}

	@Test
	public void testOperSysAPI() {

		String url = "http://192.168.0.57:8080/operSys/api/extApi";
		log.debug("====url====" + url);
		String code = "1002";
		
		Map<String,Object> dataMap = getMapByCode(code);
		
		Map<String, Object> requestDataMap = new HashMap<>();
		requestDataMap.put(code, dataMap);
		
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("code", code);
		paraMap.put("source", "101");
		paraMap.put("deviceType", "H5");
		paraMap.put("recTime", DateUtils.to(LocalDateTime.now()));
		paraMap.put("requestData",requestDataMap );
		

		log.debug("===requestDataMap====" + JsonUtils.toDefaultJson(requestDataMap));
		log.debug("===paraMap====" + JsonUtils.toDefaultJson(paraMap));
		
		String resultJson = HttpClientUtil.doPost(url, paraMap);
		log.debug("===resultJson====" + resultJson);

	}

	private Map<String,Object> getMapByCode(String code) {
		Map<String,Object> map = new HashMap<>();
		if("1002".equals(code)) {
			map.put("userSeq", "user0001");
			map.put("couponsStatus", "");	//(优惠券状态可为空1未使用,2锁定中,3已使用,4已过期[Integer])
			map.put("page", 1);
			map.put("limit", 10);
		}
		return map;
	}

	@Test
	public void testRedisDelete() {
		System.out.println("=====================");
		long num = 0L;
		String userSeq = "user0001";
		// 清除 CouponsUser--userSeq-- 的缓存
		String cacheNames = "CouponsUser--userSeq--" + userSeq + "*";
		redisService.removeLikes(cacheNames);
//		redisService.removeBlear(cacheNames);
		String zSetKey = "OPERSYS_MINUTE_20~keys";

		System.out.println(num);
		System.out.println("=====================");
	}

	@Test
	public void testCacheAdmin() {
		System.out.println("========111111111111111111111");
		Admin admin = adminService.queryAdminByLogin("admin"); // 保存key==queryAdminByLogin--ljz
		System.out.println("========" + admin);

	}

	
	@Test
	public void testActivityService() {
		System.out.println("========111111111111111111111");
		ReqActivityListDataDto apiDto = new ReqActivityListDataDto();
		PageDto pageDto = new PageDto(1, 10);
		Page<ResActivityListDataDto> list = activityService.queryListByApi(apiDto, pageDto);
		
		log.debug("========" + JsonUtils.toDefaultJson( list ) );
		log.debug("========" + JsonUtils.toDefaultJson( redisService.getAllKeys()));
		
	}
	
	
	
	private  int y = 1;
	@Test
	public void testRedisLock() throws InterruptedException {
		System.out.println("========111111111111111111111");
		
		
//		Msg<String> msgT = couponsUserService.receiveCoupons(dto);
//        System.out.println(y+"==="+DateUtils.now()+"-------"+msgT+"=====");
//        msgT = couponsUserService.receiveCoupons(dto);
//        System.out.println(y+"==="+DateUtils.now()+"-------"+msgT+"=====");
		Admin amd = new Admin();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {  
                @Override  
                public void run() {  
                    try {  
                        TimeUnit.SECONDS.sleep(1);  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }
                    for(int k = 0; k< 10;k++) {
                    	amd.setId(Long.valueOf(k));
                    	ReqUserReciveCouponsApiDataDto dto = new ReqUserReciveCouponsApiDataDto("520GiftPackeg","1000"+amd.getId(),"phone",118L);
                        Msg<String> msgT = couponsUserService.receiveCoupons(dto);
                        System.out.println(y+"==="+DateUtils.now()+"-------"+msgT+"=====");
                    }
                    y++;
                }  
            }).start();  
        }  
        TimeUnit.SECONDS.sleep(20);  
        
        System.out.println("################end");
		
	}
	
	
	

}