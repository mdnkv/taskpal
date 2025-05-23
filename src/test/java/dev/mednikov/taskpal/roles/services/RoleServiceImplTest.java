package dev.mednikov.taskpal.roles.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.roles.domain.RoleDto;
import dev.mednikov.taskpal.roles.exceptions.RoleAlreadyExistsException;
import dev.mednikov.taskpal.roles.exceptions.RoleNotFoundException;
import dev.mednikov.taskpal.roles.models.Role;
import dev.mednikov.taskpal.roles.repositories.RoleRepository;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Mock private WorkspaceRepository workspaceRepository;
    @Mock private RoleRepository roleRepository;
    @InjectMocks private RoleServiceImpl roleService;

    @Test
    void createRoleAlreadyExistsTest(){
        Long workspaceId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);

        String name = "Administrator";
        boolean administrator = true;

        Role role = new Role();
        role.setId(snowflakeGenerator.next());
        role.setName(name);
        role.setAdministrator(administrator);
        role.setWorkspace(workspace);

        RoleDto payload = new RoleDto();
        payload.setWorkspaceId(workspaceId.toString());
        payload.setId(snowflakeGenerator.next().toString());
        payload.setAdministrator(administrator);
        payload.setName(name);

        Mockito.when(roleRepository.findByWorkspaceIdAndName(workspaceId, name)).thenReturn(Optional.of(role));
        Assertions
                .assertThatThrownBy(() -> roleService.createRole(payload))
                .isInstanceOf(RoleAlreadyExistsException.class);

    }

    @Test
    void createRoleSuccessTest(){
        Long workspaceId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setPersonal(false);
        workspace.setName("Roth Wolf GmbH");

        String name = "Administrator";
        boolean administrator = true;
        Role role = new Role();
        role.setId(snowflakeGenerator.next());
        role.setName(name);
        role.setAdministrator(administrator);
        role.setWorkspace(workspace);

        RoleDto payload = new RoleDto();
        payload.setWorkspaceId(workspaceId.toString());
        payload.setAdministrator(administrator);
        payload.setName(name);

        Mockito.when(roleRepository.findByWorkspaceIdAndName(workspaceId, name)).thenReturn(Optional.empty());
        Mockito.when(workspaceRepository.getReferenceById(workspaceId)).thenReturn(workspace);
        Mockito.when(roleRepository.save(role)).thenReturn(role);

        RoleDto result = roleService.createRole(payload);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void updateRoleSuccessTest(){
        Long workspaceId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setPersonal(false);
        workspace.setName("Braun Lorenz GmbH & Co. KGaA");

        String name = "Administrator";
        boolean administrator = true;

        Long roleId = snowflakeGenerator.next();
        Role role = new Role();
        role.setId(roleId);
        role.setName(name);
        role.setAdministrator(administrator);
        role.setWorkspace(workspace);

        RoleDto payload = new RoleDto();
        payload.setWorkspaceId(workspaceId.toString());
        payload.setId(roleId.toString());
        payload.setAdministrator(administrator);
        payload.setName(name);

        Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.save(role)).thenReturn(role);

        RoleDto result = roleService.updateRole(payload);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void updateRoleNotFoundTest(){
        Long workspaceId = snowflakeGenerator.next();

        String name = "Administrator";
        boolean administrator = true;
        Long roleId = snowflakeGenerator.next();

        RoleDto payload = new RoleDto();
        payload.setWorkspaceId(workspaceId.toString());
        payload.setId(roleId.toString());
        payload.setAdministrator(administrator);
        payload.setName(name);

        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> roleService.updateRole(payload)).isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    void getRolesTest(){
        Long workspaceId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setPersonal(false);
        workspace.setName("Marx Kellner AG & Co. KG");

        Role role = new Role();
        role.setId(snowflakeGenerator.next());
        role.setWorkspace(workspace);
        role.setName("Administrator");
        role.setAdministrator(true);

        List<Role> roles = new ArrayList<>();
        roles.add(role);

        Mockito.when(roleRepository.findAllByWorkspaceId(workspaceId)).thenReturn(roles);
        List<RoleDto> result = roleService.getRoles(workspaceId);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(1);
    }

}
