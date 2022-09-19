package com.bank.search.repository;

import com.bank.search.entity.SearchWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Map;

public interface SearchRepository extends JpaRepository<SearchWord, String> {

    @Modifying
    @Query(value = "" +
            " UPDATE TB_SEARCH_WORD " +
            " SET COUNT_NUM = :#{#searchWord.countNum} " +
            "   , MOD_DATE = :#{#searchWord.modDate}  " +
            " WHERE KEYWORD = :#{#searchWord.keyword} "
    , nativeQuery = true
    )
    void updateSearchWord(@Param("searchWord") SearchWord searchWord);

    @Lock(LockModeType.PESSIMISTIC_READ)
    SearchWord findByKeyword(String keyword);

    @Query(value = " " +
            " SELECT Z.KEYWORD as keyword, Z.COUNT_NUM as countNum " +
            " FROM ( " +
            "   SELECT KEYWORD, COUNT_NUM, FIX_ORDER, '0' AS ORDERING " +
            "   FROM TB_SEARCH_WORD " +
            "   WHERE DISPLAY_YN = 'Y' " +
            "       AND FIX_YN = 'Y' " +
            "   UNION  ALL " +
            "   SELECT KEYWORD, COUNT_NUM, FIX_ORDER, 1 " +
            "   FROM TB_SEARCH_WORD " +
            "   WHERE DISPLAY_YN = 'Y' " +
            "       AND FIX_YN = 'N' " +
            "   ORDER BY ORDERING ASC, FIX_ORDER ASC, COUNT_NUM DESC " +
            "   LIMIT 10 " +
            " ) Z "
        , nativeQuery = true
    )
    List<Map<String, Object>> selectPopulateList();

    @Modifying
    @Query(value = " " +
            " UPDATE TB_SEARCH_WORD " +
            " SET DISPLAY_YN = :displayYn " +
            " WHERE KEYWORD = :keyword" +
            " "
        , nativeQuery = true
    )
    void updateDisplayYn(String keyword, String displayYn);

    @Modifying
    @Query(value = "" +
            " UPDATE TB_SEARCH_WORD " +
            " SET FIX_YN = :fixYn " +
            "     , FIX_ORDER = :order " +
            " WHERE KEYWORD = :keyword" +
            ""
        , nativeQuery = true
    )
    void updateFixKeyword(String keyword, String fixYn, Integer order);
}
