package com.bank.search.controller;

import com.bank.search.entity.SearchWord;
import com.bank.search.repository.SearchRepository;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private SearchRepository searchRepository;

    @BeforeEach
    void beforeEachMethod() throws Exception {
        // 함수 검증
        // e~n까지 알파벳으로 임시 데이터 추가
        int default_data = 100;
        SearchWord searchWord = null;
        for (int i=0; i<20; i++) {
            // random chat 넣도록 지정 (e~n까지)
            char ch = (char) (default_data + i);
            searchWord = new SearchWord();
            searchWord.setKeyword(String.valueOf(ch));
            searchWord.setCountNum((int) (Math.random() * 10));

            // 테스트 확인을 위해서 'e'의 경우에 최대값이될 11로 추가
            if (i == 1) {
                searchWord.setCountNum(11);
            }
            System.out.println(i + "  :  " + searchWord.getKeyword());
            searchRepository.save(searchWord);
        }
    }

    @DisplayName("/search/search 서비스 테스트")
    @Test
    void search() throws Exception {
        // api 검증
        mvc.perform(post("/search/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("keyword", "e")
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("/search/populate 서비스 테스트")
    @Test
    void populate() throws Exception {
        // api 검증
        mvc.perform(get("/search/populate")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("/search/hide 서비스 테스트")
    @Test
    void hide() throws Exception {
        // api 검증
        mvc.perform(post("/search/hide")
                .contentType(MediaType.APPLICATION_JSON)
                .param("keyword", "e")
                .param("displayYn", "N")
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("/search/fix 서비스 테스트")
    @Test
    void fix() throws Exception {
        // api 검증
        mvc.perform(post("/search/fix")
                .contentType(MediaType.APPLICATION_JSON)
                .param("keyword", "e")
                .param("fixYn", "Y")
                .param("order", "2")
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }


    @DisplayName("키워드 시나리오 테스트")
    @Test
    void scenarioTest()throws Exception {
        // 필요 변수
        MvcResult result = null;

        // 1. 데이터 추가 (beforeeach)
        // 2. 데이터 조회 시 특정 데이터 있는지 체크
        // api 검증
        result = mvc.perform(get("/search/populate")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
        // 비교할 String
        String asisString = result.getResponse().getContentAsString();

        // 3. 데이터 추가 후 목록 조회 : 목록 변화 없음 (캐싱)
        mvc.perform(post("/search/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("keyword", "e")
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        // api 검증
        result = mvc.perform(get("/search/populate")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
        // 비교할 String
        String no3String = result.getResponse().getContentAsString();
        // 결과 체크 : 캐싱으로 인해서 String 변경 없음
        assertTrue(asisString.equals(no3String));

        // 4. 데이터 display 수준 변경 후 목록 조회 : 목록 변화 있음 + 요청한 키워드 안보임 (캐싱 삭제)
        mvc.perform(post("/search/hide")
                .contentType(MediaType.APPLICATION_JSON)
                .param("keyword", "e")
                .param("displayYn", "N")
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

        // api 검증
        result = mvc.perform(get("/search/populate")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
        // 비교할 String
        String no4String = result.getResponse().getContentAsString();

        // e는 조회되지 않음
        assertFalse(asisString.equals(no4String));
        assertEquals(no4String.indexOf("\"e\""), -1);

        System.out.println(no4String);

        // 5. 데이터 fix 요청 후 목록 조회 : 목록 변화 있음 + 첫번째 목록이 해당 키워드 (캐싱 삭제)
        mvc.perform(post("/search/fix")
                .contentType(MediaType.APPLICATION_JSON)
                .param("keyword", "f")
                .param("fixYn", "Y")
                .param("order", "2")
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

        // 데이터 조회
        result = mvc.perform(get("/search/populate")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
        // 비교할 String
        String no5String = result.getResponse().getContentAsString();
        // 첫번째 값을 찾기 위해 jsonarray로 변경
        JSONArray jsonArray = new JSONArray(no5String);

        // 고정했던 f가 첫번째 인자로 들어옴
        assertTrue(jsonArray.getJSONObject(0).get("KEYWORD").toString().equals("f"));

        // 결과 조회
        System.out.println(no5String);
    }
}