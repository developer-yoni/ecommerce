package com.example.ecommerce.domain.article.dto.request;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class ArticleReadRequestDtoV2 {

    @DateTimeFormat(pattern = "yyyy-mm-ddThh:mm:ss")
    private Date startDateTime;

    @DateTimeFormat(pattern = "yyyy-mm-ddThh:mm:ss")
    private Date endDateTime;
}
