package com.walter.datamaskingdemo.service;

import com.walter.datamaskingdemo.api.Album;
import com.walter.datamaskingdemo.client.AlbumsClient;
import org.springframework.stereotype.Service;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Service
public class AlbumsService {

    private final AlbumsClient albumsClient;

    public AlbumsService(HttpServiceProxyFactory factory) {
        this.albumsClient = factory.createClient(AlbumsClient.class);
    }

    public List<Album> getAllAlbums() {
        return albumsClient.getAllAlbums();
    }

    public Album getAlbumById(String id) {
        return albumsClient.getAlbumById(id);
    }

    public List<Album> getAlbumsByUserId(String userId) {
        return albumsClient.getAlbumsByUserId(userId);
    }
}
