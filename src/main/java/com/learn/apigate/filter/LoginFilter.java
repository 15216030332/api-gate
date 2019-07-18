package com.learn.apigate.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录过滤
 */
@Component
public class LoginFilter extends ZuulFilter {

    /**
     * 拦截类型 前置后置等
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 执行顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 4;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String uri = request.getRequestURI();
        //拦截订单服务
//        if(isCoverBy("apigateway/order/",uri)){
//            return true;
//        }
        //StringBuffer url = request.getRequestURL();//获取去全路径，包括ip、端口
        return false;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)){
            token = request.getParameter("token");
        }

        if (StringUtils.isEmpty(token)){
            //设置成false表示不会继续走下去
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        System.out.println("被拦截了");
        return null;
    }

    private boolean isCoverBy(String a ,String b){
        if (!StringUtils.isEmpty(b)){
            return b.indexOf(a) > 0 ? true:false;
        }
        return false;
    }
}
