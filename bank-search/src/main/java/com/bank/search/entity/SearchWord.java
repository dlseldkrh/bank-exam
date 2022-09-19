package com.bank.search.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TB_SEARCH_WORD")
@Getter
@Setter
@DynamicInsert
public class SearchWord {
    @Id
    @Column(name = "KEYWORD")
    String keyword;
    @ColumnDefault("1")
    Integer countNum;
    @ColumnDefault("'Y'")
    String displayYn;
    @ColumnDefault("'N'")
    String fixYn;
    @ColumnDefault("0")
    Integer fixOrder;
    @ColumnDefault("CURRENT_TIMESTAMP()")
    Timestamp regDate;
    @Column(name = "MOD_DATE")
    Timestamp modDate;
}
