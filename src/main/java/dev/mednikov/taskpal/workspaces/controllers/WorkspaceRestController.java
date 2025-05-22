package dev.mednikov.taskpal.workspaces.controllers;

import dev.mednikov.taskpal.users.models.User;
import dev.mednikov.taskpal.users.services.UserService;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceDto;
import dev.mednikov.taskpal.workspaces.services.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceRestController {

    private final WorkspaceService workspaceService;
    private final UserService userService;

    public WorkspaceRestController(WorkspaceService workspaceService, UserService userService) {
        this.workspaceService = workspaceService;
        this.userService = userService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody WorkspaceDto createWorkspace(@RequestBody WorkspaceDto body, @AuthenticationPrincipal Jwt jwt) {
        User user = this.userService.getOrCreateUser(jwt);
        return this.workspaceService.createWorkspace(body);
    }

    @PutMapping("/update")
    public @ResponseBody WorkspaceDto updateWorkspace(@RequestBody WorkspaceDto body) {
        return this.workspaceService.updateWorkspace(body);
    }

    @DeleteMapping("/delete/{workspaceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkspace(@PathVariable Long workspaceId) {
        this.workspaceService.deleteWorkspace(workspaceId);
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<WorkspaceDto> getWorkspace(@PathVariable Long workspaceId) {
        Optional<WorkspaceDto> result = this.workspaceService.getWorkspace(workspaceId);
        return ResponseEntity.of(result);
    }

}
