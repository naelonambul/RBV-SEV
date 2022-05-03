package com.rbc.red.api.repository.user;

import com.rbc.red.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);
    Optional<User> findServiceByUserId(String userId);
    Optional<User> findByUserSeq(Long userSeq);
}
