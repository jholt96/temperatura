/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Service:  Provides logic regarding the User collection in mongodb and the interactions with the Rest Controller for users. 



*/

package edge.temperatura.temperatura.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import edge.temperatura.temperatura.models.Trucks;
import edge.temperatura.temperatura.models.UserRole;
import edge.temperatura.temperatura.models.Users;
import edge.temperatura.temperatura.payloads.JwtResponse;
import edge.temperatura.temperatura.payloads.ReturnUsers;
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
    TrucksServiceImpl trucksServiceImpl;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    //Returns a 200 ok response with a JWT token if successful
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
    //ADMIN creates a new user as long as the user doesnt already exist
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
        
        //Finds the Roles in the DB and assigns them to the user if they exist. 
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

    public List<ReturnUsers>getUsers(){

        List<Users> users = userRepository.findAll();
        List<ReturnUsers> returnUsers = new ArrayList<>();

        users.forEach(user -> {
            ReturnUsers tempUser = new ReturnUsers();
            tempUser.setUser(user);
            returnUsers.add(tempUser);
        });
        return returnUsers;
    }

    public ReturnUsers getByName(String email){

        Users user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException( email + " Does Not Exist in database!"));

        ReturnUsers returnUser = new ReturnUsers();
        returnUser.setUser(user);

        return returnUser;
    }

    public ResponseEntity<?> updateUser(String email, UpdateUser updatedUser){

        Users user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User Does Not Exist!"));

        if(updatedUser.getNewEmail() != user.getEmail() && updatedUser.getOldEmail() != null 
                                                        && updatedUser.getNewEmail() != null) {

            user.setEmail(updatedUser.getNewEmail());
            user.setUsername(updatedUser.getNewEmail());        
        }
        if((updatedUser.getNewPassword() != updatedUser.getOldPassword()) 
            && (user.getPassword() == passwordEncoder.encode(updatedUser.getOldPassword()))) {

            user.setPassword(passwordEncoder.encode(updatedUser.getNewPassword()));
        }

        userRepository.save(user);

        return ResponseEntity.ok("User was successfully updated");
    }

    public ResponseEntity<?> deleteUser(String username){
        Users userToDelete = userRepository.findByEmail(username)
                                .orElseThrow(() -> new RuntimeException("User Does not Exist!"));

        userRepository.delete(userToDelete);
        return ResponseEntity.ok((username + " was deleted"));
    }

    public List<Trucks> getFavoriteTrucks(String email){
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new RuntimeException("User Does not Exist!"));

        List<String> truckIds = new ArrayList<String>(user.getFavoriteTrucksIds());

        List<Trucks> favTrucks = trucksServiceImpl.getListofFavTrucks(truckIds);

        return favTrucks; 
    }
    
    public List<Trucks> setFavoriteTrucks(String email, String hostname){
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new RuntimeException("User Does not Exist!"));

        Trucks truck = trucksServiceImpl.getById(hostname)
                                        .orElseThrow(() -> new RuntimeException("Truck Does Not Exist!"));

        user.addFavTruck(truck.get_id().toString());

        userRepository.save(user);

        return this.getFavoriteTrucks(email);
    }
}