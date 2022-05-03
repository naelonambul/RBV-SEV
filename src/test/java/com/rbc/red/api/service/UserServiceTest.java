package com.rbc.red.api.service;

import com.rbc.red.api.entity.user.User;
import com.rbc.red.api.repository.user.UserRepository;
import com.rbc.red.oauth.entity.ProviderType;
import com.rbc.red.oauth.entity.RoleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
class UserServiceTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void beforeEach(){

    }

    @AfterEach
    public void afterEach(){

    }

    @Test
    @Transactional
    public void 회원가입() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
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

        //when
        userRepository.saveAndFlush(user);

        User byUserId = userRepository.findByUserId(user.getUserId());
        //then
        assertThat(byUserId.getUserId()).isEqualTo(user.getUserId());
    }
    
    @Test
    @Transactional
    public void 회원정보수정() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
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
        userRepository.saveAndFlush(user);
        User findUser = userRepository.findByUserId(user.getUserId());
        assertThat(findUser.getUserId()).isEqualTo(user.getUserId());

        //when
        findUser.changeUsername("steelblack1");
        findUser.changeRoleType("GUEST");
        em.flush();
        em.clear();

        //then
        assertThat(findUser.getUsername()).isEqualTo("steelblack1");
    }

    @Test
    @Transactional
    public void 회원삭제() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
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
        userRepository.saveAndFlush(user);
        User findUser = userRepository.findByUserId(user.getUserId());
        assertThat(findUser.getUserId()).isEqualTo(user.getUserId());

        //when
        userRepository.delete(findUser);

        em.flush();
        em.clear();

        User findUser2 = userRepository.findByUserId(user.getUserId());
        //then
        if(findUser2 != null)
            fail("예외가 발생해야 합니다.");
        //성공
    }
}