package com.example.ecommerce.domain.article.dto.response;

import com.example.ecommerce.domain.article.Article;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ArticleReadResponseDto {

    private Long articleId;
    private LocalDateTime time1;
    private Date time2; // DB에는 UTC로 저장되어 있어도, 응답 time2는 한국시간으로 자동 변환됨
    private ZonedDateTime time3;
    private LocalDateTime convertedTime2; //
    private Date convertedConvertedTime2;
    private Date convertedTime1;

    public static ArticleReadResponseDto toDto(Article article) {

        LocalDateTime convertedTime2 = article.getTime2().toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime();
        Timestamp convertedConvertedTime2 = Timestamp.valueOf(convertedTime2);
        Timestamp convertedTime1 = Timestamp.valueOf(article.getTime1());

        // 타임존 설정
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        // Calendar 인스턴스 생성 및 타임존 적용
        Calendar calendar = Calendar.getInstance(timeZone);
        // 현재 날짜 및 시간 설정
        calendar.setTime(article.getTime2());
        // Date 객체로 변환
        Date time2 = calendar.getTime();

        ZonedDateTime zonedDateTime = convertedTime2.atZone(ZoneId.of("Asia/Seoul"));
        ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp time22 = Timestamp.valueOf(utcDateTime.toLocalDateTime());

        return ArticleReadResponseDto.builder()
                                     .articleId(article.getId())
                                     .time1(article.getTime1())
                                     .time2(article.getTime2())
                                     .time3(article.getTime3())
                                     .convertedTime2(convertedTime2)
                                     .convertedConvertedTime2(convertedConvertedTime2)
                                     .convertedTime1(convertedTime1)
                                     .build();
    }
}
