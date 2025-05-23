package dev.mednikov.taskpal.tasks.controllers;

import dev.mednikov.taskpal.tasks.domain.TaskDto;
import dev.mednikov.taskpal.tasks.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody TaskDto createTask (@RequestBody TaskDto taskDto){
        return this.taskService.createTask(taskDto);
    }

    @PutMapping("/update")
    public @ResponseBody TaskDto updateTask (@RequestBody TaskDto taskDto){
        return this.taskService.updateTask(taskDto);
    }

    @DeleteMapping("/delete/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask (@PathVariable Long taskId){
        this.taskService.deleteTask(taskId);
    }

    @GetMapping("/workspace/{workspaceId}")
    public @ResponseBody List<TaskDto> getTasksInWorkspace (@PathVariable Long workspaceId){
        return this.taskService.getTasksInWorkspace(workspaceId);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id){
        Optional<TaskDto> result = this.taskService.getTask(id);
        return ResponseEntity.of(result);
    }

}
