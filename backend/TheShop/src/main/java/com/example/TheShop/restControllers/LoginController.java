package com.example.TheShop.restControllers;

import com.example.TheShop.models.Login;
import com.example.TheShop.models.User;
import com.example.TheShop.services.UserService;
import com.example.TheShop.utils.GoogleTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity authenticateUser(@RequestBody Login login){

        Map<String, Object> response = new HashMap<>();

        User user = userService.getUserDetailsByEmail(login.getEmail());

        // Google login authentication
        if ("google".equals(login.getAuthType())) {
            // Handle Google user authentication
            // You should verify the Google ID token here (e.g., using Google's API)
            try {

                Payload payload = GoogleTokenVerifier.verifyGoogleToken(login.getGoogleId());

                String email = payload.getEmail();
                String payloadGoogleId = payload.getSubject();

                boolean userEmail = userService.checkUserExists(email);

                if(!userEmail){
                    response.put("status", "error");
                    response.put("message", "Email does not exist");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }

                if (!payloadGoogleId.equals(user.getGoogleId())) {
                    response.put("status", "error");
                    response.put("message", "Invalid Google authentication");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                response.put("status", "success");
                response.put("user", user);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception e) {
                response.put("status", "error");
                response.put("message", "Invalid Google authentication!!!!");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        // Manual login
        else if ("password".equals(login.getAuthType())) {
            boolean userEmail = userService.checkUserExists(login.getEmail());
            if(!userEmail){
                response.put("status", "error");
                response.put("message", "Email does not exist");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            String hashedPassword = userService.checkUserPasswordByEmail(login.getEmail());
            if (!BCrypt.checkpw(login.getPassword(), hashedPassword)) {
                response.put("status", "error");
                response.put("message", "Incorrect email or password");
                return new ResponseEntity(response, HttpStatus.OK);
            }
            response.put("status", "success");
            response.put("user", user);
            return new ResponseEntity(response, HttpStatus.OK);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid Auth Type");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }



    }
}
