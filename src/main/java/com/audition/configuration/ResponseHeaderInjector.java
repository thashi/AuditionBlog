package com.audition.configuration;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ResponseHeaderInjector implements HandlerInterceptor {

    // TODO Inject openTelemetry trace and span Ids in the response headers.

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
        final Object handler,
        final Exception ex) {
        final Span currentSpan = Span.current();
        final SpanContext spanContext = currentSpan.getSpanContext();

        if (spanContext.isValid()) {
            response.setHeader("X-Trace-Id", spanContext.getTraceIdAsHexString());
            response.setHeader("X-Span-Id", spanContext.getSpanIdAsHexString());
        }
    }
}
