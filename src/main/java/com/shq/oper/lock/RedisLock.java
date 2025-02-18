package com.shq.oper.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * redis锁注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RedisLock {
	
	/**注解前缀**/
    String lockPrefix() default "";
    
    long timeOut() default 4;
    
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}