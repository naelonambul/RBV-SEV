package com.rbc.red.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookSearchCondition {
    // 카테고리별, 자산별, 출금별, 가격, 날짜, 팀, 유저
    private Long teamId;
    private Long userSeq;
    private Long groupId;
    private Long categoryId;
    private Long assetId;
    private Long priceGoe;
    private Long priceLoe;
    private LocalDateTime dataTimeAfter;
    private LocalDateTime dataTimeBefore;
}
