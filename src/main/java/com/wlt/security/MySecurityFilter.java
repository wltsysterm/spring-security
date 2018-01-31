package com.wlt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;

import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;


import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;
public class MySecurityFilter extends AbstractSecurityInterceptor implements Filter {
    //与applicationContext-security.xml里的myFilter的属性securityMetadataSource对应，
    //其他的两个组件，已经在AbstractSecurityInterceptor定义
    @Autowired
    private FilterInvocationSecurityMetadataSource mySecurityMetadataSource;

    @PostConstruct
    public void init(){
	System.out.println(" ---------------  MySecurityFilter init--------------- ");
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(" ---------------  doFilter--------------- ");
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(fi);
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        //fi里面有一个被拦截的url
        //里面调用MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法获取fi对应的所有权限
        //再调用MyAccessDecisionManager的decide方法来校验用户的权限是否足够
        // 通过调用父类AbstractSecurityInterceptor的beforeInvocation方法来实现
        // 值得注意的是，在beforeInvocation实现权限控制的逻辑中，如果请求的资源路径在securityMetadataSource中没有的话，
        // 那么就会默认为该资源无权限要求，只要请求就会通过。
        //首先，登陆后，每次访问资源都会被这个拦截器拦截，会执行doFilter这个方法，这个方法调用了invoke方法，其中fi断点显示是一个url（可能重写了toString方法吧，但是里面还有一些方法的），最重要的是beforeInvocation这个方法，它首先会调用MyInvocationSecurityMetadataSource类的getAttributes方法获取被拦截url所需的权限，在调用MyAccessDecisionManager类decide方法判断用户是否够权限。弄完这一切就会执行下一个拦截器。
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            //执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.mySecurityMetadataSource;
    }
}
