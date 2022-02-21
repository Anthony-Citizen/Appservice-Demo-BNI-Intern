package com.example.appservice.filters;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
@Slf4j
@Order(1)
public class RequestResponseLogger implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper((HttpServletRequest) servletRequest);

        CustomHttpResponseWrapper responseWrapper = new CustomHttpResponseWrapper((HttpServletResponse) servletResponse);

        filterChain.doFilter(requestWrapper, responseWrapper);

        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");

        log.info("INFO {} {}", f.format(new Date()), responseWrapper.getCharacterEncoding() );
        log.info("ID : {}", getCurrentlyDateTime());

        String method = requestWrapper.getMethod();
        String uri = requestWrapper.getRequestURI();

        switch (method) {
            case "POST" :
                switch (uri) {
                    case "/user" : log.info("STEP : Adding User");
                        break;
                    default: log.info("STEP : Default");
                }
                break;
        }

        log.info("Request URL: {}", requestWrapper.getRequestURL());
        log.info("Host : {}", requestWrapper.getServerName());

//        Request Inline
//        log.info("Request Body: {}", new String(requestWrapper.getByteArray()).replaceAll("\n", " "));

//        Request Body Original
        log.info("Request Body: {}", new String(requestWrapper.getByteArray()));

        long end = System.currentTimeMillis() - start;

        log.info("Response Status : {}", responseWrapper.getStatus());
        log.info("Response Body : {}", new String(responseWrapper.getBaos().toByteArray()));
        log.info("Response Time : {}", end);
    }

    private class CustomHttpRequestWrapper extends HttpServletRequestWrapper {
        private byte[] byteArray;

        public CustomHttpRequestWrapper(HttpServletRequest request) {
            super(request);
            try {
                byteArray = IOUtils.toByteArray(request.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException("Issue while reading request stream");
            }
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new MyDelegatingServletInputStream(new ByteArrayInputStream(byteArray));
        }

        public byte[] getByteArray() {
            return byteArray;
        }
    }

    private class CustomHttpResponseWrapper extends HttpServletResponseWrapper {
        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        private PrintStream printStream = new PrintStream(baos);

        public ByteArrayOutputStream getBaos() {
            return baos;
        }

        public CustomHttpResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new MyDelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), printStream));
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(new TeeOutputStream(super.getOutputStream(), printStream));
        }
    }

    public static String getCurrentlyDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }
}
