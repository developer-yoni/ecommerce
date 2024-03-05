package com.example.ecommerce.domain.article.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleReadRequestDtoV2 {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
