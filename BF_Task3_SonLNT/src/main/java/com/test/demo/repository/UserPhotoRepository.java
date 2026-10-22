package com.test.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.demo.entity.UserPhoto;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

}
