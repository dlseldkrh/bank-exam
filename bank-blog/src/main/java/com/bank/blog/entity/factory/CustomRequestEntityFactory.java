package com.bank.blog.entity.factory;

import com.bank.core.entity.SearchParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

public class CustomRequestEntityFactory {
    /**
     * URL에 따른 RequestEntity instance 획득
     * @param param
     * @return RequestEntity
     */
    public static RequestEntity getCustomInstance(SearchParam param) {
        // url 값이 없으면 null 리턴
        if (param.getUrl().isEmpty()) {
            return null;
        }

        // HttpHeaders 설정
        HttpHeaders headers = null;

        // URL 값으로 어떤 객체를 사용하고, 설정할 지 검토
        if (param.getUrl().indexOf("kakao.com") > 0) {
            // kko
            return KakaoRequestEntity.getRequestEntity(param);
        } else if (param.getUrl().indexOf("naver.com") > 0) {
            // naver
            return NaverRequestEntity.getRequestEntity(param);
        }

        // Default
        return new RequestEntity(headers, HttpMethod.GET, URI.create(param.getUrl()));
    }

    /**
     * 카카오용 RequestEntity
     * @param param
     * @return KakaoRequestEntity
     */
    public static RequestEntity getKKORequestEntity(SearchParam param) {
        return KakaoRequestEntity.getRequestEntity(param);
    }

    /**
     * 네이버용 RequestEntity
     * @param param
     * @return NaverRequestEntity
     */
    public static RequestEntity getNaverRequestEntity(SearchParam param) {
        return NaverRequestEntity.getRequestEntity(param);
    }

    /**
     * local 검색어 추가용 RequestEntity
     * @param param
     * @return NaverRequestEntity
     */
    public static RequestEntity getSearchRequestEntity(SearchParam param) {
        return SearchRequestEntity.getRequestEntity(param);
    }
}
