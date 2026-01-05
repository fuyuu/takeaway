package com.hope.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/chat")
public class SmartChatController {

    private final ChatClient chatClient;
    private final Map<String, String> scenePrompts = Map.of(
            "campus", "你是jsdx大学的学长，回答{question}",
            "food", "你是外卖助手，查询订单{orderId}的状态"
    );

    @GetMapping(value = "/scene", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithScene(
            @RequestParam String prompt,
            @RequestParam String chatId,
            @RequestParam String scene) {

        // 动态选择提示词模板并填充参数
        String systemPrompt = scenePrompts.getOrDefault(scene,
                "你是通用助手");

        return chatClient
                .prompt()
                .system(s -> s.text(systemPrompt))
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }
}
