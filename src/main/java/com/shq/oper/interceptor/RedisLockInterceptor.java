package com.shq.oper.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shq.oper.lock.RedisLock;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.service.primarydb.RedisService;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于注解。redis锁拦截器实现
 */
@Aspect
@Component
@Slf4j
public class RedisLockInterceptor {

	@Autowired
	private RedisService redisService;
	
    private static final Integer MAX_RETRY_COUNT = 10;
    private static final String LOCK_PRE_FIX = "lockPreFix";
    private static final String TIME_OUT = "timeOut";


    @Pointcut("@annotation(com.shq.oper.lock.RedisLock)")
    public void redisLockAspect(){}

    @Around("redisLockAspect()")
    public Msg<String> lockAroundAction(ProceedingJoinPoint proceeding){
        //获取redis锁
    	Msg<String> getLockResult = this.getLock(proceeding,0,System.currentTimeMillis());
		return getLockResult;
    }

    /**
     * 获取锁
     * @param proceeding
     * @return
     */
    @SuppressWarnings("unchecked")
	private Msg<String> getLock(ProceedingJoinPoint proceeding,int count,long currentTime){
        //获取注解中的参数
    	Map<String, Object> annotationArgs = this.getAnnotationArgs(proceeding);
        String lockPrefix = (String) annotationArgs.get(LOCK_PRE_FIX);
        long timeOut = (long) annotationArgs.get(TIME_OUT);
        String key = this.getFirstArg(proceeding);
        if(StringUtils.isEmpty(lockPrefix) || StringUtils.isEmpty(key)){
            return Msg.error("锁前缀或业务参数不能为空");
        }
        
        String lockName = lockPrefix+"_"+key;
        long expireTimeValue = currentTime + (timeOut*1000) + 1;	//设置失效时间Value 5s
        
        Msg<String> msg = Msg.error("锁执行失败");
        try {
        	if(redisService.setIfAbsent(lockName, String.valueOf(expireTimeValue))) {
        		log.debug("---ProceedingJoinPoint----ent------{}",lockName);
        		redisService.setExpireTime(lockName, 10L);//设置自动过期时间10秒
        		msg = (Msg<String>) proceeding.proceed();	//
        		log.debug("firsti==-ProceedingJoinPoint----ent----return---{}",msg.toString());
        		if(msg.isOk()) {
        			log.info("firsti==-ProceedingJoinPoint-----remove");
        			redisService.remove(lockName);
        		}
        	}else {
        		count++;
        		Object objValue = redisService.get(lockName);
        		//超时
        		if(objValue != null && System.currentTimeMillis() > Long.parseLong(objValue.toString()) ) {
        			log.debug("---ProceedingJoinPoint-#remove(lockName)--[{}]---skip---"+count);
        			//强制删除锁，并尝试再次获取锁
        			redisService.remove(lockName);
        		}
        		if(count<MAX_RETRY_COUNT){
        			log.debug("---ProceedingJoinPoint-count<MAX_RETRY_COUNT--[{}]---skip---"+count);
        			return getLock(proceeding,count,currentTime);
        		}else {
              		return Msg.error("正在处理,请稍候...");
              	}
        	}
		} catch (Throwable e) {
			log.error("proceeding----error---",e);
			msg.setMsg(e.getLocalizedMessage());
		}
        return msg;
    }

    /**
     * 获取锁参数
     * @param proceeding
     * @return
     */
    @SuppressWarnings("rawtypes")
	private Map<String, Object> getAnnotationArgs(ProceedingJoinPoint proceeding){
        Class target = proceeding.getTarget().getClass();
        Method[] methods = target.getMethods();
        String methodName = proceeding.getSignature().getName();
        for (Method method : methods) {
            if(method.getName().equals(methodName)){
                Map<String, Object> result = new HashMap<String, Object>();
                RedisLock redisLock = method.getAnnotation(RedisLock.class);
                result.put(LOCK_PRE_FIX,redisLock.lockPrefix());
                result.put(TIME_OUT, redisLock.timeUnit().toSeconds(redisLock.timeOut()));
                return result;
            }
        }
        return null;
    }

    /**
     * 获取参数为锁的业务参数
     * @param proceeding
     * @return
     */
    public String getFirstArg(ProceedingJoinPoint proceeding){
        Object[] args = proceeding.getArgs();
        if(args != null && args.length>0){
        	StringBuffer str = new StringBuffer();
            for (Object object : args) {
                str.append((object).toString());
            }
            return str.toString();
        }
        return null;
    }

}
