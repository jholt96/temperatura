package edge.temperatura.temperatura.security.roleAndPermission;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import lombok.Getter;
import static edge.temperatura.temperatura.security.roleAndPermission.UserPermission.*;


public enum UserRole {

    ROLE_ADMIN,
    ROLE_VIEWER

    /*
    @Getter
    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions){this.permissions = permissions;}

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                                                                  .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                                                                  .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissions;
    }*/

}