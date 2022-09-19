DROP TABLE IF EXISTS TB_SEARCH_WORD;
CREATE TABLE TB_SEARCH_WORD (
    KEYWORD IDENTITY VARCHAR(2000) NOT NULL           -- 검색어
    , COUNT_NUM INT DEFAULT 1                           -- 검색카운트
    , DISPLAY_YN VARCHAR(1) DEFAULT 'Y'        -- Y/N
    , FIX_YN VARCHAR(1) DEFAULT 'N'            -- 고정 여부
    , FIX_ORDER INT DEFAULT 0                -- 고정 순서
    , REG_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP()           -- 등록일
    , MOD_DATE TIMESTAMP            -- 수정일
) KEY (KEYWORD);

