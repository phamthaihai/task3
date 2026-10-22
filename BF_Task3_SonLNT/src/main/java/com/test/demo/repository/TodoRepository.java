package com.test.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.demo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

	List<Todo> findByUserEmailOrderByCreatedAtAscIdAsc(String userEmail);

	Optional<Todo> findByIdAndUserEmail(Long id, String userEmail);

}
