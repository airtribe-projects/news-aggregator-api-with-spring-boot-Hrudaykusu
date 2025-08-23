package com.newsaggregator.demo.repository;

import com.newsaggregator.demo.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterLoginRepository extends JpaRepository<UserDetails,Long> {

    UserDetails findByUsername(String username);
}
