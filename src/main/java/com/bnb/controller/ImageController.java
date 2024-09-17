package com.bnb.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import com.bnb.entity.AppUser;
import com.bnb.entity.Image;
import com.bnb.entity.Property;
import com.bnb.repository.ImageRepository;
import com.bnb.repository.PropertyRepository;
import com.bnb.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {
    private ImageRepository imageRepository;
    private PropertyRepository propertyRepository;
    private BucketService bucketService;
    public ImageController(ImageRepository imagesRepository, PropertyRepository propertyRepository, BucketService bucketService) {
        this.imageRepository = imagesRepository;
        this.propertyRepository=propertyRepository;
        this.bucketService = bucketService;
    }

    @PostMapping(path="/upload/file/{bucketName}/property/{propertyId}",consumes= MediaType.MULTIPART_FORM_DATA_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file,
                                        @PathVariable String bucketName,
                                        @PathVariable long propertyId,
                                        @AuthenticationPrincipal AppUser user){
        String imageUrl= bucketService.uploadFile(file,bucketName);
        Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new NotFoundException("Property not found"));

        Image img = new Image();
        img.setUrl(imageUrl);
        img.setProperty(property);

        Image savedImage = imageRepository.save(img);
        return new ResponseEntity<>(savedImage, HttpStatus.OK);
    }
    @GetMapping("/images")
    public ResponseEntity<List<Image>> getAllImages(@RequestParam Property id){
        List<Image> Images = imageRepository.findByPropertyId(id);
        return new ResponseEntity<>(Images,HttpStatus.OK);
    }

}
