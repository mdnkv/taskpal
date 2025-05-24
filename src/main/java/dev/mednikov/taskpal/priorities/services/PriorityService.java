package dev.mednikov.taskpal.priorities.services;

import dev.mednikov.taskpal.priorities.domain.PriorityDto;

import java.util.List;

public interface PriorityService {

    PriorityDto createPriority (PriorityDto priorityDto);

    PriorityDto updatePriority (PriorityDto priorityDto);

    void deletePriority (Long priorityId);

    List<PriorityDto> getPriorities (Long workspaceId);

}
