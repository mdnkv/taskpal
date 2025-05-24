package dev.mednikov.taskpal.statuses.domain;

import dev.mednikov.taskpal.statuses.models.Status;

import java.util.function.Function;

public final class StatusDtoMapper implements Function<Status, StatusDto> {

    @Override
    public StatusDto apply(Status status) {
        StatusDto result = new StatusDto();
        result.setId(status.getId().toString());
        result.setWorkspaceId(status.getWorkspace().getId().toString());
        result.setName(status.getName());
        return result;
    }

}
