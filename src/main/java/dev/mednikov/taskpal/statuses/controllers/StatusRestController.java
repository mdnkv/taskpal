package dev.mednikov.taskpal.statuses.controllers;

import dev.mednikov.taskpal.statuses.domain.StatusDto;
import dev.mednikov.taskpal.statuses.services.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statuses")
public class StatusRestController {

    private final StatusService statusService;

    public StatusRestController(StatusService statusService) {
        this.statusService = statusService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody StatusDto createStatus (@RequestBody StatusDto statusDto){
        return this.statusService.createStatus(statusDto);
    }

    @PutMapping("/update")
    public @ResponseBody StatusDto updateStatus (@RequestBody StatusDto statusDto){
        return this.statusService.updateStatus(statusDto);
    }

    @DeleteMapping("/delete/{statusId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStatus (@PathVariable Long statusId){
        this.statusService.deleteStatus(statusId);
    }

    @GetMapping("/workspace/{workspaceId}")
    public @ResponseBody List<StatusDto> getStatuses (@PathVariable Long workspaceId){
        return this.statusService.getStatuses(workspaceId);
    }

}
