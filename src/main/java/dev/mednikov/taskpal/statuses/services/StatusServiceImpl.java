package dev.mednikov.taskpal.statuses.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.statuses.domain.StatusDto;
import dev.mednikov.taskpal.statuses.domain.StatusDtoMapper;
import dev.mednikov.taskpal.statuses.exceptions.StatusNotFoundException;
import dev.mednikov.taskpal.statuses.models.Status;
import dev.mednikov.taskpal.statuses.repositories.StatusRepository;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StatusServiceImpl implements StatusService {

    private final static StatusDtoMapper statusDtoMapper = new StatusDtoMapper();
    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final StatusRepository statusRepository;
    private final WorkspaceRepository workspaceRepository;

    public StatusServiceImpl(StatusRepository statusRepository, WorkspaceRepository workspaceRepository) {
        this.statusRepository = statusRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public StatusDto createStatus(StatusDto statusDto) {
        Long workspaceId = Long.parseLong(statusDto.getWorkspaceId());
        Workspace workspace = this.workspaceRepository.getReferenceById(workspaceId);
        Status status = new Status();
        status.setId(snowflakeGenerator.next());
        status.setName(statusDto.getName());
        status.setWorkspace(workspace);

        Status result = this.statusRepository.save(status);
        return statusDtoMapper.apply(result);
    }

    @Override
    public StatusDto updateStatus(StatusDto statusDto) {
        Objects.requireNonNull(statusDto.getId());
        Long id = Long.parseLong(statusDto.getId());
        Status status = this.statusRepository.findById(id).orElseThrow(StatusNotFoundException::new);

        status.setName(statusDto.getName());
        Status result = this.statusRepository.save(status);
        return statusDtoMapper.apply(result);
    }

    @Override
    public void deleteStatus(Long statusId) {
        this.statusRepository.deleteById(statusId);

    }

    @Override
    public List<StatusDto> getStatuses(Long workspaceId) {
        return this.statusRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(statusDtoMapper)
                .toList();
    }
}
