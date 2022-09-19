package com.bank.blog.controller;

import com.bank.blog.service.BankBlogService;
import com.bank.core.entity.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/blog")
public class BankBlogController {

    @Autowired
    private BankBlogService bankBlogService;

    /**
     * Health check를 위한 임의 api
     * @return true:정상/false:실패
     */
    @RequestMapping("/health")
    public ResponseEntity health() {
        return new ResponseEntity(true, HttpStatus.OK);
    }

    /**
     * 블로그 API 요청
     * @param param
     * @return
     */
    @RequestMapping("/query")
    public ResponseEntity blogQuery(@Valid SearchParam param) {
        return new ResponseEntity(bankBlogService.getQueryData(param), HttpStatus.OK);
    }
}
