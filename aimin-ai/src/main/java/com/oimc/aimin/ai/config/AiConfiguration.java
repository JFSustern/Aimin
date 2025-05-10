package com.oimc.aimin.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
 *
 * @author 渣哥
 */
@Configuration
@RequiredArgsConstructor
public class AiConfiguration {

    private final OllamaChatModel ollamaChatModel;

    private final VectorStore vectorStore;

    private final ChatStorageMemory chatMemory;


    @Bean
    public ChatClient chatClient() {
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = new MessageChatMemoryAdvisor(chatMemory);
        return ChatClient.builder(ollamaChatModel)
                .defaultAdvisors(
                        messageChatMemoryAdvisor,
                        new SimpleLoggerAdvisor(),
                        new QuestionAnswerAdvisor(
                                vectorStore,
                                SearchRequest.builder().topK(2).similarityThreshold(1.0).build()
                        )
                )
                .defaultOptions(
                        OllamaOptions.builder()
                                .topP(0.3)
                                .build()
                )
                .build();
    }

}
