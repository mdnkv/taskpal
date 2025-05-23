package dev.mednikov.taskpal.projects.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class ProjectNotFoundException extends RuntimeException{
}
