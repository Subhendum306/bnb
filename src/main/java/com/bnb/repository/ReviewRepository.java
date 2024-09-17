package com.bnb.repository;

import com.bnb.entity.AppUser;
import com.bnb.entity.Property;
import com.bnb.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("Select r from Review r where r.appUser=:user and r.property=:property ")
    Review findByUserAndProperty(@Param("user")AppUser user, @Param("property")Property property);

    @Query("Select r from Review r where r.appUser=:user")
    List<Review> findReviewsByUser(@Param("user") AppUser user);
}