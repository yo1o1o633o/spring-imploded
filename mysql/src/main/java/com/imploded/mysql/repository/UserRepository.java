package com.imploded.mysql.repository;

import com.imploded.mysql.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shuai.yang
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}
