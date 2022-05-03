package com.rbc.red.api.service;

import com.rbc.red.api.entity.Team;
import com.rbc.red.api.entity.UserTeam;
import com.rbc.red.api.entity.user.User;
import com.rbc.red.api.exeption.NotExistUserTeamException;
import com.rbc.red.api.repository.UserTeamRepository;
import com.rbc.red.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    public User getUser(String userId) {
        return userRepository.findServiceByUserId(userId).orElseThrow(() -> new NotExistUserTeamException("Fail to Find User"));
    }

    public User getUserBySeq(Long userSeq){
        return userRepository.findByUserSeq(userSeq).orElseThrow(() -> new NotExistUserTeamException("Fail to Find User"));
    }
    @Transactional
    public void deleteUser(String userId) {
        User findUser = userRepository.findByUserId(userId);
        if(findUser != null)
            userRepository.delete(findUser);
    }

    @Transactional
    public UserTeam addUserTeam(UserTeam userTeam){
        UserTeam result = userTeamRepository.saveAndFlush(userTeam);
        return result;
    }

    @Transactional
    public void removeUserTeam(Team team, User user){
        Optional<UserTeam> findUserTeam = userTeamRepository.findByUserAndTeam(user, team);
        UserTeam userTeam = findUserTeam.orElseThrow(() -> new NotExistUserTeamException("Fail to Find UserTeam"));
        user.deleteUserTeam(userTeam);
        team.deleteUserTeam(userTeam);
        userTeamRepository.delete(userTeam);
    }

}
