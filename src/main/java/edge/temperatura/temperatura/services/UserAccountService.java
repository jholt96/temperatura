/*
Author: Josh Holt
Purpose of file: Provide business logic service for the User Controller method

*/

package edge.temperatura.temperatura.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edge.temperatura.temperatura.models.Role;
import edge.temperatura.temperatura.models.UserRole;
import edge.temperatura.temperatura.models.Users;
import edge.temperatura.temperatura.payloads.JwtResponse;
import edge.temperatura.temperatura.payloads.Signin;
import edge.temperatura.temperatura.payloads.Signup;
import edge.temperatura.temperatura.payloads.UpdateUser;
import edge.temperatura.temperatura.repositories.RoleRepository;
import edge.temperatura.temperatura.repositories.UserRepository;
import edge.temperatura.temperatura.security.JwtUtils;
import edge.temperatura.temperatura.security.UserDetailsImpl;

@Service
public class UserAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    public ResponseEntity<?> loginUser(Signin user){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
    
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
        List<String> roles = userDetails.getAuthorities().stream()
                                        .map(item -> item.getAuthority())
                                        .collect(Collectors.toList());
    
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), roles));
    }

    public ResponseEntity<?> signupUser(Signup user){

		if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("User already Exists");
        }
    
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("User already Exists");
        }
    
        
        Users newUser = new Users(user.getEmail(), 
                                  user.getEmail(), 
                                  passwordEncoder.encode(user.getPassword()));
        
    
        Set<String> strRoles = user.getRoles();
        Set<UserRole> tempRoles = new HashSet<>();
    
        if(strRoles == null){
            Role role = roleRepository.findByname(UserRole.VIEWER)
                        .orElseThrow(() -> new RuntimeException("Role does not exist!"));
    
            tempRoles.add(role.getName());
        }
        else{
            strRoles.forEach(newRole -> {
                if (newRole.equals("ADMIN")){
    
                    Role tempRole = roleRepository.findByname(UserRole.ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Role does not exist!"));
    
                    tempRoles.add(tempRole.getName());
    
                }else if (newRole.equals("VIEWER")){
    
                    Role tempRole = roleRepository.findByname(UserRole.VIEWER)
                                    .orElseThrow(() -> new RuntimeException("Role does not exist!!"));
    
                    tempRoles.add(tempRole.getName());
                }else{
    
                    throw(new RuntimeException("This Role does not Exist!"));
                }
            }); 
        }
    
        newUser.setRoles(tempRoles);
        userRepository.save(newUser);

        return ResponseEntity.ok("User has been successfully created!");
    }

public List<Users>getUsers(){
    return userRepository.findAll();
}

public Optional<Users> getByName(String email){
    return userRepository.findByEmail(email);
}

public ResponseEntity<?> updateUser(String email, UpdateUser updatedUser){
    //TODO if they are validated as the user then update
    //userRepository.save(user);
    return ResponseEntity.ok("User was successfully updated");
}

public ResponseEntity<?> deleteUser(String username){
    Users userToDelete = userRepository.findByEmail(username)
                            .orElseThrow(() -> new RuntimeException("User Does not Exist!"));

    userRepository.delete(userToDelete);
    return ResponseEntity.ok((username + " was deleted"));
}

}