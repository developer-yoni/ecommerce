package com.example.ecommerce.domain.article.repository;

import com.example.ecommerce.domain.article.Article;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public interface ArticleRepositoryCustom {

    List<Article> findAllBetweenStartDateTimeAndEndDateTime(Date startDateTime, Date endDateTime);

    List<Article> findAllBetweenStartDateTimeAndEndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Article> findAllBetweenStartDateTimeAndEndDateTime(ZonedDateTime startDateTime, ZonedDateTime endDateTime);
}
