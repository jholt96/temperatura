package edge.temperatura.temperatura.apicontroller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edge.temperatura.temperatura.models.UserRole;
import edge.temperatura.temperatura.models.Users;
import edge.temperatura.temperatura.repositories.UserRepository;

@RestController
@RequestMapping("/management/api/v1/users")
public class UsersController {

@Autowired
UserRepository userRepository;

@Autowired
PasswordEncoder passwordEncoder;



@PostMapping(value = "/login")
public void loginUser(@RequestBody Users user){
    //TODO signin
}

@PostMapping(value = "/signup")
//@PreAuthorize("hasAuthority('ADMIN')")
public Users createUser(@RequestBody Users user){
/*
    if (userRepository.existsByEmail(user.getEmail())) {
        return ResponseEntity
                .badRequest()
                .body(null);
    }

    if (userRepository.existsByEmail(user.getEmail())) {
        return ResponseEntity
                .badRequest()
                .body();
    }*/

    Users newUser = new Users(user.getEmail(), 
                              user.getEmail(), 
                              passwordEncoder.encode(user.getPassword()));

    Set<UserRole> roles = user.getRoles();
    roles.forEach(role -> {



    });
    newUser.setRoles(user.getRoles());
    userRepository.save(newUser);

    return user;

}

@DeleteMapping(value = "/{username}/delete")
@PreAuthorize("hasAuthority('ADMIN')")
public Users deleteUser(@PathVariable("username")String username,@RequestBody Users user){
    userRepository.delete(user);
    return user;

}

@GetMapping(value = "/")
@PreAuthorize("hasAuthority('ADMIN')")
public List<Users>getUsers(){
    return userRepository.findAll();
}
@GetMapping(value = "/{username}")
@PreAuthorize("hasAuthority('ADMIN')")
public Optional<Users> getByName(@PathVariable("username")String username){
    return userRepository.findByEmail(username);
}

@PutMapping(value = "/{username}/update")
@PreAuthorize("hasAuthority('ADMIN')")
public Users updateUser(@PathVariable("username")String username, @RequestBody Users user){
    //TODO if they are validated as the user then update
    userRepository.save(user);
    return user;
}
    
}