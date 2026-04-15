package com.walter.datamaskingdemo.service;

import com.walter.datamaskingdemo.api.Post;
import com.walter.datamaskingdemo.client.PostsClient;
import org.springframework.stereotype.Service;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Service
public class PostsService {

    private final PostsClient postsClient;

    public PostsService(HttpServiceProxyFactory factory) {
        this.postsClient = factory.createClient(PostsClient.class);
    }

    public List<Post> getAllPosts() {
        return postsClient.getAllPosts();
    }

    public Post getPostById(String id) {
        return postsClient.getPostById(id);
    }

    public List<Post> getPostsByUserId(String userId) {
        return postsClient.getPostsByUserId(userId);
    }
}
