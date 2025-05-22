package dev.mednikov.taskpal.users.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.users.models.User;
import dev.mednikov.taskpal.users.repositories.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getOrCreateUser(Jwt principal) {
        String keycloakId = principal.getSubject();
        Optional<User> result = this.userRepository.findByKeycloakId(keycloakId);
        if (result.isPresent()) {
            User user = result.get();
            boolean updated = false;
            if (!principal.getClaimAsString("email").equals(user.getEmail())){
                user.setEmail(principal.getClaimAsString("email"));
                updated = true;
            }
            if (!principal.getClaimAsString("given_name").equals(user.getFirstName())){
                user.setFirstName(principal.getClaimAsString("given_name"));
                updated = true;
            }
            if (principal.getClaimAsString("family_name").equals(user.getLastName())){
                user.setLastName(principal.getClaimAsString("family_name"));
                updated = true;
            }
            if (updated){
                return userRepository.save(user);
            } else {
                return user;
            }
        } else {
            String email = principal.getClaimAsString("email");
            String firstName = principal.getClaimAsString("given_name");
            String lastName = principal.getClaimAsString("family_name");
            User user = new User();
            user.setEmail(email);
            user.setKeycloakId(keycloakId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setId(snowflakeGenerator.next());
            return this.userRepository.save(user);
        }
    }
}
