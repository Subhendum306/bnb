package com.bnb.controller;

import com.bnb.entity.AppUser;
import com.bnb.entity.Property;
import com.bnb.entity.Review;
import com.bnb.repository.PropertyRepository;
import com.bnb.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    public ReviewController(ReviewRepository reviewRepository, PropertyRepository propertyRepository) {
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
    }

    @RequestMapping("/createReview")
    public ResponseEntity<?> createReview(@RequestBody Review review,
                                               @AuthenticationPrincipal AppUser user,
                                               @RequestParam long propertyId){
        Property property = propertyRepository.findById(propertyId).get();
        Review reviewDetails = reviewRepository.findByUserAndProperty(user, property);
        if(reviewDetails!=null){
             return new ResponseEntity<>("Review Exists",HttpStatus.CREATED);
        }
        review.setAppUser(user);
        review.setProperty(property);
        Review savedReview = reviewRepository.save(review);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }
    @GetMapping("/userReviews")
    public ResponseEntity<List<Review>>  listReviewsOfUser( @AuthenticationPrincipal AppUser user){
        List<Review> reviews = reviewRepository.findReviewsByUser(user);
        return new ResponseEntity<>(reviews,HttpStatus.OK);

    }
}
