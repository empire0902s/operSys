package com.shq.oper.service.impl.primarydb;

import java.time.LocalDateTime;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shq.oper.dao.GuestBrowsingLogTemplate;
import com.shq.oper.dao.MongoDao;
import com.shq.oper.model.domain.mongo.GuestBrowsingLog;
import com.shq.oper.service.primarydb.GuestBrowsingLogService;
import com.shq.oper.util.MemoryViewUtil;

import lombok.extern.slf4j.Slf4j;

@Service("guestBrowsingLogService")
@Slf4j
public class GuestBrowsingLogServiceImpl implements GuestBrowsingLogService {

	private static java.util.concurrent.ExecutorService threadPool_Fixed = java.util.concurrent.Executors.newFixedThreadPool(10);
	
	@Autowired
	private MongoDao<GuestBrowsingLog> mongoDao;
	
	@Autowired
	private GuestBrowsingLogTemplate guestBrowsingLogTemplate;

	@Override
	public void save(Map map, String remortIP) {
		
		threadPool_Fixed.submit(new Runnable() {
			@Override
			public void run() {
				String threadName = Thread.currentThread().getName();
				if (!(StringUtils.isEmpty(map) && StringUtils.isEmpty(remortIP))) {
					GuestBrowsingLog tmpGuestBrowsingLog = new GuestBrowsingLog();
					tmpGuestBrowsingLog.setRemortIP(remortIP);
					tmpGuestBrowsingLog.setCreateTime(LocalDateTime.now());
					tmpGuestBrowsingLog.setCurrentUrl(map.get("f").toString());
					tmpGuestBrowsingLog.setOperateMethod(map.get("e").toString());
					tmpGuestBrowsingLog.setBrowserKernel(map.get("ay").toString());
					tmpGuestBrowsingLog.setCookieVisitCount(Integer.parseInt(map.get("ao").toString()));
					tmpGuestBrowsingLog.setCookieVisitorUuid(map.get("b").toString());
					tmpGuestBrowsingLog.setCookieVisitorSessionid(map.get("c").toString());
					tmpGuestBrowsingLog.setLanguage(map.get("ba").toString());
					tmpGuestBrowsingLog.setMobileType(map.get("l").toString());
					tmpGuestBrowsingLog.setOperatingSystemType(map.get("o").toString());
					tmpGuestBrowsingLog.setOperatingSystemVersion(map.get("p").toString());
					tmpGuestBrowsingLog.setResolvingPower(map.get("s").toString());
					tmpGuestBrowsingLog.setUserAgent(map.get("r").toString());
					tmpGuestBrowsingLog.setCookieVisitorIdValuesCurrentVisitTs(map.get("j").toString());
					tmpGuestBrowsingLog.setConfigTrackerSiteId(map.get("a").toString());
					tmpGuestBrowsingLog.setCity(map.get("aw").toString());
					tmpGuestBrowsingLog.setProvince(map.get("av").toString());
					tmpGuestBrowsingLog.setUserid(map.get("ap").toString());
					tmpGuestBrowsingLog.setGender(map.get("ar").toString());
					tmpGuestBrowsingLog.setAge(map.get("as").toString());
					tmpGuestBrowsingLog.setRole(map.get("at").toString());
					tmpGuestBrowsingLog.setLevel(map.get("au").toString());
					tmpGuestBrowsingLog.setConfigReferrerUrl(map.get("bd").toString());
					tmpGuestBrowsingLog.setEngine(map.get("ax").toString());
					tmpGuestBrowsingLog.setWinWidth(map.get("bf").toString());
					tmpGuestBrowsingLog.setWinHeight(map.get("bg").toString());
					mongoDao.insert(tmpGuestBrowsingLog);
					log.debug(threadName+"======="+MemoryViewUtil.showMemoryStr());

				}
				
			}
		});
		
		

	}

	
	@PreDestroy
	public void destory(){
		log.info("----------------shutdown threadPool_Fixed");
		threadPool_Fixed.shutdownNow();
	}
}
