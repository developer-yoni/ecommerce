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
    // 사실 LocalDateTime의 시간은 잘 모르겠다 -> 유력하게 유추되는건, dabase의 timezone이 어떻든, LocalDateTime은 Springboot의 Timezone을 따른다
    // 따라서 Springboot의 timezone이 UTC라면 그냥 LocalDateTime 쓰면 되고 (사실 안변하니까 가장 안전)
    // 그렇지 않다면 응답으로 UTC로 fix할 수 있는 Date나 ZonedDateTime이 적합하지 않을까
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="UTC") // LocalDateTime은 @JsonFormat의 timezone에 영향 안받음
    private LocalDateTime time1;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="UTC") // Date는 @JsonFormat의 timezone에 영향 받아서 변환됨
    private Date time2;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="UTC") // ZonedDateTime은 @JsonFormat의 timezone에 영향 받아서 변환됨
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
