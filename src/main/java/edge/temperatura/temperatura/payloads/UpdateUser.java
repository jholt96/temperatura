package edge.temperatura.temperatura.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUser {

    private String oldUsername;
    private String oldEmail;
    private String newUsername;
    private String newEmail;
    private String oldPassword;
    private String newPassword;
    
}