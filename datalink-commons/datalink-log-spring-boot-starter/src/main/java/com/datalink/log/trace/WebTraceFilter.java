package com.datalink.log.trace;

import com.datalink.log.properties.TraceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * WebTraceFilter
 * web过滤器，生成日志链路追踪id，并赋值MDC
 *
 * @author wenmo
 * @since 2021/5/11
 */
@ConditionalOnClass(value = {HttpServletRequest.class, OncePerRequestFilter.class})
@Order(value = MDCTraceUtils.FILTER_ORDER)
public class WebTraceFilter extends OncePerRequestFilter {
    @Resource
    private TraceProperties traceProperties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !traceProperties.getEnable();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            String traceId = request.getHeader(MDCTraceUtils.TRACE_ID_HEADER);
            if (StringUtils.isEmpty(traceId)) {
                MDCTraceUtils.addTraceId();
            } else {
                MDCTraceUtils.putTraceId(traceId);
            }
            filterChain.doFilter(request, response);
        } finally {
            MDCTraceUtils.removeTraceId();
        }
    }
}
