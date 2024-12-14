package com.springSecurity.stepsForSecurity.controller;

import com.springSecurity.stepsForSecurity.payload.UserDTO;
import com.springSecurity.stepsForSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
       UserDTO userDTO1= userService.createOneUser(userDTO);
       return  new ResponseEntity<>(userDTO1, HttpStatus.OK);
    }



    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public  ResponseEntity<String> deleteUser(@PathVariable long id){
        userService.deleteusrById(id);
        return new ResponseEntity<>("user deleted with id "+id,HttpStatus.OK);
    }
}
