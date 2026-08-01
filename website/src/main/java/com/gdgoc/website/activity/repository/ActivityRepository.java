package com.gdgoc.website.activity.repository;

import com.gdgoc.website.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByGenerationOrderByQuarterAscIdAsc(Integer generation);
}
