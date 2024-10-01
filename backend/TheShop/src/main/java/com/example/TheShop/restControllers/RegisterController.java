package com.example.TheShop.restControllers;


import com.example.TheShop.repository.UserRepository;
import com.example.TheShop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        origins = {"*"},
        maxAge = 3600L
)

@RestController
@RequestMapping("/api/v1")
public class RegisterController {

    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity<String> registerNewUser(@RequestParam("fullname") String fullname,
                                                  @RequestParam("email") String email,
                                                  @RequestParam(value = "password", required = false) String password,
                                                  @RequestParam(value = "googleId", required = false) String googleId,
                                                  @RequestParam("authType") String authType) {
        if(authType.equals("google")) {
            if (userService.checkGoogleUserExists(googleId)) {
                return ResponseEntity.ok("User already exists");
            } else {
                userService.registerGoogleUser(fullname, email, googleId, authType);
                return ResponseEntity.ok("Google User registered successfully");
            }
        }


        if(authType.equals("password")) {
            if(userService.checkUserExists(email)) {
                return ResponseEntity.ok("User already exists");
            } else {
                userService.registerEmailUser(fullname, email, password, authType);
                return ResponseEntity.ok("User registered successfully");
            }
        }
        return ResponseEntity.badRequest().body("Invalid authentication type.");
    }
}
