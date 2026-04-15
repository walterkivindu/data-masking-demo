package com.walter.datamaskingdemo.service;

import com.walter.datamaskingdemo.api.Photo;
import com.walter.datamaskingdemo.client.PhotosClient;
import org.springframework.stereotype.Service;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Service
public class PhotosService {

    private final PhotosClient photosClient;

    public PhotosService(HttpServiceProxyFactory factory) {
        this.photosClient = factory.createClient(PhotosClient.class);
    }

    public List<Photo> getAllPhotos() {
        return photosClient.getAllPhotos();
    }

    public Photo getPhotoById(String id) {
        return photosClient.getPhotoById(id);
    }

    public List<Photo> getPhotosByAlbumId(String albumId) {
        return photosClient.getPhotosByAlbumId(albumId);
    }
}
