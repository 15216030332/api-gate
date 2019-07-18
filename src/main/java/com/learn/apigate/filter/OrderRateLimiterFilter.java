package com.learn.apigate.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/***
 * 订单服务限流
 */
@Component
public class OrderRateLimiterFilter extends ZuulFilter {

    //每秒产生100个令牌
    private static final RateLimiter limiter = RateLimiter.create(100);

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -4;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String uri = request.getRequestURI();
        //拦截订单服务
        if(isCoverBy("apigateway/order/",uri)){
            return true;
        }
        //StringBuffer url = request.getRequestURL();//获取去全路径，包括ip、端口
        return false;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext requestContext = RequestContext.getCurrentContext();
        //非阻塞获取令牌
        if (!limiter.tryAcquire()){
            //设置成false表示不会继续走下去
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }

    private boolean isCoverBy(String a ,String b){
        if (!StringUtils.isEmpty(b)){
            return b.indexOf(a) > 0 ? true:false;
        }
        return false;
    }
}
