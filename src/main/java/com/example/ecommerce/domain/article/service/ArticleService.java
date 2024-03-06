package com.example.ecommerce.domain.article.service;

import com.example.ecommerce.domain.article.Article;
import com.example.ecommerce.domain.article.dto.response.ArticleReadResponseDto;
import com.example.ecommerce.domain.article.repository.ArticleRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public void createArticleList() {

        LocalDateTime startAt1 = LocalDateTime.of(2024, 03, 06, 0, 0, 0);
        Date startAt2 = Timestamp.valueOf(startAt1);
        ZonedDateTime startAt3 = ZonedDateTime.of(2024, 03, 06, 0, 0, 0, 0, ZoneId.of("UTC"));

        IntStream.range(0, 24).forEach(
                intValue -> {

                    Article article = Article.create(startAt1.plusHours(intValue), plusHour(startAt2, intValue), startAt3.plusHours(intValue));
                    articleRepository.save(article);
                }
        );
    }

    private Date plusHour(Date currentTime, int hour) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);

        // Calendar에 1시간을 추가
        calendar.add(Calendar.HOUR_OF_DAY, hour);

        // Calendar의 시간을 업데이트된 Date 객체로 변환
        Date updatedDate = calendar.getTime();

        return updatedDate;
    }

    public List<ArticleReadResponseDto> getArticleListV1(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        List<Article> articleList = articleRepository.findAllBetweenStartDateTimeAndEndDateTime(startDateTime, endDateTime);
        return articleList.stream().map(ArticleReadResponseDto::toDto).collect(Collectors.toList());
    }

    public List<ArticleReadResponseDto> getArticleListV2(Date startDateTime, Date endDateTime) {

        System.out.println("변환된 시작 시간 " + startDateTime.toString());
        System.out.println("변환된 종료 시간 " + endDateTime.toString());
        List<Article> articleList = articleRepository.findAllBetweenStartDateTimeAndEndDateTime(startDateTime, endDateTime);
        return articleList.stream().map(ArticleReadResponseDto::toDto).collect(Collectors.toList());
    }

    public List<ArticleReadResponseDto> getArticleListV3(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {

        List<Article> articleList = articleRepository.findAllBetweenStartDateTimeAndEndDateTime(startDateTime, endDateTime);
        return articleList.stream().map(ArticleReadResponseDto::toDto).collect(Collectors.toList());
    }

    public List<ArticleReadResponseDto> getArticleListV4(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        List<Article> articleList = articleRepository.findAllBetweenStartDateTimeAndEndDateTime(Timestamp.valueOf(startDateTime),
                                                                                                Timestamp.valueOf(endDateTime));
        return articleList.stream().map(ArticleReadResponseDto::toDto).collect(Collectors.toList());
    }
}
