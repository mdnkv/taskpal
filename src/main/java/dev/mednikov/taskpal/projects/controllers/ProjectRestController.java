package dev.mednikov.taskpal.projects.controllers;

import dev.mednikov.taskpal.projects.domain.ProjectDto;
import dev.mednikov.taskpal.projects.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectRestController {

    private final ProjectService projectService;

    public ProjectRestController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ProjectDto createProject (@RequestBody ProjectDto body){
        return this.projectService.createProject(body);
    }

    @PutMapping("/update")
    public @ResponseBody ProjectDto updateProject (@RequestBody ProjectDto body){
        return this.projectService.updateProject(body);
    }

    @DeleteMapping("/delete/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject (@PathVariable Long projectId){
        this.projectService.deleteProject(projectId);
    }

    @GetMapping("/workspace/{workspaceId}")
    public List<ProjectDto> getProjects (@PathVariable Long workspaceId){
        return this.projectService.getProjects(workspaceId);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long id){
        Optional<ProjectDto> result = this.projectService.getProject(id);
        return ResponseEntity.of(result);
    }

}
