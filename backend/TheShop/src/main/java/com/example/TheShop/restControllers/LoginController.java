package com.example.TheShop.restControllers;

import com.example.TheShop.models.Login;
import com.example.TheShop.models.User;
import com.example.TheShop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity authenticateUser(@RequestBody Login login){
        boolean userEmail = userService.checkUserExists(login.getEmail());

        if(userEmail == false){
            return new ResponseEntity("Email does not exist", HttpStatus.NOT_FOUND);
        }
        String hashedPassword = userService.checkUserPasswordByEmail(login.getEmail());
        if(!BCrypt.checkpw(login.getPassword(), hashedPassword)){
            return new ResponseEntity("Incorrect email or password", HttpStatus.BAD_REQUEST);
        }
        User user = userService.getUserDetailsByEmail(login.getEmail());
        return new ResponseEntity(user, HttpStatus.OK);



    }
}
