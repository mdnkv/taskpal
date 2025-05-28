package dev.mednikov.taskpal.workspaces.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.roles.exceptions.RoleNotFoundException;
import dev.mednikov.taskpal.roles.models.Role;
import dev.mednikov.taskpal.roles.repositories.RoleRepository;
import dev.mednikov.taskpal.users.exceptions.UserNotFoundException;
import dev.mednikov.taskpal.users.models.User;
import dev.mednikov.taskpal.users.repositories.UserRepository;
import dev.mednikov.taskpal.workspaces.domain.CreateWorkspaceUserRequestDto;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceUserDto;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceUserDtoMapper;
import dev.mednikov.taskpal.workspaces.events.WorkspaceCreatedEvent;
import dev.mednikov.taskpal.workspaces.exceptions.PersonalWorkspaceException;
import dev.mednikov.taskpal.workspaces.exceptions.WorkspaceNotFoundException;
import dev.mednikov.taskpal.workspaces.exceptions.WorkspaceUserAlreadyExistsException;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import dev.mednikov.taskpal.workspaces.models.WorkspaceUser;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceRepository;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceUserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkspaceUserServiceImpl implements WorkspaceUserService{

    private final static WorkspaceUserDtoMapper workspaceUserDtoMapper = new WorkspaceUserDtoMapper();
    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final WorkspaceUserRepository workspaceUserRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final RoleRepository roleRepository;

    public WorkspaceUserServiceImpl(
            WorkspaceUserRepository workspaceUserRepository,
            UserRepository userRepository,
            WorkspaceRepository workspaceRepository,
            RoleRepository roleRepository) {
        this.workspaceUserRepository = workspaceUserRepository;
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<WorkspaceUserDto> getActiveWorkspace(User user) {
        return this.workspaceUserRepository.findActive(user.getId()).map(workspaceUserDtoMapper);
    }

    @Override
    public WorkspaceUserDto setActiveWorkspace(User user, Long workspaceId) {
        // Retrieve all workspaces for the user
        List<WorkspaceUser> workspaces = this.workspaceUserRepository.findAllByUserId(user.getId());

        /*
        If the workspace user id == workspaceId then mark it as active
        Else mark the workspace as not active
         */
        List<WorkspaceUser> result = workspaces
                .stream()
                .peek(ws -> ws.setActive(ws.getId().equals(workspaceId)))
                .toList();

        // Persist
        this.workspaceUserRepository.saveAll(result);

        // Return the active workspace
        WorkspaceUser active = workspaces.stream().filter(WorkspaceUser::isActive).findFirst().orElseThrow();
        return workspaceUserDtoMapper.apply(active);
    }

    @Override
    public WorkspaceUserDto createWorkspaceUser(CreateWorkspaceUserRequestDto request) {
        User user = this.userRepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);
        Long workspaceId = Long.parseLong(request.getWorkspaceId());

        // Verify that the user does not already belong to the workspace
        if (this.workspaceUserRepository.findByWorkspaceIdAndUserId(workspaceId, user.getId()).isPresent()){
            throw new WorkspaceUserAlreadyExistsException();
        }

        Workspace workspace = this.workspaceRepository.findById(workspaceId).orElseThrow(WorkspaceNotFoundException::new);

        // Verify that the workspace is not a personal workspace
        if (workspace.isPersonal()){
            throw new PersonalWorkspaceException();
        }
        Long roleId = Long.parseLong(request.getRoleId());
        Role role = this.roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);

        // Verify that the user does not already have another active workspace
        boolean active = this.workspaceUserRepository.findAllByUserId(user.getId()).isEmpty();

        WorkspaceUser workspaceUser = new WorkspaceUser();
        workspaceUser.setId(snowflakeGenerator.next());
        workspaceUser.setRole(role);
        workspaceUser.setUser(user);
        workspaceUser.setWorkspace(workspace);
        workspaceUser.setActive(active);

        WorkspaceUser result = this.workspaceUserRepository.save(workspaceUser);
        return workspaceUserDtoMapper.apply(result);

    }

    @Override
    public List<WorkspaceUserDto> getUserWorkspaces(User user) {
        return this.workspaceUserRepository.findAllByUserId(user.getId())
                .stream()
                .map(workspaceUserDtoMapper)
                .toList();
    }

    @EventListener
    public void onWorkspaceCreatedEventListener (WorkspaceCreatedEvent event){
        Workspace workspace = this.workspaceRepository.findById(event.getWorkspaceId()).orElseThrow();
        // Create roles
        List<Role> roles = new ArrayList<>();
        Role adminRole = new Role();
        Long adminRoleId = snowflakeGenerator.next();
        adminRole.setId(adminRoleId);
        adminRole.setName("Administrator");
        adminRole.setWorkspace(workspace);
        adminRole.setAdministrator(true);
        roles.add(adminRole);

        // If workspace is not personal, then create other roles too
        if (!workspace.isPersonal()){
            Role managerRole = new Role();
            managerRole.setId(snowflakeGenerator.next());
            managerRole.setName("Project Manager");
            managerRole.setWorkspace(workspace);
            managerRole.setAdministrator(false);

            Role developerRole = new Role();
            developerRole.setId(snowflakeGenerator.next());
            developerRole.setName("Developer");
            developerRole.setWorkspace(workspace);
            developerRole.setAdministrator(false);

            roles.add(managerRole);
            roles.add(developerRole);
        }

        this.roleRepository.saveAll(roles);

        // Create a workspace user entity for an owner
        WorkspaceUser admin = new WorkspaceUser();
        admin.setId(snowflakeGenerator.next());
        admin.setUser(event.getOwner());

        // get admin role
        admin.setWorkspace(workspace);
        admin.setRole(adminRole);

        boolean active = this.workspaceUserRepository.findAllByUserId(event.getOwner().getId()).isEmpty();
        admin.setActive(active);
        this.workspaceUserRepository.save(admin);
    }

}
