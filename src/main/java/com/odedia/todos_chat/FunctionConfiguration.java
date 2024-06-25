package com.odedia.todos_chat;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
class FunctionConfiguration {

  @Bean
  @Description("Get all the todo items")
  public Function<TodosRequest, TodosResponse> fetchAllTodos(TodosProvider todosProvider) {
    return request -> {
		try {
			return todosProvider.getAllTodos();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	};
  }

  @Bean
  @Description("Mark todo as complete")
  public Function<TodosRequest, TodoResponse> markTodoAsComplete(TodosProvider todosProvider) {
	return request -> {
		return todosProvider.markTodoAsComplete(request.todo());
	};
  }
  
  @Bean
  @Description("Delete todo item")
  public Function<TodosRequest, DeletedTodoResponse> deleteTodo(TodosProvider todosProvider) {
	return request -> {
		return todosProvider.deleteTodo(request.todo());
	};
  }
  
  
}

record TodosRequest (Todo todo) {};
record TodosResponse(List<Todo> todos) {};
record TodoResponse(Todo todo) {};
record DeletedTodoResponse(Long id) {};
