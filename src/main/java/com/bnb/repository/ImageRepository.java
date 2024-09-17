package com.bnb.repository;

import com.bnb.entity.Image;
import com.bnb.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("Select i from Image i where i.property=:id")
    List<Image> findByPropertyId(@Param("id") Property id);
}