package com.walter.datamaskingdemo.api;

import com.walter.datamaskingdemo.service.CommentsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping
    public List<Comment> getAllComments() {
        return commentsService.getAllComments();
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable String id) {
        return commentsService.getCommentById(id);
    }

    @GetMapping("/by-post/{postId}")
    public List<Comment> getCommentsByPostId(@PathVariable String postId) {
        return commentsService.getCommentsByPostId(postId);
    }

    @GetMapping(params = "postId")
    public List<Comment> getCommentsByPostIdParam(@RequestParam String postId) {
        return commentsService.getCommentsByPostIdParam(postId);
    }
}
