package dev.mednikov.taskpal.roles.controllers;

import dev.mednikov.taskpal.roles.domain.RoleDto;
import dev.mednikov.taskpal.roles.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleRestController {

    private final RoleService roleService;

    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody RoleDto createRole (@RequestBody RoleDto body){
        return this.roleService.createRole(body);
    }

    @PutMapping("/update")
    public @ResponseBody RoleDto updateRole (@RequestBody RoleDto body){
        return this.roleService.updateRole(body);
    }

    @DeleteMapping("/delete/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole (@PathVariable Long roleId){
        this.roleService.deleteRole(roleId);
    }

    @GetMapping("/workspace/{workspaceId}")
    public @ResponseBody List<RoleDto> getRoles (@PathVariable Long workspaceId){
        return this.roleService.getRoles(workspaceId);
    }

}
