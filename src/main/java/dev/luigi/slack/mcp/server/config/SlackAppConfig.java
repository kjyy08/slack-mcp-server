package dev.luigi.slack.mcp.server.config;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackAppConfig {
    @Value("${slack.slack-bot-token}")
    private String token;

    @Bean
    public AppConfig appConfig() {
        return AppConfig.builder()
                .singleTeamBotToken(token)
                .build();
    }

    @Bean
    public App slackApp(AppConfig appConfig) {
        return App.builder()
                .appConfig(appConfig)
                .build();
    }
}
