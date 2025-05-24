package dev.mednikov.taskpal.priorities.controllers;

import dev.mednikov.taskpal.priorities.domain.PriorityDto;
import dev.mednikov.taskpal.priorities.services.PriorityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priorities")
public class PriorityRestController {

    private final PriorityService priorityService;

    public PriorityRestController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody PriorityDto createPriority (@RequestBody PriorityDto priorityDto){
        return this.priorityService.createPriority(priorityDto);
    }

    @PutMapping("/update")
    public @ResponseBody PriorityDto updatePriority (@RequestBody PriorityDto priorityDto){
        return this.priorityService.updatePriority(priorityDto);
    }

    @DeleteMapping("/delete/{priorityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePriority (@PathVariable Long priorityId){
        this.priorityService.deletePriority(priorityId);
    }

    @GetMapping("/workspace/{workspaceId}")
    public @ResponseBody List<PriorityDto> getPriorities (@PathVariable Long workspaceId){
        return this.priorityService.getPriorities(workspaceId);
    }

}
