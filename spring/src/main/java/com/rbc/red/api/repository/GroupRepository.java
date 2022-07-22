package com.rbc.red.api.repository;

import com.rbc.red.api.entity.Group;
import com.rbc.red.api.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name);
    List<Group> findByTeam(Team team);
}
