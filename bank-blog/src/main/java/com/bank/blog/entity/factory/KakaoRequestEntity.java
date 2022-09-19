package com.bank.blog.entity.factory;

import com.bank.core.entity.SearchParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;

@Component
public class KakaoRequestEntity {
    static String KAKAO_BLOG_ENDPOINT_URL;
    static String KAKAO_REST_API_PREFIX_HEADER;
    static String KAKAO_REST_API_KEY;
    static Integer PAGE_SIZE;

    @Value("${kakao.blog.endpoint.url}")
    private void setKakaoBlogEndpointUrl(String value) {
        KAKAO_BLOG_ENDPOINT_URL = value;
    }
    @Value("${kakao.rest.api.prefix.header}")
    private void setKakaoRestApiPrefixHeader(String value) {
        KAKAO_REST_API_PREFIX_HEADER = value;
    }
    @Value("${kakao.rest.api.key}")
    private void setKakaoRestApiKey(String value) {
        KAKAO_REST_API_KEY = value;
    }
    @Value("${api.page.size}")
    private void setPageSize(Integer value) {PAGE_SIZE=value;}

    public static RequestEntity getRequestEntity(SearchParam param) {
        // Header Setting
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Kko용 authorization prefix 추가
        headers.set(HttpHeaders.AUTHORIZATION, KAKAO_REST_API_PREFIX_HEADER + " " + KAKAO_REST_API_KEY);

        // Sort 조건 분리 (정확도[기본], 날짜 순)
        if (param.getSort() == null) {
            param.setSort("accuracy");
        } else if ("DATE".equals(param.getSort())) {
            param.setSort("recency");
        } else {
            param.setSort("accuracy");
        }

        // GET 전용 url 생성
        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_BLOG_ENDPOINT_URL)
                .queryParam("query", param.getQuery())
                .queryParam("page", param.getPage() == null ? 1 : param.getPage())
                .queryParam("size", param.getSize() == null ? PAGE_SIZE : param.getSize())
                .queryParam("sort", param.getSort() == null ? "accuracy" : param.getSort())
                .build().encode(Charset.forName("UTF-8")).toUri()
        ;
        return new RequestEntity(headers, HttpMethod.GET, uri);
    }
}
