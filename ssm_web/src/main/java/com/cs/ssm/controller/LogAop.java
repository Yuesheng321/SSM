package com.cs.ssm.controller;

import com.cs.ssm.domain.SysLog;
import com.cs.ssm.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAop {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ISysLogService sysLogService;

    private Date visitTime; // 开始时间
    private Class clazz;  // 访问方法
    private Method method;  // 访问的类

    /**
     * 获取开始时间，执行的类，执行的方法
     * @param jp
     */
    @Before("execution(* com.cs.ssm.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws NoSuchMethodException {
        visitTime = new Date();
        clazz = jp.getTarget().getClass();
        String methodName = jp.getSignature().getName(); // 获取访问方法名
        Object[] args = jp.getArgs();// 获取访问方法参数
        if(args == null || args.length == 0) {
            method = clazz.getMethod(methodName);  // 只能获取无参方法
        } else {
            Class[] classArgs = new Class[args.length];
            for (int i = 0; i < args.length; ++i) {
                classArgs[i] = args[i].getClass();
            }
            clazz.getMethod(methodName, classArgs);
        }
    }

    /**
     *
     * @param jp
     */
    @After("execution(* com.cs.ssm.controller.*.*(..))")
    public void doAfter(JoinPoint jp) throws Exception {//
        long time = new Date().getTime() - visitTime.getTime();
        // 获取 url
        String url = "";
        if (clazz != null && method != null && clazz != LogAop.class) {
            RequestMapping clazzAnnotation= (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            if (clazzAnnotation != null) {
                String[] classValue = clazzAnnotation.value();
                RequestMapping methodAnnotation= (RequestMapping) method.getAnnotation(RequestMapping.class);
                if (methodAnnotation != null) {
                    String[] methodValue = methodAnnotation.value();
                    url = classValue[0] +methodValue[0];

                    // 获取访问ip
                    String ip = request.getRemoteAddr();

                    // 获取当前操作用户
                    SecurityContext context = SecurityContextHolder.getContext();
                    User user = (User) context.getAuthentication().getPrincipal();
                    String username = user.getUsername();

                    // 将日志封装到SysLog对象
                    SysLog sysLog = new SysLog();
                    sysLog.setExecutionTime(time);
                    sysLog.setIp(ip);
                    sysLog.setMethod("[类名] " + clazz.getName() + " [方法名] " + method.getName());
                    sysLog.setUrl(url);
                    sysLog.setUsername(username);
                    sysLog.setVisitTime(visitTime);

                    sysLogService.save(sysLog);
                }
            }
        }

    }
}
