package com.jayandhan.portfolio.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AiController {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public AiController(ChatClient chatClient, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
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
            .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
            .advisors(a -> a.param("chat_memory_conversation_id", sessionId))
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
            No intro, no explanation, just the fact itself in 1-2 sentences.
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

        // Inject true randomness from Java so the AI gets a unique prompt every time
        String randomSeed = java.util.UUID.randomUUID().toString();
        userPrompt += "\n(Randomization seed: " + randomSeed + " - ensure you provide a completely unique fact different from previous ones).";

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
