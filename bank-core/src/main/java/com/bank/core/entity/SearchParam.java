package com.bank.core.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class SearchParam {
    @NotEmpty
    String query;

    @Min(value = 1, message = "1보다 작을 수 없습니다.")
    @Max(value = 50, message = "50을 초과할 수 없습니다.")
    Integer page;

    @Min(value = 1, message = "1보다 작을 수 없습니다.")
    @Max(value = 50, message = "50을 초과할 수 없습니다.")
    Integer size;

    @Pattern(regexp = "(CURR)|(accuracy)|(sim)|(DATE)|(date)|(recency)", message = "파라미터 구분을 확인해주세요.")
    String sort;
    String url;
}
