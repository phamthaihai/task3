package com.test.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.demo.entity.LoginHistory;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

	List<LoginHistory> findByEmailOrderByLoginTimeDesc(String email);

}
