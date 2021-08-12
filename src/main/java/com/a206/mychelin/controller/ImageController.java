package com.a206.mychelin.controller;

import com.a206.mychelin.util.ImageServer;
import com.a206.mychelin.web.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    final private ImageServer imageServer;

    @PostMapping
    public ResponseEntity<Response> registerImageIntoServer(@RequestParam MultipartFile file) throws IOException {
        return imageServer.registerImageIntoServer(file);
    }
}