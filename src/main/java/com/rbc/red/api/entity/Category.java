package com.rbc.red.api.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private AssetType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    public Category(String name, AssetType type) {
        this.name = name;
        this.type = type;
    }
    public void changeCategory(String name, AssetType type){
        this.name = name;
        this.type = type;
    }

    public void addTeam(Team team){
        this.team = team;
    }
}
