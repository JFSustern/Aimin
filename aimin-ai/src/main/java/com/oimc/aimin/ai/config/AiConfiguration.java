package com.oimc.aimin.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
 *
 * @author 渣哥
 */
@Configuration
@RequiredArgsConstructor
public class AiConfiguration {

    private final OpenAiChatModel openAiChatModel;

    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(openAiChatModel).build();
    }

}
