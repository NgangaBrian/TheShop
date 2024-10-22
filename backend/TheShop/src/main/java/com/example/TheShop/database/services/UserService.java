package com.example.TheShop.database.services;

import com.example.TheShop.database.models.User;
import com.example.TheShop.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerEmailUser (String fullname, String email, String password, String authType) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        userRepository.registerNewUser(fullname, email, encodedPassword, authType);
    }

    public void registerGoogleUser (String fullname, String email, String googleId, String authType) {
        userRepository.registerGoogleUser(fullname, email, googleId, authType);
    }
    public boolean checkUserExists(String email) {
        return !userRepository.checkUserEmail(email).isEmpty();
    }
    public boolean checkGoogleUserExists(String googleId) {
        return !userRepository.checkGoogleUser(googleId).isEmpty();
    }
    public String checkUserPasswordByEmail(String email){
        return userRepository.checkUserPassword(email);
    }

    public User getUserDetailsByEmail(String email){
        return userRepository.findUserByEmail(email);
    }


}
