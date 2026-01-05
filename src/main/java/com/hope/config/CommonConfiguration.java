package com.hope.config;

import com.hope.util.SystemConstants;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 3402
 * 
 */
@Configuration
public class CommonConfiguration {

    @Bean
    public ChatClient chatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)// 选择模型
                .defaultSystem("你是jsdx大学的一名资深老学长，十分熟悉校园，请以该身份的语气和性格回答问题")// 系统设置
                .defaultAdvisors(new SimpleLoggerAdvisor())// 添加日志记录
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())// 添加会话记忆功能
                .build();
    }
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository()) // 设置存储库
                .maxMessages(20) // 记忆窗口大小（保留最近的10条消息）
                .build();
    }
    @Bean
    public ChatClient chatChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model) // 选择模型
                .defaultSystem(SystemConstants.ASSISTANT_SUM) // 系统设置
                .defaultAdvisors(new SimpleLoggerAdvisor()) // 添加日志记录
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()) // 添加会话记忆功能
                .build();
    }

   /* // 也可以直接
    *//*@Bean
    public ChatMemory chatMemory() {
        // 使用 MessageWindowChatMemory 作为默认内存策略（窗口消息保留）
        return MessageWindowChatMemory.builder().build();
    }*/
}