package com.bank.blog.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    // Connectin Pool 사용을 위한 선언
    @Bean
    HttpClient httpClient() {
        return HttpClientBuilder.create()
                .setMaxConnPerRoute(10)
                .setMaxConnTotal(50)
                .build();
    }

    // 연결 시간 지정을 위한 factory 선언
    @Bean
    HttpComponentsClientHttpRequestFactory factory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(3000);
        factory.setHttpClient(httpClient);
        return factory;
    }

    @Bean
    RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }
}
