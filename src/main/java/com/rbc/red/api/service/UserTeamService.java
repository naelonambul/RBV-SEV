package com.rbc.red.api.service;

import com.rbc.red.api.entity.Team;
import com.rbc.red.api.entity.UserTeam;
import com.rbc.red.api.entity.UserType;
import com.rbc.red.api.entity.user.User;
import com.rbc.red.api.exeption.NotExistTeamException;
import com.rbc.red.api.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserTeamService {
    private final UserTeamRepository userTeamRepository;

    @Transactional
    public UserTeam join(UserTeam userTeam){
        if(userTeam.getUserTeamId() != null)
            validateDuplicateTeamMember(userTeam);
        UserTeam result = userTeamRepository.saveAndFlush(userTeam);
        return result;
    }

    @Transactional
    public UserTeam join(User user, Team team, UserType userType){
        validateDuplicateUserTeam(team, user);
        UserTeam userTeam = UserTeam.createUserTeam(user, team, userType);
        return userTeamRepository.saveAndFlush(userTeam);
    }

    private void validateDuplicateUserTeam(Team team, User user){
        userTeamRepository.findByUserAndTeam(user, team)
                .ifPresent( m -> {
                    throw new NotExistTeamException("Member already exist.");
                });
    }

    private void validateDuplicateTeamMember(UserTeam userTeam) {
        userTeamRepository.findById(userTeam.getUserTeamId())
                .ifPresent(m -> {
                    throw new NotExistTeamException("Member already exist.");
                });
    }
    /**
     * 팀 조회(팀)
     */
    public UserTeam findTeamUser(Team team, Long userSeq) {
        return team.getUserTeams().stream()
                .filter(m -> m.getUser().getUserSeq() == userSeq)
                .findFirst()
                .orElseThrow(() -> new NotExistTeamException("Fail to Find Team"));
    }

    /**
     * 팀 조회(유저)
     */
    public UserTeam findUserTeam(User user, Long teamId){
        return user.getUserTeams().stream()
                .filter(m -> m.getTeam().getId() == teamId)
                .findFirst()
                .orElseThrow(() -> new NotExistTeamException("Fail to Find Team"));
    }
    private void deleteUserTeam(UserTeam userTeam){
        userTeamRepository.deleteById(userTeam.getUserTeamId());
    }
}
