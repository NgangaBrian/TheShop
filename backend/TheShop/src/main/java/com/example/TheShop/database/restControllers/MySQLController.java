package com.example.TheShop.database.restControllers;

import com.example.TheShop.database.models.*;
import com.example.TheShop.database.services.BannerSliderService;
import com.example.TheShop.database.services.CategoriesService;
import com.example.TheShop.database.services.ItemsService;
import com.example.TheShop.database.services.UserService;
import com.example.TheShop.database.utils.GoogleTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MySQLController {


    private final BannerSliderService bannerSliderService;
    private CategoriesService categoriesService;
    private ItemsService itemsService;
    private UserService userService;


    public MySQLController(UserService userService, ItemsService itemsService, CategoriesService categoriesService, BannerSliderService bannerSliderService) {
        this.userService = userService;
        this.itemsService = itemsService;
        this.categoriesService = categoriesService;
        this.bannerSliderService = bannerSliderService;
    }


    @PostMapping("/user/register")
    public ResponseEntity<String> registerNewUser(@RequestParam("fullname") String fullname,
                                                  @RequestParam("email") String email,
                                                  @RequestParam(value = "password", required = false) String password,
                                                  @RequestParam(value = "googleId", required = false) String idToken,
                                                  @RequestParam("authType") String authType) {
        if(authType.equals("google")) {
            try {
                GoogleIdToken.Payload payload = GoogleTokenVerifier.verifyGoogleToken(idToken);

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

    @PostMapping("/user/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody Login login){

        Map<String, Object> response = new HashMap<>();

        User user = userService.getUserDetailsByEmail(login.getEmail());

        // Google login authentication
        if ("google".equals(login.getAuthType())) {
            // Handle Google user authentication
            // You should verify the Google ID token here (e.g., using Google's API)
            try {

                GoogleIdToken.Payload payload = GoogleTokenVerifier.verifyGoogleToken(login.getGoogleId());

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
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.put("status", "success");
            response.put("user", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid Auth Type");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    @GetMapping("/bannersliders")
    public List<BannerSliderModel> getSliders() {
        return bannerSliderService.getAllBannerSliders();
    }

    @GetMapping("/categories")
    public List<CategoriesModel> getAllCategories() {
        return categoriesService.getAllCategories();
    }

    @GetMapping("/items")
    public List<ItemsModel> getAllItems() {
        return itemsService.getAllItems();
    }
}
