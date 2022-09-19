package com.bank.search.service;

import com.bank.search.entity.SearchWord;
import com.bank.search.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchRepository searchRepository;

    @Override
    @Transactional
    public void createSearchWord(SearchWord searchWord) {
        // 값 존재여부 체크
        SearchWord updatetObject = searchRepository.findByKeyword(searchWord.getKeyword());
        // 없으면 넣고 있으면 업데이트
        if (updatetObject == null) {
            searchRepository.save(searchWord);
        } else {
            updatetObject.setCountNum(updatetObject.getCountNum() + 1);
            updatetObject.setModDate(new Timestamp(System.currentTimeMillis()));
            searchRepository.updateSearchWord(updatetObject);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "CACHE_POPULATE")
    public List<Map<String, Object>> getSearchWord() {
        return searchRepository.selectPopulateList();
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "CACHE_POPULATE", allEntries = true)
    public String hideKeyword(String keyword, String displayYn) {
        SearchWord searchWord = searchRepository.findByKeyword(keyword);
        if (searchWord != null) {
            searchRepository.updateDisplayYn(searchWord.getKeyword(), displayYn);
            return searchWord.getKeyword() + (displayYn.equals("N") ? " is Hidden." : " is Shown.");
        } else {

        }
        return keyword + " is not exists.";
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "CACHE_POPULATE", allEntries = true)
    public String fixKeyword(String keyword, String fixYn, Integer order) {
        SearchWord searchWord = searchRepository.findByKeyword(keyword);
        if (searchWord != null) {
            searchRepository.updateFixKeyword(searchWord.getKeyword(), fixYn, order);
            return searchWord.getKeyword() + (fixYn.equals("Y") ? " is fixed." : " is unfixed.");
        }
        return keyword + " is not exists.";
    }
}
