package dev.mednikov.taskpal.priorities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PriorityNotFoundException extends RuntimeException{
}
