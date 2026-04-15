package com.walter.datamaskingdemo.service;

import com.walter.datamaskingdemo.api.Comment;
import com.walter.datamaskingdemo.client.CommentsClient;
import org.springframework.stereotype.Service;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Service
public class CommentsService {

    private final CommentsClient commentsClient;

    public CommentsService(HttpServiceProxyFactory factory) {
        this.commentsClient = factory.createClient(CommentsClient.class);
    }

    public List<Comment> getAllComments() {
        return commentsClient.getAllComments();
    }

    public Comment getCommentById(String id) {
        return commentsClient.getCommentById(id);
    }

    public List<Comment> getCommentsByPostId(String postId) {
        return commentsClient.getCommentsByPostId(postId);
    }

    public List<Comment> getCommentsByPostIdParam(String postId) {
        return commentsClient.getCommentsByPostIdParam(postId);
    }
}
