package dev.mednikov.taskpal.statuses.services;

import dev.mednikov.taskpal.statuses.domain.StatusDto;

import java.util.List;

public interface StatusService {

    StatusDto createStatus (StatusDto statusDto);

    StatusDto updateStatus (StatusDto statusDto);

    void deleteStatus (Long statusId);

    List<StatusDto> getStatuses (Long workspaceId);

}
