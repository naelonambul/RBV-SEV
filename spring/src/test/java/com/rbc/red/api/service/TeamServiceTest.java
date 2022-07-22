package com.rbc.red.api.service;

import com.rbc.red.api.entity.*;
import com.rbc.red.api.entity.user.User;
import com.rbc.red.api.repository.*;
import com.rbc.red.api.repository.user.UserRepository;
import com.rbc.red.oauth.entity.ProviderType;
import com.rbc.red.oauth.entity.RoleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.rbc.red.api.entity.UserTeam.createUserTeam;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamServiceTest {

    @Autowired TeamService teamService;
    @Autowired UserService userService;
    @Autowired UserTeamService userTeamService;
    @Autowired EntityManager em;

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTeamRepository userTeamRepository;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    AssetRepository assetRepository;
    @Autowired GroupRepository groupRepository;
    @BeforeEach
    public void beforeEach(){
        userTeamRepository.deleteAll();
        userRepository.deleteAll();
        teamRepository.deleteAll();
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
        assetRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @AfterEach
    public void afterEach(){

    }

    @Test
    public void 팀생성() throws Exception{
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        //when
        teamService.join(teamA);
        teamService.join(teamB);

        //then
        List<Team> teams = teamService.findTeams();
        assertThat(teams.size()).isEqualTo(2);
    }
    
    @Test
    public void 팀삭제() throws Exception{
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        //when
        teamService.join(teamA);
        teamService.join(teamB);

        teamService.delete(teamA);
        //then
        List<Team> teams = teamService.findTeams();
        assertThat(teams.size()).isEqualTo(1);
    }
    
    @Test
    public void 팀수정() throws Exception{
        //given
        Team teamA = new Team("teamA");

        //when
        teamService.join(teamA);

        teamA.setName("teamAA");
        em.flush();
        em.clear();
        //then
        Optional<Team> findTeam = teamService.findByName("teamAA");
        assertThat(findTeam.get().getName()).isEqualTo("teamAA");
    }

    @Test
    public void 팀원추가() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.now();
        User user1 = new User(
                "1111111",
                "steelblack",
                "steelblack@nate.com",
                "Y",
                "https://imageUrl",
                ProviderType.GOOGLE,
                RoleType.USER,
                now,
                now
        );
        em.persist(user1);
        User user2 = new User(
                "222222",
                "steelblack2",
                "steelblack2@nate.com",
                "Y",
                "https://imageUrl",
                ProviderType.GOOGLE,
                RoleType.USER,
                now,
                now
        );

        em.persist(user2);
        Team teamA = new Team("teamA");
        teamService.join(teamA);

        userTeamService.join(user1, teamA, UserType.ONER);
        userTeamService.join(user2, teamA, UserType.USER);

        em.flush();
        em.clear();

        //when


        //then
        Optional<Team> findTeam = teamService.findByName("teamA");
        findTeam.ifPresent(m->{
            List<UserTeam> findTeams = m.getUserTeams();
            assertThat(findTeams).extracting("userType").containsExactly(UserType.ONER, UserType.USER);
        });
    }
    
    @Test
    public void 팀원삭제() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.now();
        User user1 = new User(
                "1111111",
                "steelblack",
                "steelblack@nate.com",
                "Y",
                "https://imageUrl",
                ProviderType.GOOGLE,
                RoleType.USER,
                now,
                now
        );
        em.persist(user1);
        User user2 = new User(
                "222222",
                "steelblack2",
                "steelblack2@nate.com",
                "Y",
                "https://imageUrl",
                ProviderType.GOOGLE,
                RoleType.USER,
                now,
                now
        );

        em.persist(user2);
        Team teamA = new Team("teamA");
        teamService.join(teamA);

        userTeamService.join(user1, teamA, UserType.ONER);
        userTeamService.join(user2, teamA, UserType.USER);

        em.flush();
        em.clear();
        
        //when
        Team findTeam = teamService.findByName("teamA").get();
        User findUser = userService.getUser("222222");
        userService.removeUserTeam(findTeam, findUser);

        //then
        Team result = teamService.findByName("teamA").get();
        List<UserTeam> userTeam = result.getUserTeams();
        for (UserTeam team : userTeam) {
            System.out.println("teamA - user = " + team.getUser().getUsername());
        }
    }

}