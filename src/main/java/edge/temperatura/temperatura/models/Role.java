package edge.temperatura.temperatura.models;

import org.apache.catalina.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;


@Document(value = "roles")
@Getter
@Setter
public class Role {
    
    @Id
    private String id;

    private UserRole name;

    public Role() {
        this.name = UserRole.VIEWER;
    }

    public Role(UserRole name) {
        this.name = name;
      }
}