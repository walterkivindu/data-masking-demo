package com.walter.datamaskingdemo.client;

import com.walter.datamaskingdemo.api.Comment;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

public interface CommentsClient {

    @GetExchange("/comments")
    List<Comment> getAllComments();

    @GetExchange("/comments/{id}")
    Comment getCommentById(String id);

    @GetExchange("/posts/{postId}/comments")
    List<Comment> getCommentsByPostId(String postId);

    @GetExchange("/comments?postId={postId}")
    List<Comment> getCommentsByPostIdParam(String postId);
}
