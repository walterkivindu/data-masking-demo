package com.walter.datamaskingdemo.client;

import com.walter.datamaskingdemo.api.Album;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface AlbumsClient {

    @GetExchange("/albums")
    List<Album> getAllAlbums();

    @GetExchange("/albums/{id}")
    Album getAlbumById(String id);

    @GetExchange("/users/{userId}/albums")
    List<Album> getAlbumsByUserId(String userId);
}
