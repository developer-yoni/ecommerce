package com.example.ecommerce.domain.article.controller;

import com.example.ecommerce.domain.article.dto.request.ArticleReadRequestDtoV1;
import com.example.ecommerce.domain.article.dto.request.ArticleReadRequestDtoV2;
import com.example.ecommerce.domain.article.dto.request.ArticleReadRequestDtoV3;
import com.example.ecommerce.domain.article.dto.request.ArticleReadRequestDtoV4;
import com.example.ecommerce.domain.article.dto.response.ArticleReadResponseDto;
import com.example.ecommerce.domain.article.service.ArticleService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/articles")
    public ResponseEntity createArticleList() {

        System.out.println("***** LocalDateTime : " + LocalDateTime.now());
        System.out.println("***** ZonedDateTime : " + ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC")));
        articleService.createArticleList();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/articles/v1")
    public ResponseEntity<List<ArticleReadResponseDto>> getArticleListV1(@ModelAttribute ArticleReadRequestDtoV1 readRequestDto) {

        List<ArticleReadResponseDto> articleReadResponseDtoList = articleService.getArticleListV1(readRequestDto.getStartDateTime(),
                                                                                     readRequestDto.getEndDateTime());

        return ResponseEntity.ok(articleReadResponseDtoList);
    }

    @GetMapping("/articles/v2")
    public ResponseEntity<List<ArticleReadResponseDto>> getArticleListV2(@ModelAttribute ArticleReadRequestDtoV2 readRequestDto) {

        List<ArticleReadResponseDto> articleReadResponseDtoList = articleService.getArticleListV2(readRequestDto.getStartDateTime(), readRequestDto.getEndDateTime());
        return ResponseEntity.ok(articleReadResponseDtoList);
    }

    @GetMapping("/articles/v3")
    public ResponseEntity<List<ArticleReadResponseDto>> getArticleListV3(@ModelAttribute ArticleReadRequestDtoV3 readRequestDto) {

        List<ArticleReadResponseDto> articleReadResponseDtoList = articleService.getArticleListV3(readRequestDto.getStartDateTime(),
                                                                                                  readRequestDto.getEndDateTime());

        return ResponseEntity.ok(articleReadResponseDtoList);
    }

    @GetMapping("/articles/v4")
    public ResponseEntity<List<ArticleReadResponseDto>> getArticleListV4(@ModelAttribute ArticleReadRequestDtoV4 readRequestDto) {

        List<ArticleReadResponseDto> articleReadResponseDtoList = articleService.getArticleListV4(readRequestDto.getStartDateTime(), readRequestDto.getEndDateTime());

        return ResponseEntity.ok(articleReadResponseDtoList);
    }
}
