package com.rbc.red.api.repository;

import com.rbc.red.api.entity.Team;
import com.rbc.red.api.entity.UserTeam;
import com.rbc.red.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    Optional<UserTeam> findByUserAndTeam(User user, Team team);
}
