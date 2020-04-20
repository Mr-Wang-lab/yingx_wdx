package com.baizhi.wdx.aspect;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.Set;

@Configuration
@Aspect
public class AddCache {

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;

     //   @Around("execution(* com.baizhi.wdx.serviceImpl.*.query*(..))")
     @Around("@annotation(com.baizhi.wdx.annotation.AddCache)")
        public  Object addCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

            System.out.println("环绕的通知");

            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
            redisTemplate.setKeySerializer(stringRedisSerializer);
            redisTemplate.setHashKeySerializer(stringRedisSerializer);

            StringBuilder sb = new StringBuilder();
            //拼接key
            //选择数据类型

            //key    value  类型
            //       缓存数据
            //获取类的全限定名
            String className = proceedingJoinPoint.getTarget().getClass().getName();


           // sb.append(className);
            //获取方法名
            String methodName = proceedingJoinPoint.getSignature().getName();

            sb.append(methodName);
            //
            Object[] args = proceedingJoinPoint.getArgs();
            for (Object arg : args) {
                sb.append(arg);
            }
            String key = sb.toString();
            //取出key
          //  Boolean aBoolean = redisTemplate.hasKey(key);

           // Boolean aBoolean = stringRedisTemplate.hasKey(key);

         //   ValueOperations valueOperations = redisTemplate.opsForValue();
         Boolean aBoolean = redisTemplate.opsForHash().hasKey(className, key);

         HashOperations hashOperations = redisTemplate.opsForHash();
         Object result =null;
            //去redis 中判断key是否存在
            if(aBoolean){
                // 如果存在 到缓存中查  取出并返回
                 result = hashOperations.get(className,key);

            }else{
                //如不存在在数据库中查  并添加到缓存
                    //放行方法
             result = proceedingJoinPoint.proceed();
                hashOperations.put(className,key,result);
            }

            return  result;
        }

    //    @After("execution(* com.baizhi.wdx.serviceImpl.*.*(..)) && !execution(* com.baizhi.wdx.serviceImpl.*.query*(..))")
       @After("@annotation(com.baizhi.wdx.annotation.DelCache)")

        public void delCache(JoinPoint joinPoint){
            //com.baizhi.wdx.serviceImpl.UserServiceImplqueryByPage12

            //清空缓存
            //获取类的权限定名
            String className = joinPoint.getTarget().getClass().getName();
           //删除该类下所有的数据
           redisTemplate.delete(className);
    
     }

}
