package com.walter.datamaskingdemo.service;

import com.walter.datamaskingdemo.api.Todo;
import com.walter.datamaskingdemo.client.TodosClient;
import org.springframework.stereotype.Service;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Service
public class TodosService {

    private final TodosClient todosClient;

    public TodosService(HttpServiceProxyFactory factory) {
        this.todosClient = factory.createClient(TodosClient.class);
    }

    public List<Todo> getAllTodos() {
        return todosClient.getAllTodos();
    }

    public Todo getTodoById(String id) {
        return todosClient.getTodoById(id);
    }

    public List<Todo> getTodosByUserId(String userId) {
        return todosClient.getTodosByUserId(userId);
    }

    public List<Todo> getTodosByCompleted(boolean completed) {
        return todosClient.getTodosByCompleted(String.valueOf(completed));
    }
}
