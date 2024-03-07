package com.example.ecommerce.domain.article.dto.response;

import com.example.ecommerce.domain.article.Article;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ArticleReadResponseDto {

    private Long articleId;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="UTC")
    private LocalDateTime time1;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="UTC")
    private Date time2; // DB에는 UTC로 저장되어 있어도, 응답 time2는 한국시간으로 자동 변환됨
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="UTC")
    private ZonedDateTime time3;


    public static ArticleReadResponseDto toDto(Article article) {

        return ArticleReadResponseDto.builder()
                                     .articleId(article.getId())
                                     .time1(article.getTime1())
                                     .time2(article.getTime2())
                                     .time3(article.getTime3())
                                     .build();
    }
}
