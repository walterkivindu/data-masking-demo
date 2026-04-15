package com.walter.datamaskingdemo.client;

import com.walter.datamaskingdemo.api.Todo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface TodosClient {

    @GetExchange("/todos")
    List<Todo> getAllTodos();

    @GetExchange("/todos/{id}")
    Todo getTodoById(String id);

    @GetExchange("/todos?userId={userId}")
    List<Todo> getTodosByUserId(String userId);

    @GetExchange("/todos")
    List<Todo> getTodosByCompleted(@RequestParam("completed") String completed);
}
