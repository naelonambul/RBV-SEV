package com.rbc.red.api.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name="GROUPS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id")
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private GroupType type;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = ALL, orphanRemoval = true)
    private List<Asset> assets = new ArrayList<>();


    public Group(String name, GroupType type) {
        this.name = name;
        this.type = type;
    }

    public void changeTeam(Team team){
        this.team = team;
    }
    public void changeName(String name) {this.name = name;}
    public void addAsset(Asset asset){
        this.assets.add(asset);
        asset.changeGroup(this);
    }
    public void removeAsset(Long assetId){
        this.assets.removeIf(m->m.getId() == assetId);
    }
}
