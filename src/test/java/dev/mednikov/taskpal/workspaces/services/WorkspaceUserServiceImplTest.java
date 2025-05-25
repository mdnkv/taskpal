package dev.mednikov.taskpal.workspaces.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.roles.models.Role;
import dev.mednikov.taskpal.roles.repositories.RoleRepository;
import dev.mednikov.taskpal.users.models.User;
import dev.mednikov.taskpal.users.repositories.UserRepository;
import dev.mednikov.taskpal.workspaces.domain.CreateWorkspaceUserRequestDto;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceUserDto;
import dev.mednikov.taskpal.workspaces.exceptions.PersonalWorkspaceException;
import dev.mednikov.taskpal.workspaces.exceptions.WorkspaceUserAlreadyExistsException;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import dev.mednikov.taskpal.workspaces.models.WorkspaceUser;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceRepository;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceUserRepository;
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
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class WorkspaceUserServiceImplTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Mock private WorkspaceUserRepository workspaceUserRepository;
    @Mock private WorkspaceRepository workspaceRepository;
    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;

    @InjectMocks private WorkspaceUserServiceImpl workspaceUserService;

    @Test
    void createWorkspaceUser_alreadyExistsTest(){
        Long workspaceId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();
        Long userId = snowflakeGenerator.next();
        String email = "l87dyxst2ohnl4gg@ymail.com";

        User user = new User();
        user.setKeycloakId(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setId(userId);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(workspaceUserRepository.findByWorkspaceIdAndUserId(workspaceId, userId)).thenReturn(Optional.of(new WorkspaceUser()));

        CreateWorkspaceUserRequestDto request = new CreateWorkspaceUserRequestDto();
        request.setEmail(email);
        request.setWorkspaceId(workspaceId.toString());
        request.setRoleId(roleId.toString());

        Assertions.assertThatThrownBy(() -> workspaceUserService.createWorkspaceUser(request))
                .isInstanceOf(WorkspaceUserAlreadyExistsException.class);


    }

    @Test
    void createWorkspaceUser_personalWorkspaceTest(){
        Long workspaceId = snowflakeGenerator.next();
        Long userId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();
        String email = "7hvrbqh2v0mc1fi9h@gmail.com";

        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("My workspace");
        workspace.setPersonal(true);

        User user = new User();
        user.setKeycloakId(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setId(userId);


        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(workspaceUserRepository.findByWorkspaceIdAndUserId(workspaceId, userId)).thenReturn(Optional.empty());
        Mockito.when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspace));

        CreateWorkspaceUserRequestDto request = new CreateWorkspaceUserRequestDto();
        request.setEmail(email);
        request.setWorkspaceId(workspaceId.toString());
        request.setRoleId(roleId.toString());

        Assertions.assertThatThrownBy(() -> workspaceUserService.createWorkspaceUser(request))
                .isInstanceOf(PersonalWorkspaceException.class);

    }

    @Test
    void createWorkspaceUser_successTest(){
        Long roleId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();
        Long userId = snowflakeGenerator.next();
        String email = "tc7sbq9tmd1iwy79f@comcast.net";

        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Brand Steffens AG & Co. KG");
        workspace.setPersonal(false);

        User user = new User();
        user.setKeycloakId(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setId(userId);

        Role role = new Role();
        role.setId(roleId);
        role.setName("Administrator");
        role.setAdministrator(true);
        role.setWorkspace(workspace);

        WorkspaceUser workspaceUser = new WorkspaceUser();
        workspaceUser.setId(snowflakeGenerator.next());
        workspaceUser.setRole(role);
        workspaceUser.setUser(user);
        workspaceUser.setWorkspace(workspace);

        CreateWorkspaceUserRequestDto request = new CreateWorkspaceUserRequestDto();
        request.setEmail(email);
        request.setWorkspaceId(workspaceId.toString());
        request.setRoleId(roleId.toString());

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(workspaceUserRepository.findByWorkspaceIdAndUserId(workspaceId, userId)).thenReturn(Optional.empty());
        Mockito.when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspace));
        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        Mockito.when(workspaceUserRepository.save(workspaceUser)).thenReturn(workspaceUser);


        WorkspaceUserDto result = workspaceUserService.createWorkspaceUser(request);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void setActiveWorkspaceTest(){
        Long userId = snowflakeGenerator.next();
        Long activeId = snowflakeGenerator.next();

        User user = new User();
        user.setId(userId);

        Workspace workspace = new Workspace();
        workspace.setId(snowflakeGenerator.next());
        workspace.setName("Wieland Steinbach KG");
        workspace.setPersonal(false);

        Role role = new Role();
        role.setId(snowflakeGenerator.next());
        role.setName("Administrator");
        role.setAdministrator(true);
        role.setWorkspace(workspace);

        List<WorkspaceUser> workspaceUsers = new ArrayList<>();
        WorkspaceUser workspaceUser1 = new WorkspaceUser();
        workspaceUser1.setId(activeId);
        workspaceUser1.setUser(user);
        workspaceUser1.setActive(false);
        workspaceUser1.setRole(role);
        workspaceUser1.setWorkspace(workspace);
        workspaceUsers.add(workspaceUser1);

        WorkspaceUser workspaceUser2 = new WorkspaceUser();
        workspaceUser2.setId(snowflakeGenerator.next());
        workspaceUsers.add(workspaceUser2);
        workspaceUser1.setActive(false);

        WorkspaceUser workspaceUser3 = new WorkspaceUser();
        workspaceUser3.setId(snowflakeGenerator.next());
        workspaceUser3.setActive(true);
        workspaceUsers.add(workspaceUser3);

        Mockito.when(workspaceUserRepository.findAllByUserId(userId)).thenReturn(workspaceUsers);

        WorkspaceUserDto result = workspaceUserService.setActiveWorkspace(user, activeId);
        Assertions.assertThat(result).isNotNull()
                .hasFieldOrProperty("workspace")
                .hasFieldOrProperty("role")
                .hasFieldOrPropertyWithValue("active", true);
    }

    @Test
    void getActiveWorkspace_existsTest(){
        Long userId = snowflakeGenerator.next();
        User user = new User();
        user.setId(userId);

        Workspace workspace = new Workspace();
        workspace.setId(snowflakeGenerator.next());
        workspace.setName("Schiller Jordan GbR");
        workspace.setPersonal(false);

        Role role = new Role();
        role.setId(snowflakeGenerator.next());
        role.setName("Administrator");
        role.setAdministrator(true);
        role.setWorkspace(workspace);

        WorkspaceUser workspaceUser = new WorkspaceUser();
        workspaceUser.setId(snowflakeGenerator.next());
        workspaceUser.setRole(role);
        workspaceUser.setUser(user);
        workspaceUser.setWorkspace(workspace);

        Mockito.when(workspaceUserRepository.findActive(userId)).thenReturn(Optional.of(workspaceUser));
        Optional<WorkspaceUserDto> result = workspaceUserService.getActiveWorkspace(user);
        Assertions.assertThat(result).isPresent();
    }

    @Test
    void getActiveWorkspace_notExistsTest(){
        Long userId = snowflakeGenerator.next();
        User user = new User();
        user.setId(userId);

        Mockito.when(workspaceUserRepository.findActive(userId)).thenReturn(Optional.empty());
        Optional<WorkspaceUserDto> result = workspaceUserService.getActiveWorkspace(user);
        Assertions.assertThat(result).isNotPresent();
    }




}
