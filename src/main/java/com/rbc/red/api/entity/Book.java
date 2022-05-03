package com.rbc.red.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbc.red.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "book")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="asset_id")
    private Asset asset;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="transfer_id")
    private Asset transfer;

    private Long price;
    private LocalDateTime dateTime;
    private String memo;
    private String filePath;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_seq")
    private User user;

    public Book(Category category, Asset asset, Asset transfer, Long price, LocalDateTime dateTime, String memo, String filePath, Team team, User user) {
        this.category = category;
        this.asset = asset;
        this.transfer = transfer;
        this.price = price;
        this.dateTime = dateTime;
        this.memo = memo;
        this.filePath = filePath;
        this.team = team;
        this.user = user;
    }

    public void changeBook(Category category, Asset asset, Asset transfer, Long price, String memo, String filePath, Team team, User user) {
        this.category = category;
        this.asset = asset;
        this.transfer = transfer;
        this.price = price;
        this.memo = memo;
        this.filePath = filePath;
        this.team = team;
        this.user = user;
    }
    public void addTeam(Team team){
        this.team = team;
    }

    public void setNull(){
        this.category = null;
        this.asset = null;
        this.transfer = null;
        List<Book> book = this.team.getBooks();
        book.removeIf(m -> getId() == this.getId());
        this.team = null;
        this.user = null;
    }
}
