package com.odedia.todos_chat;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TodosProvider {

	private final RestClient restClient;


	public TodosProvider(RestClient restClient) {
		this.restClient = restClient;
	}

	public TodosResponse getAllTodos() throws IOException {
		String data = restClient.get()
				.uri("http://todo-service:8080/todos")
				.accept(MediaType.APPLICATION_JSON) 
				.retrieve().body(String.class);

		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		JsonNode jsNode = om.readTree(data);
		String test = jsNode.at("/_embedded/todos").toString();

		List<Todo> todos = om.readValue(test, new TypeReference<List<Todo>>() {});

		return new TodosResponse(todos);
	}



	public TodoResponse markTodoAsComplete(Todo todo) {
		Long id = todo.id();
		Todo myTodo = restClient.get()
				.uri("http://todo-service:8080/todos/"+id)
				.accept(MediaType.APPLICATION_JSON) 
				.retrieve().body(Todo.class);

		Todo updatedTodo = restClient.put()
				.uri("http://todo-service:8080/todos/"+id)
				.accept(MediaType.APPLICATION_JSON) 
				.body(new Todo(myTodo.id(), myTodo.title(), true))
				.retrieve()
				.body(Todo.class);
		return new TodoResponse(updatedTodo);
	}
	
	public DeletedTodoResponse deleteTodo(Todo todo) {
		Long id = todo.id();


		restClient.delete()
				.uri("http://todo-service:8080/todos/"+id)
				.accept(MediaType.APPLICATION_JSON) 
				.retrieve()
				.toBodilessEntity();
		return new DeletedTodoResponse(id);
	}

}