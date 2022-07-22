package com.rbc.red.api.controller.team;

import com.rbc.red.api.entity.Team;
import com.rbc.red.api.entity.UserTeam;
import com.rbc.red.api.entity.UserType;
import com.rbc.red.api.entity.user.User;
import com.rbc.red.api.service.TeamService;
import com.rbc.red.api.service.UserService;
import com.rbc.red.api.service.UserTeamService;
import com.rbc.red.common.ApiResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final UserService userService;
    private final UserTeamService userTeamService;

    @GetMapping("/api/v1/teams")
    public ApiResponse teamsV1(){
        User user = getUser();
        List<GetTeamResponse> Teams = user.getUserTeams().stream()
                .map(o -> new GetTeamResponse(o.getTeam().getName(), o.getTeam().getId()))
                .collect(Collectors.toList());
        return ApiResponse.success("Team", Teams);
    }

    @PostMapping("/api/v1/teams")
    public ApiResponse saveTeamV1(@RequestBody @Valid CreateTeamRequest request){
        User user = getUser();
        Team team = teamService.join(new Team(request.getName()));
        userTeamService.join(user, team, UserType.ONER);
        return ApiResponse.success("Team", team.getId());
    }

    @PutMapping("/api/v1/teams/{id}")
    public ApiResponse updateTeamV1(@PathVariable("id") Long id, @RequestBody @Valid UpdateTeamRequest request){
        Team findTeam = teamService.findOne(id);
        findTeam.changeName(request.getName());
        return ApiResponse.success("Team", findTeam.getName());
    }

    @PutMapping("/api/v1/team/{id}/invite")
    public ApiResponse inviteTeamV1(@PathVariable("id") Long id,
                                    @RequestBody @Valid InviteTeamRequest request){
        //권한 체크
        User loginUser = getUser();
        UserTeam userTeam = userTeamService.findUserTeam(loginUser, id);
        //로직
        User user = userService.getUserBySeq(request.getUserSeq());
        Team team = userTeam.getTeam();
        UserTeam newUserTeam = userTeamService.join(user, team, UserType.USER);
        return ApiResponse.success("userTeam", newUserTeam.getUserTeamId());
    }

    @DeleteMapping("/api/v1/team/{id}")
    public ApiResponse deleteTeamV1(@PathVariable("id") Long id){
        //권한 체크
        User loginUser = getUser();
        UserTeam userTeam = userTeamService.findTeamUser(teamService.findOne(id), loginUser.getUserSeq());
        if(userTeam.getUserType() != UserType.ONER)
            return ApiResponse.fail();

        //todo 서비스 내부 로직
        teamService.delete(userTeam.getTeam());
        return ApiResponse.success("Team", "Team deleted.");
    }

    private User getUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        User user = userService.getUser(principal.getUsername());
        return user;
    }
    @Data
    static class GetTeamResponse{
        private String name;
        private Long id;

        public GetTeamResponse(String name, Long id) {
            this.name = name;
            this.id = id;
        }
    }
    @Data
    static class CreateTeamRequest {
        private String name;
    }

    @Data
    static class UpdateTeamRequest {
        private String name;
    }
    @Data
    static class InviteTeamRequest{
        private Long userSeq;
    }
}

