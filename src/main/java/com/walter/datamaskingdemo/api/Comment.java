package com.walter.datamaskingdemo.api;

public record Comment(String postId, String id, String name, String email, String body) {
}
