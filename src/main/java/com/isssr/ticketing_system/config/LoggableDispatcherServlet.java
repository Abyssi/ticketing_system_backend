package com.isssr.ticketing_system.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;

@Configuration
public class LoggableDispatcherServlet extends DispatcherServlet {

    public static final int MAX_PAYLOAD_LENGTH_TO_LOG = 5120;

    private final Log logger = LogFactory.getLog(getClass());

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new LoggableDispatcherServlet();
    }

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }

        HandlerExecutionChain handler = getHandler(request);

        try {
            super.doDispatch(request, response);
        } finally {
            log(request, response, handler);
        }
    }

    private void log(HttpServletRequest requestToCache, HttpServletResponse responseToCache, HandlerExecutionChain handler) {
        StringBuilder log = new StringBuilder();
        log.append("\nStatus: ").append(responseToCache.getStatus());
        log.append("\nHttp Method: ").append(requestToCache.getMethod());
        log.append(" Path: ").append(requestToCache.getRequestURI());
        log.append("\nClient Ip: ").append(requestToCache.getRemoteAddr());
        log.append("\nJava method: " + handler.toString());
        log.append("\nRequest body:").append(getRequestPayload(requestToCache));
        log.append("\nResponse body:").append(getResponsePayload(responseToCache));
        Timestamp end = new Timestamp(new Date().getTime());
        log.append("\n------------------End: ").append(end);
        logger.info(log);
    }

    private String getResponsePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            return _extractPayloadFromBuffer(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
        }
        return "[]";
    }


    private String getRequestPayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            return _extractPayloadFromBuffer(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
        }
        return "[]";
    }

    private String _extractPayloadFromBuffer(byte[] buf, String characterEncoding) {
        if (buf.length > 0) {
            int length = Math.min(buf.length, MAX_PAYLOAD_LENGTH_TO_LOG);
            try {
                return new String(buf, 0, length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                return "[Error extracting payload";
            }
        }
        return "";
    }
}
