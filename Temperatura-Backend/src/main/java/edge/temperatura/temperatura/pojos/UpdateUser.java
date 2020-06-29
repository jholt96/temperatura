/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provide a POJO for the update user request. 
When the user tries to update their information they will create this object in the form and then pass it in the PUT request.

*/
package edge.temperatura.temperatura.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUser {

    private String oldEmail;
    private String newEmail;
    private String oldPassword;
    private String newPassword;

    public UpdateUser(String oldEmail, String newEmail, String oldPassword, String newPassword){
        this.newEmail = newEmail;
        this.newPassword = newPassword;
        this.oldEmail = oldEmail;
        this.oldPassword = oldPassword;
    }

    public UpdateUser(){
        oldEmail = newEmail = oldPassword = newPassword = null;
    }

}