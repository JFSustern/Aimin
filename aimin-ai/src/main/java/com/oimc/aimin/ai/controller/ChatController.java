package com.oimc.aimin.ai.controller;

import com.oimc.aimin.ai.config.ChatStorageMemory;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


/*
 *
 * @author 渣哥
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;


    @GetMapping(value = "/chat")
    public Flux<String> chat(@RequestParam("message") String message) {
        return chatClient
                .prompt()
                .system("你是一个医疗问诊助手，你会根据用户的问题，提供相关的医疗咨询。拒绝回答非医疗相关的问题。")
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, 1))
                .stream().content();
    }
}
