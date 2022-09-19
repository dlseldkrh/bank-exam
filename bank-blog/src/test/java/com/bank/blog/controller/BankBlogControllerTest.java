package com.bank.blog.controller;

import com.bank.blog.entity.factory.CustomRequestEntityFactory;
import com.bank.core.entity.SearchParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BankBlogControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${naver.blog.endpoint.url}")
    String NAVER_REST_ENDPOINT_URL;
    @Value("${api.page.size}")
    Integer PAGE_SIZE;

    @DisplayName("/blog/query 정상 테스트")
    @Test
    void blogQuery() throws Exception {
        mvc.perform(get("/blog/query")
                .contentType(MediaType.APPLICATION_JSON)
                .param("query", "집꾸미기")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "date")
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

        // 검색어 문제
        mvc.perform(get("/blog/query")
                .contentType(MediaType.APPLICATION_JSON)
                .param("query", "")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "date")
        ).andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());

        // valid 문제
        mvc.perform(get("/blog/query")
                .contentType(MediaType.APPLICATION_JSON)
                .param("query", "집꾸미기")
                .param("page", "100")
                .param("size", "100")
                .param("sort", "date")
        ).andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());

        // sort 문제
        mvc.perform(get("/blog/query")
                .contentType(MediaType.APPLICATION_JSON)
                .param("query", "집꾸미기")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "new")
        ).andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());

        // 네이버객체 호출
        SearchParam searchParam = new SearchParam();
        searchParam.setQuery("집꾸미기");
        searchParam.setPage(1);
        searchParam.setSize(10);
        searchParam.setSort("date");
        searchParam.setUrl(NAVER_REST_ENDPOINT_URL);

        ResponseEntity<String> responseEntity = restTemplate.exchange(CustomRequestEntityFactory.getNaverRequestEntity(searchParam), String.class);
        assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
        System.out.println(responseEntity.getBody().toString());
    }
}