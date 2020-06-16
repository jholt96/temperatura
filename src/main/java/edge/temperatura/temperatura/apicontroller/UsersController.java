package edge.temperatura.temperatura.apicontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edge.temperatura.temperatura.models.Users;
import edge.temperatura.temperatura.repositories.UserRepository;

@RestController
@RequestMapping("management/api/v1/users")
public class UsersController {

@Autowired
UserRepository userRepository;



@PostMapping(value = "/login")
public void loginUser(@RequestBody Users user){
    //TODO signin
}
@PostMapping(value = "/signup")
public Users createUser(@RequestBody Users user){
    userRepository.save(user);
    return user;
}
@DeleteMapping(value = "/{username}/delete")
public Users deleteUser(@PathVariable("username")String username,@RequestBody Users user){
    userRepository.delete(user);
    return user;

}
@GetMapping(value = "/")
public List<Users>getUsers(){
    return userRepository.findAll();
}

@GetMapping(value = "/{username}")
public Optional<Users> getByName(@PathVariable("username")String username){
    return userRepository.findByEmail(username);
}



@PutMapping(value = "/{username}/update")
public Users updateUser(@PathVariable("username")String username, @RequestBody Users user){
    userRepository.save(user);
    return user;
}
    
}