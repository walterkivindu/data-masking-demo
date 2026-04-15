package com.walter.datamaskingdemo.client;

import com.walter.datamaskingdemo.api.Photo;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface PhotosClient {

    @GetExchange("/photos")
    List<Photo> getAllPhotos();

    @GetExchange("/photos/{id}")
    Photo getPhotoById(String id);

    @GetExchange("/albums/{albumId}/photos")
    List<Photo> getPhotosByAlbumId(String albumId);
}
