package com.rbc.red.api.repository;

import com.rbc.red.api.entity.Book;
import com.rbc.red.api.entity.Team;
import com.rbc.red.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    List<Book> findByTeamAndUser(Team team, User user);
}
