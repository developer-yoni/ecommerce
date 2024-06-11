package com.example.ecommerce.global.util;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class SlackUtil {

    @Value("${slack.webhook.url}")
    private static String SLACK_WEBHOOK_URL;

    private static final Slack slackClient = Slack.getInstance();

    /**
     * [슬랙 메시지 전송]
     * */
    public static void sendMessage(String title, String message) {

        Payload payload = Payload.builder()
            .text("*[" + title + "]*\n" + message)
            .build();

        WebhookResponse webhookResponse = null;
        try {

            webhookResponse = slackClient.send("https://hooks.slack.com/services/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", payload);
            log.info("Slack Response : {}", webhookResponse);
        } catch (IOException e) {

            log.error("Error sending Slack message", e);
        }
    }
}
