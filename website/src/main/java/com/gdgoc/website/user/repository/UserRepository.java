package com.gdgoc.website.user.repository;

import com.gdgoc.website.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
