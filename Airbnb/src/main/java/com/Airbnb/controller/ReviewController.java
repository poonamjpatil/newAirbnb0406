package com.Airbnb.controller;

import com.Airbnb.dto.ReviewDto;
import com.Airbnb.entity.Property;
import com.Airbnb.entity.PropertyUser;
import com.Airbnb.entity.Review;
import com.Airbnb.repository.PropertyRepository;
import com.Airbnb.repository.ReviewRepository;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private ReviewRepository reviewRepository;
    private PropertyRepository propertyRepository;

    public ReviewController(ReviewRepository reviewRepository, PropertyRepository propertyRepository) {
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
    }

    @PostMapping("/addReview/{propertyId}")
    public ResponseEntity<String> addReviews(
            @PathVariable long propertyId,
            @RequestBody Review review,
            @AuthenticationPrincipal PropertyUser user
    ) {
        Optional<Property> onProperty = propertyRepository.findById(propertyId);
        Property property = onProperty.get();
        Review r = reviewRepository.findReviewByUser(property, user);
        if (r != null)
        {
            return new ResponseEntity<>("You have already added a review for this property",HttpStatus.BAD_REQUEST);
        }
        review.setProperty(property);
        review.setPropertyUser(user);
        reviewRepository.save(review);
        return new ResponseEntity<>("Review added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/userReview")
    public ResponseEntity<List<Review>> getUserReviews(@AuthenticationPrincipal PropertyUser user)
    {
        List<Review> reviews = reviewRepository.findByPropertyUser(user);
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }
}






