package dev.mednikov.taskpal.workspaces.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class WorkspaceNotFoundException extends RuntimeException{
}
