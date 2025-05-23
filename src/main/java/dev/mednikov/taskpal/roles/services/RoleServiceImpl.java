package dev.mednikov.taskpal.roles.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.roles.domain.RoleDto;
import dev.mednikov.taskpal.roles.domain.RoleDtoMapper;
import dev.mednikov.taskpal.roles.exceptions.RoleAlreadyExistsException;
import dev.mednikov.taskpal.roles.exceptions.RoleNotFoundException;
import dev.mednikov.taskpal.roles.models.Role;
import dev.mednikov.taskpal.roles.repositories.RoleRepository;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RoleServiceImpl implements RoleService {

    private final static RoleDtoMapper roleDtoMapper = new RoleDtoMapper();
    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final RoleRepository roleRepository;
    private final WorkspaceRepository workspaceRepository;

    public RoleServiceImpl(RoleRepository roleRepository, WorkspaceRepository workspaceRepository) {
        this.roleRepository = roleRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Long workspaceId = Long.parseLong(roleDto.getWorkspaceId());
        // verify that the role does not exist in the workspace
        String name = roleDto.getName();
        if (this.roleRepository.findByWorkspaceIdAndName(workspaceId, name).isPresent()){
            throw new RoleAlreadyExistsException();
        }

        Workspace workspace = this.workspaceRepository.getReferenceById(workspaceId);
        Role role = new Role();
        role.setId(snowflakeGenerator.next());
        role.setName(name);
        role.setAdministrator(roleDto.isAdministrator());
        role.setWorkspace(workspace);

        Role result = this.roleRepository.save(role);
        return roleDtoMapper.apply(result);
    }

    @Override
    public RoleDto updateRole(RoleDto roleDto) {
        Objects.requireNonNull(roleDto.getId());
        Long id = Long.parseLong(roleDto.getId());
        Role role = this.roleRepository.findById(id).orElseThrow(RoleNotFoundException::new);
        role.setName(roleDto.getName());
        role.setAdministrator(roleDto.isAdministrator());

        Role result = this.roleRepository.save(role);
        return roleDtoMapper.apply(result);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.roleRepository.deleteById(roleId);

    }

    @Override
    public List<RoleDto> getRoles(Long workspaceId) {
        return this.roleRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(roleDtoMapper)
                .toList();
    }
}
