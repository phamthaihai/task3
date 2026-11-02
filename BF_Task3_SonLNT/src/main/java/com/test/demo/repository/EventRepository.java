package com.test.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.demo.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findAllByOrderByEventDateAscStartTimeAsc();

}
