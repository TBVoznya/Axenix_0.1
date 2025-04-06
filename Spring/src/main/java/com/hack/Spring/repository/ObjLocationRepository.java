package com.hack.Spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hack.Spring.model.ObjLocation;

import java.util.List;

public interface ObjLocationRepository extends JpaRepository<ObjLocation, Long> {

    @Query(value = "SELECT * FROM obj_location ORDER BY id DESC LIMIT :limit", nativeQuery = true)
    List<ObjLocation> findTopNByOrderByIdDesc(@Param("limit") int limit);
}
