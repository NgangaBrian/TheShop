package com.example.TheShop.restControllers;


import com.example.TheShop.repository.UserRepository;
import com.example.TheShop.services.UserService;
import com.example.TheShop.utils.GoogleTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
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
                                                  @RequestParam(value = "googleId", required = false) String idToken,
                                                  @RequestParam("authType") String authType) {
        if(authType.equals("google")) {
            try {
                Payload payload = GoogleTokenVerifier.verifyGoogleToken(idToken);

                String payloadEmail = payload.getEmail();
                String payloadGoogleId = payload.getSubject();
                String payloadName = (String) payload.get("name");

            if (userService.checkGoogleUserExists(payloadGoogleId)) {
                return ResponseEntity.ok("User already exists");
            } else {
                userService.registerGoogleUser(payloadName, payloadEmail, payloadGoogleId, authType);
                return ResponseEntity.ok("Google User registered successfully");
            }
            } catch (Exception e) {
                throw new RuntimeException(e);
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
