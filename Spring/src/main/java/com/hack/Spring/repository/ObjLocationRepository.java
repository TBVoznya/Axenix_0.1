package com.hack.Spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hack.Spring.model.ObjLocation;

@Repository
public interface ObjLocationRepository extends JpaRepository<ObjLocation, Long> {
}
