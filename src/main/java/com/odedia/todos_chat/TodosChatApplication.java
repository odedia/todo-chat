package com.odedia.todos_chat;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class TodosChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodosChatApplication.class, args);
	}

	@Bean
	public RestClient restClient() {
		return RestClient.create();
	}
	
	@Bean
	public ChatMemory chatMemory() {
		return new InMemoryChatMemory();
	}
}
