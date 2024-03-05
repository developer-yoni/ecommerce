package com.example.ecommerce.domain.article;

import com.example.ecommerce.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "article")
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Column(name = "time1")
    private LocalDateTime time1;

    @Column(name = "time2")
    private Date time2;

    @Column(name = "time3")
    private ZonedDateTime time3;

    public static Article create(LocalDateTime time1, Date time2, ZonedDateTime time3) {

        Article article = Article.builder().time1(time1).time2(time2).time3(time3).build();
        return article;
    }
}
