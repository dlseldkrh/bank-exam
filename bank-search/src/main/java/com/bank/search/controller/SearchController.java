package com.bank.search.controller;

import com.bank.search.entity.SearchWord;
import com.bank.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * Health check 및 서버 테스트
     * @return
     */
    @RequestMapping("/health")
    public ResponseEntity health() {
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    /**
     * 검색어 입력 시 데이터 insert
     * @param searchWord
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity search(SearchWord searchWord) {
        searchService.createSearchWord(searchWord);
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    /**
     * 인기검색어 TOP 10 조회
     * @return
     */
    @RequestMapping(value = "/populate", method = RequestMethod.GET)
    public ResponseEntity populate() {
        return new ResponseEntity(searchService.getSearchWord(), HttpStatus.OK);
    }


    /**
     * 특정 키워드 숨김, 노출 처리
     * @param keyword
     * @param showYn (Y/N)
     * @return
     */
    @RequestMapping(value="/hide", method = RequestMethod.POST)
    public ResponseEntity hide(@RequestParam String keyword, @RequestParam String displayYn) {
        if (!"Y".equals(displayYn) && !"N".equals(displayYn)) {
            return new ResponseEntity("Wrong Parameter showYn. (Y/N)", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(searchService.hideKeyword(keyword, displayYn), HttpStatus.OK);
    }

    /**
     * 특정 키워드 고정, 고정취소 처리
     * @param keyword
     * @param fixYn (Y/N)
     * @param order
     * @return
     */
    @RequestMapping(value="/fix", method = RequestMethod.POST)
    public ResponseEntity fix(@RequestParam String keyword
            , @RequestParam String fixYn
            , @RequestParam Integer order) {
        if (!"Y".equals(fixYn) && !"N".equals(fixYn)) {
            return new ResponseEntity("Wrong Parameter fixYn. (Y/N)", HttpStatus.BAD_REQUEST);
        }
        // fixYn == N이면 order는 0으로 변경
        if ("N".equals(fixYn)) {
            order = 0;
        }
        return new ResponseEntity(searchService.fixKeyword(keyword, fixYn, order), HttpStatus.OK);
    }
}
