package com.rbc.red.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbc.red.api.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_team")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTeam {
    @JsonIgnore    @Id
    @Column(name = "user_team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTeamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_seq")
    private User user;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    //==생성 메서드==//
    public static UserTeam createUserTeam(User user, UserType userType){
        UserTeam userteam = new UserTeam();
        userteam.setUser(user);
        userteam.setUserType(userType);
        user.addUserTeam(userteam);
        return userteam;
    }

    private UserTeam(User user, Team team, UserType userType) {
        this.setTeam(team);
        this.setUser(user);
        this.setUserType(userType);
    }

    public static UserTeam createUserTeam(User user, Team team, UserType userType) {
        return new UserTeam(user, team, userType);
    }
    public void setUserTeamId(Long userTeamId) {
        this.userTeamId = userTeamId;
    }
    private void setTeam(Team team) {
        this.team = team;
        team.addUserTeam(this);
    }

    private void setUser(User user) {
        this.user = user;
        user.addUserTeam(this);
    }

    private void setUserType(UserType userType) {
        this.userType = userType;
    }
}
