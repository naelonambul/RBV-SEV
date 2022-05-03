package com.rbc.red.api.service;

import com.rbc.red.api.entity.*;
import com.rbc.red.api.entity.user.User;
import com.rbc.red.oauth.entity.ProviderType;
import com.rbc.red.oauth.entity.RoleType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static com.rbc.red.api.entity.UserTeam.createUserTeam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookServiceTest {
    @Autowired BookService bookService;
    @Autowired UserService userService;
    @Autowired TeamService teamService;
    @Autowired UserTeamService userTeamService;
    @Autowired EntityManager em;


    @BeforeEach
    public void beforeEach(){
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

        em.persist(user1);
        em.persist(user2);
        Team teamZ = new Team("teamZ");
        Team joinedTeam = teamService.join(teamZ);
        userTeamService.join(user1, joinedTeam, UserType.ONER);
        userTeamService.join(user2, joinedTeam, UserType.USER);
    }

    @AfterEach
    public void afterEach(){

    }

    @Test
    @Commit
    public void 그룹입력삭제() throws Exception{
        //given
        Team teamA = teamService.findByName("teamZ").get();
        teamA.addGroup(new Group("현금", GroupType.CASH)); //
        teamA.addGroup(new Group("카드", GroupType.CRADIT));
        teamA.addGroup(new Group("은행", GroupType.CASH));
        teamA.getGroups().get(0).addAsset(new Asset("지갑")); //
        teamA.getGroups().get(1).addAsset(new Asset("현대"));
        teamA.getGroups().get(2).addAsset(new Asset("국민"));

        teamA.addCategory(new Category("이체", AssetType.TRANSFER)); //
        teamA.addCategory(new Category("월급", AssetType.INCOME));
        teamA.addCategory(new Category("부업", AssetType.INCOME));
        teamA.addCategory(new Category("통신비", AssetType.EXPENSE));
        teamA.addCategory(new Category("식비", AssetType.EXPENSE));
        teamA.addCategory(new Category("월세", AssetType.EXPENSE));
        em.flush();
        em.clear();
        
        //when
        Team teamC = teamService.findByName("teamZ").get();
        Group groupC = teamC.getGroups().get(1);
        Long aId = groupC.getAssets().get(0).getId();
        bookService.deleteAsset(groupC, aId);
        em.flush();
        em.clear();

        Team teamB = teamService.findByName("teamZ").get();
        Long gId = teamB.getGroups().get(0).getId();
        bookService.deleteGroup(teamB, gId);
        em.flush();
        em.clear();

        Team teamD = teamService.findByName("teamZ").get();
        Long cId = teamD.getCategories().get(0).getId();
        bookService.deleteCategory(teamD, cId);
        em.flush();
        em.clear();

        //then
        //조회코드
        Team teamE = teamService.findByName("teamZ").get();

        assertThat(teamE.getGroups().get(1).getAssets().size()).isEqualTo(1);
        assertThat(teamE.getGroups().size()).isEqualTo(2);
        assertThat(teamE.getCategories().size()).isEqualTo(5);
    }
    @Test
    public void 가계부입력() throws Exception{
        //given
        Team teamA = teamService.findByName("teamZ").get();
        teamA.addGroup(new Group("현금", GroupType.CASH));
        teamA.addGroup(new Group("카드", GroupType.CRADIT));
        teamA.addGroup(new Group("은행", GroupType.CASH));

        teamA.getGroups().get(0).addAsset(new Asset("지갑"));
        teamA.getGroups().get(1).addAsset(new Asset("현대"));
        teamA.getGroups().get(2).addAsset(new Asset("국민"));

        teamA.addCategory(new Category("이체", AssetType.TRANSFER));
        teamA.addCategory(new Category("월급", AssetType.INCOME));
        teamA.addCategory(new Category("부업", AssetType.INCOME));
        teamA.addCategory(new Category("통신비", AssetType.EXPENSE));
        teamA.addCategory(new Category("식비", AssetType.EXPENSE));
        teamA.addCategory(new Category("월세", AssetType.EXPENSE));

        em.flush();
        em.clear();
        //when
        Team teamB = teamService.findByName("teamZ").get();
        User userC = teamB.getUserTeams().get(0).getUser();
        Asset cash = teamB.getGroups().get(0).getAssets().get(0);
        Category monthPay = teamB.getCategories().get(0);
        Book bookC = new Book(
                monthPay,
                cash,
                cash,
                2000000L,
                LocalDateTime.now(),
                " ",
                " ",
                teamB,
                userC
        );
        teamB.addBook(bookC);
        em.flush();
        em.clear();
        Team teamD = teamService.findByName("teamZ").get();
        List<Book> bookCList = teamD.getBooks();
        Book bookD = bookCList.get(0);
        //Todo
        //asset, category에서 찾아서 삭제 먼저해야됨.
        bookD.setNull();
        bookCList.removeIf(m->m.getId() == bookD.getId());
        em.flush();
        em.clear();

        //then
        Team teamE = teamService.findByName("teamZ").get();
        List<Book> bookEList = teamE.getBooks();
        assertThat(bookEList.size()).isEqualTo(0);
    }


}