package com.chungnv.microservice.gateway.config;

import com.chungnv.microservice.gateway.filter.CustomErrorFilter;
import com.chungnv.microservice.gateway.filter.MyPostFilter;
import com.chungnv.microservice.gateway.filter.MyPreFilter;
import com.chungnv.microservice.service.AccessTokenService;
import com.chungnv.microservice.service.InternalRestTemplate;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GatewayBeanConfiguration {
    @Bean
    public MyPreFilter myPreFilter(InternalRestTemplate internalRestTemplate, AccessTokenService accessTokenService) {
        return new MyPreFilter(internalRestTemplate, accessTokenService);
    }

    @Bean
    public CustomErrorFilter customErrorFilter() {
        return new CustomErrorFilter();
    }

    @Bean
    public MyPostFilter myPostFilter() {
        return new MyPostFilter();
    }
}
