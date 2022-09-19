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
public class NaverRequestEntity {
    static String NAVER_CLIENT_ID;
    static String NAVER_CLIENT_SECRET;
    static String NAVER_REST_ENDPOINT_URL;
    static Integer PAGE_SIZE;

    @Value("${naver.client.id}")
    void setNaverClientId(String value) { NAVER_CLIENT_ID = value;}
    @Value("${naver.client.secret}")
    void setNaverClientSecret(String value) { NAVER_CLIENT_SECRET = value;}
    @Value("${naver.blog.endpoint.url}")
    void setNaverRestEndpointUrl(String value) {NAVER_REST_ENDPOINT_URL = value;}
    @Value("${api.page.size}")
    private void setPageSize(Integer value) {PAGE_SIZE=value;}

    public static RequestEntity getRequestEntity(SearchParam param) {
        // Header Setting
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Kko용 authorization prefix 추가
        headers.set("X-Naver-Client-Id", NAVER_CLIENT_ID);
        headers.set("X-Naver-Client-Secret", NAVER_CLIENT_SECRET);

        // Sort 조건 분리 (정확도[기본], 날짜 순)
        if (param.getSort() == null) {
            param.setSort("sim");
        } else if ("DATE".equals(param.getSort())) {
            param.setSort("date");
        } else {
            param.setSort("sim");
        }

        // GET 전용 url 생성
        URI uri = UriComponentsBuilder.fromHttpUrl(NAVER_REST_ENDPOINT_URL)
                .queryParam("query", param.getQuery())
                .queryParam("start", param.getPage() == null ? 1 : param.getPage())
                .queryParam("display", param.getSize() == null ? PAGE_SIZE : param.getSize())
                .queryParam("sort", param.getSort() == null ? "sim" : param.getSort())
                .build().encode(Charset.forName("UTF-8")).toUri()
                ;
        return new RequestEntity(headers, HttpMethod.GET, uri);
    }
}
