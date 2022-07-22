package com.rbc.red.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @NotNull
    @Column(length = 100)
    @Size(max = 100)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "team")
    private List<UserTeam> userTeams = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Group> groups = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    public Team(String name){
        this.name = name;
    }

    public void changeName(String name)
    {
        this.name = name;
    }

    public void addUserTeam(UserTeam userTeam){
        this.userTeams.add(userTeam);
    }

    public void deleteUserTeam(UserTeam userTeam){
        this.userTeams.removeIf(u-> u.getUserTeamId() == userTeam.getUserTeamId());
    }

    public void addCategory(Category category){
        this.categories.add(category);
        category.addTeam(this);
    }
    public void removeCategory(Long categoryId){
        this.categories.removeIf(m -> m.getId() == categoryId);
    }
    public void addBook(Book book){
        this.books.add(book);
        book.addTeam(this);
    }
    public void removeBook(Long bookId){
        this.books.removeIf(m -> m.getId() == bookId);
    }

    public List<Group> addGroup(Group group){
        this.groups.add(group);
        group.changeTeam(this);
        return this.groups;
    }

    public void removeGroup(Long groupId){
        this.groups.removeIf(m->m.getId() == groupId);
    }
}
