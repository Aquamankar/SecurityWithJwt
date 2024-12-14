package com.springSecurity.stepsForSecurity.service;

import com.springSecurity.stepsForSecurity.payload.UserDTO;

public interface UserService {


    UserDTO createOneUser(UserDTO userDTO);

    void deleteusrById(long id);
}
