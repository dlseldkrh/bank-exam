package com.bank.blog.service;

import com.bank.core.entity.SearchParam;

public interface BankBlogService {
    /**
     * API를 이용한 Query Data 조회
     * @param param
     * @return json string
     */
    String getQueryData(SearchParam param);
}
