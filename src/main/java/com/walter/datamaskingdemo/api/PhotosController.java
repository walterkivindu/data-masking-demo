package com.walter.datamaskingdemo.api;

import com.walter.datamaskingdemo.annotation.masking.ApplyMasking;
import com.walter.datamaskingdemo.service.PhotosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/photos")
public class PhotosController {

    private final PhotosService photosService;

    public PhotosController(PhotosService photosService) {
        this.photosService = photosService;
    }

    @GetMapping
    @ApplyMasking
    public List<Photo> getAllPhotos() {
        return photosService.getAllPhotos();
    }

    @GetMapping("/{id}")
    public Photo getPhotoById(@PathVariable String id) {
        return photosService.getPhotoById(id);
    }

    @GetMapping("/by-album/{albumId}")
    public List<Photo> getPhotosByAlbumId(@PathVariable String albumId) {
        return photosService.getPhotosByAlbumId(albumId);
    }
}
