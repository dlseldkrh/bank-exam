package com.bank.blog.service;

import com.bank.blog.entity.factory.CustomRequestEntityFactory;
import com.bank.core.entity.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class BankBlogServiceImpl implements BankBlogService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${kakao.blog.endpoint.url}")
    String KAKAO_BLOG_ENDPOINT_URL;
    @Value("${naver.blog.endpoint.url}")
    String NAVER_BLOG_ENDPOINT_URL;

    @Override
    public String getQueryData(SearchParam param) {
        // 기본은 카카오
        if (param.getUrl() == null) {
            param.setUrl(KAKAO_BLOG_ENDPOINT_URL);
        }

        // 에러 처리를 위한 factory, handler 추가
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
                // logging
                log.error("StatusCode  :  " + httpResponse.getStatusCode());
                log.error("StatusMessage  :  " + httpResponse.getStatusText());
                HttpStatus httpStatus = httpResponse.getStatusCode();
                return httpStatus.series() == HttpStatus.Series.SERVER_ERROR;
            }
        });
        // 한글 깨짐 조치
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        // ResponseEntity 선언
        ResponseEntity<String> responseEntity = null;

        // exchage
        responseEntity = restTemplate.exchange(CustomRequestEntityFactory.getCustomInstance(param), String.class);

        // 성공여부 체크
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            // 1차 실패 시 네이버로 재 실행
            param.setUrl(NAVER_BLOG_ENDPOINT_URL);
            responseEntity = restTemplate.exchange(CustomRequestEntityFactory.getCustomInstance(param), String.class);
        }

        // 성공 시 검색어를 검색어 모듈쪽에 전달 / 에러 시 무시
        try {
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ResponseEntity populateResponse = restTemplate.exchange(CustomRequestEntityFactory.getSearchRequestEntity(param), String.class);
                if (populateResponse.getStatusCode() != HttpStatus.OK) {
                    log.error("'/search/populate' is not working.");
                }
            }
        } catch (Exception e) {
            // 에러만 찍고 넘어감
            log.error(e.getMessage());
        }

        JSONObject json = new JSONObject(responseEntity.getBody());
        return json.toString();
    }
}
