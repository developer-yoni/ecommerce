package com.example.ecommerce.global.util;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SlackAdaptor {

    @Value("${slack.webhook.url}")
    private String SLACK_WEBHOOK_URL;

    private static final Slack slackClient = Slack.getInstance();

    /**
     * [슬랙 메시지 전송]
     * */
    @Async
    public void sendMessage(String title, String message) {

        Payload payload = Payload.builder()
            .text("*[" + title + "]*\n" + message)
            .build();

        try {

            WebhookResponse webhookResponse = slackClient.send(SLACK_WEBHOOK_URL, payload);
            log.info("Slack Response : {}", webhookResponse);
        } catch (IOException e) {

            log.error("Error sending Slack message", e);
        }
    }
}
