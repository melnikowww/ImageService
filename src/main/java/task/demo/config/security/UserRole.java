package task.demo.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static task.demo.config.security.Permission.MODERATOR_CREATE;
import static task.demo.config.security.Permission.MODERATOR_DELETE;
import static task.demo.config.security.Permission.MODERATOR_READ;
import static task.demo.config.security.Permission.MODERATOR_UPDATE;

@RequiredArgsConstructor
public enum UserRole {
    MODERATOR(
        Set.of(
            MODERATOR_READ,
            MODERATOR_UPDATE,
            MODERATOR_DELETE,
            MODERATOR_CREATE
        )
    ),
    USER(Collections.EMPTY_SET);

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
