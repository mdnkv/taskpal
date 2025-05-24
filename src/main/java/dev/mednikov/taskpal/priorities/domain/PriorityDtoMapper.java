package dev.mednikov.taskpal.priorities.domain;

import dev.mednikov.taskpal.priorities.models.Priority;

import java.util.function.Function;

public final class PriorityDtoMapper implements Function<Priority, PriorityDto> {

    @Override
    public PriorityDto apply(Priority priority) {
        PriorityDto result = new PriorityDto();
        result.setId(priority.getId().toString());
        result.setWorkspaceId(priority.getWorkspace().getId().toString());
        result.setName(priority.getName());
        result.setUiOrder(priority.getUiOrder());
        return result;
    }
}
