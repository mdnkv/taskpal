package dev.mednikov.taskpal.users.services;

import dev.mednikov.taskpal.users.models.User;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {

    User getOrCreateUser(Jwt principal);

}
