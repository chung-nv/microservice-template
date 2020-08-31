package com.chungnv.microservice.service;

import com.chungnv.microservice.constant.ServiceEnum;
import com.chungnv.microservice.exception.ErrorCode;
import com.chungnv.microservice.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class InternalRestTemplate {
    static final Logger logger = LoggerFactory.getLogger(InternalRestTemplate.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;

    public <T> ResponseEntity<T> exchange(HttpMethod method, ServiceEnum service, String path, Map<String, ?> uriVariables, @Nullable HttpEntity<?> requestEntity, Class<T> responseType) {
        ServiceInstance serviceInstance = getServiceInstance(service);
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + path;

        if (uriVariables == null) {
            uriVariables = new HashMap<>();
        }

        logger.info("Call internal API: {} {}", method.name(), url);

        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    public <T> T post(ServiceEnum service, String path, Object requestBody, Class<T> responseType) {
        HttpHeaders headers = extractHeaders(CommonUtil.getCurrentHttpRequest());
        HttpEntity requestEntity = new HttpEntity(requestBody, headers);
        return this.exchange(HttpMethod.POST, service, path, null, requestEntity, responseType).getBody();
    }

    private HttpHeaders extractHeaders(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String value = request.getHeader(headerName);
            headers.add(headerName, value);
        }

        // for zuul gateway (not using Spring Dispatch Servlet)
        headers.addIfAbsent("correlationId", MDC.get("correlationId"));

        return headers;
    }

    private ServiceInstance getServiceInstance(ServiceEnum service) {
        List<ServiceInstance> instanceList = discoveryClient.getInstances(service.getId());
        if (instanceList == null || instanceList.size() < 1) {
            throw ErrorCode.INSTANCE_NOT_FOUND.build(service.getId());
        }
        int randomInstanceIndex = ThreadLocalRandom.current().nextInt(0, instanceList.size());
        return instanceList.get(randomInstanceIndex);
    }
}
