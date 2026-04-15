package com.walter.datamaskingdemo.api;

import com.walter.datamaskingdemo.service.AlbumsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumsController {

    private final AlbumsService albumsService;

    public AlbumsController(AlbumsService albumsService) {
        this.albumsService = albumsService;
    }

    @GetMapping
    public List<Album> getAllAlbums() {
        return albumsService.getAllAlbums();
    }

    @GetMapping("/{id}")
    public Album getAlbumById(@PathVariable String id) {
        return albumsService.getAlbumById(id);
    }

    @GetMapping("/by-user/{userId}")
    public List<Album> getAlbumsByUserId(@PathVariable String userId) {
        return albumsService.getAlbumsByUserId(userId);
    }
}
