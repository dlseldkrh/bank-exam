# 신규 과제 제출
## 과제 설명
제출용 신규 과제 입니다. 구현에 필요한 내용은 총 2가지로 아래와 같습니다.  
* 블로그 검색 API를 이용한 API
* 인기 검색어 목록

## 개발환경
* Intellij CE
* Java 11
* Springboot
* Gradle
* H2 DB
* External Librarys
  * org.projectlombok - getter, setter, toString 등
  * org.json - json parsing
  * org.apache.httpcomponents - resttemplate pooling 등
  * com.h2database - in-memory db 활용

## API 명세서
1. 블로그 검색
- URL : http://{Server IP}:{8080}/blog/query
- Method : GET
``` java
// Request Field
String query; // 검색어, 필수
Integer page; // 페이징, 1~50 (2안 스펙)
Integer size; // 한 페이징 별 호출 건수, 1~50 (2안 스펙)
String sort; // 정렬기준 (CURR)|(accuracy)|(sim)|(DATE)|(date)|(recency) 정확도3, 최근3
```
``` json
* keyword : 집꾸미기
{
    "documents": [
        {
            "blogname": "마음의세상",
            "datetime": "2022-06-13T13:42:08.000+09:00",
            "thumbnail": "https://search4.kakaocdn.net/argon/130x130_85_c/2BXrZ8ms4DA",
            "contents": "플랫폼인 &#39;<b>집</b><b>꾸미기</b>&#39;와 &#39;원룸만들기&#39;를 운영하고 있습니다. 인테리어는 정보탐색부터 내 취향 찾기, 의사결정 등 여러 과정을 거쳐야 하고 한 번 결정한 후에는 쉽게 바꿀 수 없습니다. 그래서 내 <b>집</b>을 꾸미는 일에 쉽게 도전하는 것을 주저하게 되는데요. <b>집</b><b>꾸미기</b>는 인테리어 과정에서 겪게 되는 어려움을 해결하고...",
            "title": "인테리어 앱, <b>집</b><b>꾸미기</b> 어플 사용하기",
            "url": "http://worldhearts.tistory.com/163"
        },
        .....
        ],
    "meta": {
        "total_count": 1043593,
        "is_end": false,
        "pageable_count": 798
    }
}

* Naver 검색 결과
{
    "total": 383767,
    "lastBuildDate": "Mon, 19 Sep 2022 20:50:27 +0900",
    "display": 30,
    "start": 1,
    "items": [
        {
            "link": "https://blog.naver.com/arafatare?Redirect=Log&logNo=222870225344",
            "postdate": "20220908",
            "description": "가을맞이 <b>집꾸미기</b> 이케아 소품과 타일카펫으로 분위기 변신 ©diana 현재 살고있는 아파트로 이사온지... 요즘 열심히 <b>집꾸미기</b> 한다고 일주일에 세번은 가는듯. 근데 바닥이 바뀌지 않으니 가구를 들여놔도... ",
            "title": "이케아 선반 조명 &amp; 타일 카페트 가을맞이 <b>집꾸미기</b>",
            "bloggerlink": "https://blog.naver.com/arafatare",
            "bloggername": "Diana Beauty ෆ Fashion"
        },
        .....
    ]
}
```

2. 인기검색어
   1) 인기검색어 등록
    - URL : http://{Server IP}:{8081}/search/search
    - Method : POST
   ``` java
   // Request Field
   String keyword; // 검색어
   
   // Result
   true
    ```
   2) 인기검색어 조회 : 비노출을 제외, 
    - URL : http://{Server IP}:{8081}/search/populate
    - Method : GET
   ``` json
   // Result
   [
    {
        "KEYWORD": "마음의세상",
        "COUNTNUM": 6
    },
   ...
   ]
    ```
   3) [추가기능] 특정 인기검색어 노출/비노출 설정
    - URL : http://{Server IP}:{8081}/search/hide
    - Method : POST
   ``` java
    // Request Field
    String keyword; // 검색어, 필수
    String showYn;  // 노출, 비노출 여부 (Y/N)
   
    // Result
    xxx is not exists.            // 검색어 없을 시
    xxx is Hidden.                // 검색어 숨김
    xxx is Shown.                 // 검색어 숨김
    Wrong Parameter showYn. (Y/N) // 파라미터 문제 시
    ```
   4) [추가기능] 특정 인기검색어 고정/고정해제 설정
    - URL : http://{Server IP}:{8081}/search/fix
    - Method : POST
   ``` java
    // Request Field
    String keyword; // 검색어, 필수
    String fixYn;   // 노출, 비노출 여부 (Y/N)
    Integer order;  // 정렬 순서
   
    // Result
    xxx is fixed.            // 검색어 없을 시
    xxx is unfixed.                // 검색어 숨김
    Wrong Parameter fixYn. (Y/N) // 파라미터 문제 시
    ```

## 테스트 케이스
- bank-blog, bank-search 모듈 내 Test 패키지 내 추가됨

1. bank-blog
   1. 정상 데이터 조회
   2. 검색어 문제 시 : 빈 검색어 입력 시 bad request 에러
   3. valid 문제 : @Valid의 체크값인 page, size의 값이 max인 50이 넘은 경우 bad request 에러
   4. sort 문제 : 기대되지 않는 파라미터 입력 시 bad request 에러
   5. 네이버로 강제 요청 : 정상 수신 및 콘솔에 결과 출력
   > ** kakao daum 실패 테스트를 위해서는 아래 동작 필요  
   > Case1. bank-blog > application.properties > kakao.rest.api.key 값 수정  
   > Case2. bank-blog > KakaoRequestEntity.java에서 해당 데이터 수정
   > ``` java 
   > 변경 전 : @Value("${kakao.rest.api.key}")
   > 변경 후 : @Value("${kakao.rest.api.key_old}")
   > ```

2. bank-search
   0. @BeforeEach beforeEachMethod() 함수로 항상 테스트 데이터 추가
   1. search : 키워드 입력 시 정상 수신
   2. populate : 서비스 호출 시 정상 데이터 수신
   3. hide : 숨김 호출 시 정상 수신
   4. fix : 데이터 고정 호출 시 정상 수신
   5. Scenario 테스트
   > ** 시나리오 검증  
   > * @BeforeEach로 필요 데이터 추가
   > 1. populate로 데이터 조회
   > 2. search로 키워드 추가 후 populate로 조회 : **[1번 수행 시 Cache 적용으로 변경없음]**
   > 3. hide로 display 값 수정 후 populate로 조회 : 최대값이던 "e" 데이터가 사라짐  
   >   * *2번까지 변경된 내용으로 조회*
   > 4. fix 요청 후 populate로 조회 : "f"가 고정되어 맨 앞에 데이터로 조회 됨

## 기타 조건
### * 아래 추가 요건들에 대해서 조치 하였습니다.
- [x] 멀티 모듈로 생성
- [x] 트래픽이 많고 데이터가 큰 경우 : @Cacheable 을 이용해서 처리 (타이머, 수정 필요 시 cache 초기화)
- [x] 동시성 이슈 : JPA의 비관적 락 이용
- [x] K안 문제로 에러 발생 시 N안으로

## 파일 다운로드 링크 추가
- [bank-blog jar](https://github.com/dlseldkrh/bank-exam/blob/main/bank-blog-1.0-SNAPSHOT.jar)  
- [bank-search jar](https://github.com/dlseldkrh/bank-exam/blob/main/bank-search-1.0-SNAPSHOT.jar)
