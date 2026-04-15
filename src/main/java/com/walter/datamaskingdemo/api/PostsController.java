package com.walter.datamaskingdemo.api;

import com.walter.datamaskingdemo.service.PostsService;
import com.walter.datamaskingdemo.service.CommentsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostsController {

    private final PostsService postsService;
    private final CommentsService commentsService;

    public PostsController(PostsService postsService, CommentsService commentsService) {
        this.postsService = postsService;
        this.commentsService = commentsService;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postsService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable String id) {
        return postsService.getPostById(id);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsByPostId(@PathVariable String id) {
        return commentsService.getCommentsByPostId(id);
    }

    @GetMapping(params = "userId")
    public List<Post> getPostsByUserId(@RequestParam String userId) {
        return postsService.getPostsByUserId(userId);
    }
}
