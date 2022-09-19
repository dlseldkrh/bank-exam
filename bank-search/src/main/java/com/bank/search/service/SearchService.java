package com.bank.search.service;

import com.bank.search.entity.SearchWord;

import java.util.List;
import java.util.Map;

public interface SearchService {
    void createSearchWord(SearchWord searchWord);

    List<Map<String, Object>> getSearchWord();

    String hideKeyword(String keyword, String displayYn);

    String fixKeyword(String keyword, String fixYn, Integer order);
}
