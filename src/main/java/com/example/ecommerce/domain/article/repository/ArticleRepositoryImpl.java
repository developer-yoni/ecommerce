package com.example.ecommerce.domain.article.repository;

import com.example.ecommerce.domain.article.Article;
import com.example.ecommerce.domain.article.QArticle;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Article> findAllBetweenStartDateTimeAndEndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        return queryFactory.select(QArticle.article)
                           .from(QArticle.article)
                           .where(QArticle.article.time1.between(startDateTime, endDateTime))
                           .fetch();
    }

    @Override
    public List<Article> findAllBetweenStartDateTimeAndEndDateTime(Date startDateTime, Date endDateTime) {

        return queryFactory.select(QArticle.article)
                .from(QArticle.article)
                .where(QArticle.article.time2.between(startDateTime, endDateTime))
                .fetch();
    }

    @Override
    public List<Article> findAllBetweenStartDateTimeAndEndDateTime(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {

        return queryFactory.select(QArticle.article)
                           .from(QArticle.article)
                           .where(QArticle.article.time3.between(startDateTime, endDateTime))
                           .fetch();
    }
}
