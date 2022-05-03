package com.rbc.red.api.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookDto {
    private Long bookId;
    private String teamName;
    private String username;
    private String categoryName;
    private String assetName;
//    private String transferName;
    private Long price;
    private String memo;
    private String filePath;
    private LocalDateTime localDateTime;

    @QueryProjection
    public BookDto(Long bookId, String teamName, String username, String categoryName, String assetName, /*String transferName,*/ Long price, String memo, String filePath,  LocalDateTime localDateTime) {
        this.bookId = bookId;
        this.teamName = teamName;
        this.username = username;
        this.categoryName = categoryName;
        this.assetName = assetName;
//        this.transferName = transferName;
        this.price = price;
        this.memo = memo;
        this.filePath = filePath;
        this.localDateTime = localDateTime;
    }
}
