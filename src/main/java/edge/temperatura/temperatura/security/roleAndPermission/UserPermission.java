package edge.temperatura.temperatura.security.roleAndPermission;

import lombok.Getter;

public enum UserPermission {
     TRUCKS_READ("trucks:read"),
     TRUCKS_WRITE("trucks:write"),
     ALERTS_READ("alerts:read"),
     ALERTS_WRITE("alerts:write"),
     USER_READ("user:read"),
     USER_WRITE("user:write");

     @Getter
     private final String permission;

     UserPermission(String permission){
         this.permission = permission;
     }
}