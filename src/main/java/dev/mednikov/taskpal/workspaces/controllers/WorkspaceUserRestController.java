package dev.mednikov.taskpal.workspaces.controllers;

import dev.mednikov.taskpal.users.models.User;
import dev.mednikov.taskpal.users.services.UserService;
import dev.mednikov.taskpal.workspaces.domain.CreateWorkspaceUserRequestDto;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceUserDto;
import dev.mednikov.taskpal.workspaces.services.WorkspaceUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workspaces/users")
public class WorkspaceUserRestController {

    private final WorkspaceUserService workspaceUserService;
    private final UserService userService;

    public WorkspaceUserRestController(WorkspaceUserService workspaceUserService, UserService userService) {
        this.workspaceUserService = workspaceUserService;
        this.userService = userService;
    }

    @GetMapping("/active")
    public ResponseEntity<WorkspaceUserDto> getActiveWorkspace (@AuthenticationPrincipal Jwt jwt){
        User user = this.userService.getOrCreateUser(jwt);
        Optional<WorkspaceUserDto> result = this.workspaceUserService.getActiveWorkspace(user);
        return ResponseEntity.of(result);
    }

    @PostMapping("/active/{workspaceId}")
    public WorkspaceUserDto setActiveWorkspace (@AuthenticationPrincipal Jwt jwt, @PathVariable Long workspaceId){
        User user = this.userService.getOrCreateUser(jwt);
        return this.workspaceUserService.setActiveWorkspace(user, workspaceId);
    }

    @GetMapping("/my")
    public List<WorkspaceUserDto> getUserWorkspaces (@AuthenticationPrincipal Jwt jwt){
        User user = this.userService.getOrCreateUser(jwt);
        return this.workspaceUserService.getUserWorkspaces(user);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody WorkspaceUserDto createWorkspaceUser (@RequestBody CreateWorkspaceUserRequestDto body){
        return this.workspaceUserService.createWorkspaceUser(body);
    }

}
