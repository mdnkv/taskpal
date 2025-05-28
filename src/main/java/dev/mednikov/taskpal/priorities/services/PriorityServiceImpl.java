package dev.mednikov.taskpal.priorities.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.priorities.domain.PriorityDto;
import dev.mednikov.taskpal.priorities.domain.PriorityDtoMapper;
import dev.mednikov.taskpal.priorities.exceptions.PriorityNotFoundException;
import dev.mednikov.taskpal.priorities.models.Priority;
import dev.mednikov.taskpal.priorities.repositories.PriorityRepository;
import dev.mednikov.taskpal.workspaces.events.WorkspaceCreatedEvent;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PriorityServiceImpl implements PriorityService{

    private final static PriorityDtoMapper priorityDtoMapper = new PriorityDtoMapper();
    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final PriorityRepository priorityRepository;
    private final WorkspaceRepository workspaceRepository;

    public PriorityServiceImpl(PriorityRepository priorityRepository, WorkspaceRepository workspaceRepository) {
        this.priorityRepository = priorityRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public PriorityDto createPriority(PriorityDto priorityDto) {
        Long workspaceId = Long.parseLong(priorityDto.getWorkspaceId());
        Workspace workspace = this.workspaceRepository.getReferenceById(workspaceId);
        Priority priority = new Priority();
        priority.setId(snowflakeGenerator.next());
        priority.setName(priorityDto.getName());
        priority.setWorkspace(workspace);
        priority.setUiOrder(priorityDto.getUiOrder());
        Priority result = this.priorityRepository.save(priority);
        return priorityDtoMapper.apply(result);
    }

    @Override
    public PriorityDto updatePriority(PriorityDto priorityDto) {
        Objects.requireNonNull(priorityDto.getId());
        Long id = Long.parseLong(priorityDto.getId());
        Priority priority = this.priorityRepository.findById(id).orElseThrow(PriorityNotFoundException::new);
        priority.setName(priorityDto.getName());
        priority.setUiOrder(priorityDto.getUiOrder());
        Priority result = this.priorityRepository.save(priority);
        return priorityDtoMapper.apply(result);
    }

    @Override
    public void deletePriority(Long priorityId) {
        this.priorityRepository.deleteById(priorityId);

    }

    @Override
    public List<PriorityDto> getPriorities(Long workspaceId) {
        return this.priorityRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(priorityDtoMapper)
                .toList();
    }

    @EventListener
    public void onWorkspaceCreatedEventListener (WorkspaceCreatedEvent event){
        Workspace workspace = this.workspaceRepository.getReferenceById(event.getWorkspaceId());
        List<Priority> priorities = new ArrayList<>();
        Priority high = new Priority();
        high.setId(snowflakeGenerator.next());
        high.setName("High");
        high.setWorkspace(workspace);
        high.setUiOrder(1);
        priorities.add(high);
        Priority medium = new Priority();
        medium.setId(snowflakeGenerator.next());
        medium.setName("Medium");
        medium.setWorkspace(workspace);
        medium.setUiOrder(2);
        priorities.add(medium);
        Priority low = new Priority();
        low.setId(snowflakeGenerator.next());
        low.setName("Low");
        low.setWorkspace(workspace);
        low.setUiOrder(3);
        priorities.add(low);
        this.priorityRepository.saveAll(priorities);
    }

}
