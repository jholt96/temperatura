package edge.temperatura.temperatura.apicontroller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edge.temperatura.temperatura.models.Users;
import edge.temperatura.temperatura.payloads.Signin;
import edge.temperatura.temperatura.payloads.Signup;
import edge.temperatura.temperatura.payloads.UpdateUser;
import edge.temperatura.temperatura.services.UserAccountService;

@RestController
@RequestMapping("/management/api/v1/users")
public class UsersController {

@Autowired
UserAccountService userAccountService;

@PostMapping(value = "/login")
public ResponseEntity<?> loginUser(@RequestBody Signin user){
    return userAccountService.loginUser(user);
}
/*
@GetMapping(value = "/crsf")
public String getCsrfToken(HttpServletRequest request){
    
    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    return csrf.getToken();
}*/

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
public List<Users>getUsers(){
    return userAccountService.getUsers();
}
@GetMapping(value = "/{email}")
@PreAuthorize("hasRole('ADMIN')")
public Optional<Users> getByName(@PathVariable("email")String email){
    return userAccountService.getByName(email);
}

@PutMapping(value = "/{email}/update")
@PreAuthorize("hasAnyRole('ADMIN','VIEWER')")
public ResponseEntity<?> updateUser(@PathVariable("email")String email, @RequestBody UpdateUser user){
    //TODO if they are validated as the user then update
    return userAccountService.updateUser(email, user);
}
    
}