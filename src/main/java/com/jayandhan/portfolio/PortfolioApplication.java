package com.jayandhan.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;

@SpringBootApplication
public class PortfolioApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
    }

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
            .chatMemoryRepository(new InMemoryChatMemoryRepository())
            .build();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
            .defaultSystem("You are a helpful assistant named Jay. Be concise and friendly.")
            .build();
    }
}