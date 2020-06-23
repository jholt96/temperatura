/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provide a RESTFUL API Controller for logging in, updating their account, and for users with role 'ADMIN' to create new users and manage users. 

Note: This application is meant for an enterprise thus users should not be able to create their own account. So only ADMINs can create new users. 

APIs:

GET crsf token
POST login user and retrieve a JWT token
POST a new user into the database and encrypt their password.
DELETE a single user
GET all users
GET a single user
PUT update user email, password, or both. 
GET all favorited trucks
PUT update user Favorite Trucks
*/
package edge.temperatura.temperatura.apicontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edge.temperatura.temperatura.models.Trucks;
import edge.temperatura.temperatura.payloads.ReturnUsers;
import edge.temperatura.temperatura.payloads.Signin;
import edge.temperatura.temperatura.payloads.Signup;
import edge.temperatura.temperatura.payloads.UpdateUser;
import edge.temperatura.temperatura.services.UserAccountService;

@RestController
@RequestMapping("/management/api/v1/users")
public class UsersController {

    @Autowired
    UserAccountService userAccountService;

    
    @GetMapping(value = "/crsf")
    public String getCsrfToken(HttpServletRequest request){
        
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        return csrf.getToken();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> loginUser(@RequestBody Signin user){
        return userAccountService.loginUser(user);
    }

    @PostMapping(value = "/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody Signup user){
        return userAccountService.signupUser(user);
    }

    @DeleteMapping(value = "/{username}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("username")String username){
        return userAccountService.deleteUser(username);
    }

    @GetMapping(value = "/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReturnUsers>getUsers(){
        return userAccountService.getUsers();
    }
    @GetMapping(value = "/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ReturnUsers getByName(@PathVariable("email")String email){
        return userAccountService.getByName(email);
    }

    @PutMapping(value = "/{email}/update")
    @PreAuthorize("hasAnyRole('ADMIN','VIEWER')")
    public ResponseEntity<?> updateUser(@PathVariable("email")String email, @RequestBody UpdateUser user){

        if(SecurityContextHolder.getContext().getAuthentication().getName() == user.getOldEmail()){
            return userAccountService.updateUser(email, user);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to change this user");
    }

    @GetMapping(value = "/{email}/trucks")
    @PreAuthorize("hasAnyRole('ADMIN','VIEWER')")
    public List<Trucks> getFavoriteTrucks(@PathVariable("email") String email){

        return userAccountService.getFavoriteTrucks(email);
    }

    @PutMapping(value = "/{email}/trucks/{hostname}")
    @PreAuthorize("hasAnyRole('ADMIN','VIEWER')")
    public List<Trucks> updateFavoriteTrucksList(@PathVariable("email") String email, @PathVariable("hostname")String hostname){

        if(SecurityContextHolder.getContext().getAuthentication().getName() == email){

            return userAccountService.setFavoriteTrucks(email, hostname);
        }

        return null;
    }
}