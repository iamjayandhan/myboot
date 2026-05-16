package com.jayandhan.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@RestController
public class PortfolioApplication {

    private final ChatClient chatClient;

    public PortfolioApplication(ChatClient.Builder builder) {
        this.chatClient = builder
            .defaultSystem("You are a helpful assistant named Jay. Be concise and friendly.")
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
    }

    @GetMapping("/")
    public String hello() {
        return "Hello world from Java Spring Boot!";
    }

    @PostMapping("/chat")
    public String chat(
        @RequestParam String message,
        @RequestParam(defaultValue = "default") String sessionId
    ) {
        log.info("Chat request — session: {}, message: {}", sessionId, message);

        long start = System.currentTimeMillis();
        String response = chatClient.prompt()
            .user(message)
            .advisors(a -> a.param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId))
            .call()
            .content();
        long duration = System.currentTimeMillis() - start;

        log.info("Chat response — session: {}, duration: {}ms", sessionId, duration);
        return response;
    }

    @GetMapping("/fact")
    public String fact(
        @RequestParam(defaultValue = "any") String category
    ) {
        log.info("Fact request — category: {}", category);

        String systemPrompt = """
            You are a fun fact generator. Return ONLY a single interesting fact.
            No intro, no explanation, just the fact itself in 1-5 sentences.
            Keep it surprising and engaging.
            """;

        String userPrompt = switch (category.toLowerCase()) {
            case "animal"  -> "Give me a surprising fact about any animal.";
            case "food"    -> "Give me an interesting fact about any food or ingredient.";
            case "health"  -> "Give me a useful or surprising health fact.";
            case "science" -> "Give me a mind-blowing science fact.";
            case "history" -> "Give me a surprising historical fact.";
            default        -> "Give me a random interesting fact about animals, food, health, science, or history.";
        };

        long start = System.currentTimeMillis();
        String fact = chatClient.prompt()
            .system(systemPrompt)
            .user(userPrompt)
            .call()
            .content();
        long duration = System.currentTimeMillis() - start;

        log.info("Fact response — category: {}, duration: {}ms", category, duration);
        return fact;
    }
}