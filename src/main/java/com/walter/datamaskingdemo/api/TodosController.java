package com.walter.datamaskingdemo.api;

import com.walter.datamaskingdemo.service.TodosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodosController {

    private final TodosService todosService;

    public TodosController(TodosService todosService) {
        this.todosService = todosService;
    }


    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable String id) {
        return todosService.getTodoById(id);
    }

    @GetMapping("/by-user/{userId}")
    public List<Todo> getTodosByUserId(@PathVariable String userId) {
        return todosService.getTodosByUserId(userId);
    }

    @GetMapping
    public List<Todo> getTodosByCompleted(@RequestParam(required = false) Boolean completed) {
        if (completed == null) {
            return todosService.getAllTodos();
        }
        return todosService.getTodosByCompleted(completed);
    }
}
