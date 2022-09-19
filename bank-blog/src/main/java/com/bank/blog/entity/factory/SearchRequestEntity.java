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
public class SearchRequestEntity {
    static String LOCAL_SEARCH_ENDPOINT_URL = "http://localhost:8081/search/search";

    public static RequestEntity getRequestEntity(SearchParam param) {
        // Header Setting
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // GET 전용 url 생성
        URI uri = UriComponentsBuilder.fromHttpUrl(LOCAL_SEARCH_ENDPOINT_URL)
                .queryParam("keyword", param.getQuery())
                .build().encode(Charset.forName("UTF-8")).toUri()
        ;
        return new RequestEntity(headers, HttpMethod.POST, uri);
    }
}
