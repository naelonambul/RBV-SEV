package com.rbc.red.api.service;

import com.rbc.red.api.entity.*;
import com.rbc.red.api.entity.user.User;
import com.rbc.red.api.exeption.NotExistBookException;
import com.rbc.red.api.exeption.NotExistTeamException;
import com.rbc.red.api.exeption.NotExistUserTeamException;
import com.rbc.red.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final BookRepository bookRepository;
    /**
     *  팀 생성
     */
    @Transactional
    public Team join(Team team){
        validateDuplicateTeam(team);
        Team result = teamRepository.saveAndFlush(team);
        return result;
    }

    @Transactional
    public Team changeName(Long teamId, String name){
        Team team = findOne(teamId);
        team.changeName(name);
        return team;
    }

    /**
     * 팀삭제
     */
    @Transactional
    public void delete(Team team){
        //객체에서 먼저 삭제후
        //코드
        //디비 삭제
        //userTeamRepository.deleteAllById(team.getId());
        //유저팀 다 삭제후
        teamRepository.deleteById(team.getId());
    }
    /**
     * 중복 검색
     */
    private void validateDuplicateTeam(Team team){
        teamRepository.findByName(team.getName())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 팀입니다.");
                });
    }
    /**
     * 전체 팀 조회
     */
    public List<Team> findTeams(){
        return teamRepository.findAll();
    }

    /**
     * 팀 조회
     */

    public Team findOne(Long teamId) { return teamRepository.findById(teamId).orElseThrow(() -> new NotExistTeamException("Fail to Find Team"));}
    public Optional<Team> findByName(String name) { return teamRepository.findByName(name);}

    /**
     * 유저 * 팀 삭제
     */
    @Transactional
    public void removeUserTeam(Team team, User user){
        Optional<UserTeam> findUserTeam = userTeamRepository.findByUserAndTeam(user, team);
        UserTeam userTeam = findUserTeam.orElseThrow(() -> new NotExistUserTeamException("Fail to Find UserTeam"));
        userTeamRepository.delete(userTeam);
        team.deleteUserTeam(userTeam);
    }
}
