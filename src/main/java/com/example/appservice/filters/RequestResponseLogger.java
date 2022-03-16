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
import java.util.UUID;

@Component
@Slf4j
@Order(1)
public class RequestResponseLogger implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper((HttpServletRequest) servletRequest);

        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
        String date = f.format(new Date());
        UUID uuid = UUID.randomUUID();

        String method = requestWrapper.getMethod();
        String uri = requestWrapper.getRequestURI();
        String step = "Default Step";

        switch (method) {
            case "POST" :
                switch (uri) {
                    case "/user" : step = "Adding User";
                        break;
                    default: step = "Default";
                }
                break;
            case "GET" : step = "Get User";
                break;
            case "DELETE" : step = "Delete User";
                break;
            case "PATCH" : step = "Update User";
                break;
        }

        String url = String.valueOf(requestWrapper.getRequestURL());
        String serverName = requestWrapper.getServerName();
        String reqBody = new String(requestWrapper.getByteArray());

        CustomHttpResponseWrapper responseWrapper = new CustomHttpResponseWrapper((HttpServletResponse) servletResponse);

        filterChain.doFilter(requestWrapper, responseWrapper);

//        Request Inline
//        log.info("Request Body: {}", new String(requestWrapper.getByteArray()).replaceAll("\n", " "));

        long end = System.currentTimeMillis() - start;

        int status = responseWrapper.getStatus();

        if(serverName.equals("localhost")) {
            log.info("INFO {} {} \n" +
                    "ID : {}\n" +
                    "STEP : {}\n" +
                    "Request URL: {}\n" +
                    "Host : {}\n" +
                    "Request Body: {}\n" +
                    "Response Status : {}\n" +
                    "Response Body :  \n" +
                    "Response Time : {}", date, uuid, getCurrentlyDateTime(), step, url, serverName, reqBody, status, end);
        }

//        log.info("Response Body : {}", new String(responseWrapper.getBaos().toByteArray()));
    }

    @Override
    public void destroy() {

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
