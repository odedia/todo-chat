package com.odedia.todos_chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID;
@RestController
@RequestMapping("/")
public class TodosChatClient {

  // ChatModel is the primary interfaces for interacting with an LLM
  // it is a request/response interface that implements the ModelModel
  // interface. Make suer to visit the source code of the ChatModel and
  // checkout the interfaces in the core spring ai package.
  private final ChatClient chatClient;

  public TodosChatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
		// @formatter:off
		this.chatClient = builder
				.defaultSystem("""
			      		You are a friendly AI assistant designed to help with the management of Todo items. A Todo is synonym to a task.
			      		Your job is to answer questions about the existing todos and to perform actions on the user's behalf if needed.
			      		You are required to answer an a professional manner. If you don't know the answer, politely tell the customer 
			      		you don't know the answer, then ask the customer a followup qusetion to try and clarify the question they are asking.
			      		If you do know the answer, provide the answer but do not provide any additional helpful followup questions.
			      		""")
				.defaultAdvisors(
						new MessageChatMemoryAdvisor(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, 10), // CHAT MEMORY
						new LoggingAdvisor()
						)
				.defaultFunctions("fetchAllTodos", "markTodoAsComplete", "deleteTodo") // FUNCTION CALLING

				.build();
  }

  @PostMapping("/chatclient")
  public String exchange(@RequestBody String query) {
	  return
	  this.chatClient
	  .prompt()
      .user(
          u ->
              u.text(query)
              )
      .call()
      .content();
  }
}
