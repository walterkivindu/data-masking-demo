package com.walter.datamaskingdemo.client;

import com.walter.datamaskingdemo.api.Post;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface PostsClient {

    @GetExchange("/posts")
    List<Post> getAllPosts();

    @GetExchange("/posts/{id}")
    Post getPostById(String id);

    @GetExchange("/posts?userId={userId}")
    List<Post> getPostsByUserId(String userId);
}
