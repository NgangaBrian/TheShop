package com.example.TheShop.database.repository;


import com.example.TheShop.database.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository <User, Long> {

    // Check if a user email exists in the system
    @Query(value = "select email from users where email = :email", nativeQuery = true)
    List<String> checkUserEmail(@Param("email") String email);

    @Query(value = "select  * from users where email = :email", nativeQuery = true)
    User findUserByEmail(@Param("email") String email);

    @Query(value = "select  password from users where email = :email", nativeQuery = true)
    String checkUserPassword(@Param("email") String email);

    // Check if a Google ID exists in the system
    @Query(value = "SELECT googleId FROM users WHERE googleId = :googleId", nativeQuery = true)
    List<String> checkGoogleUser(@Param("googleId") String googleId);

    // Find a user by their Google ID (for Google Sign-In)
    @Query(value = "SELECT * FROM users WHERE googleId = :googleId", nativeQuery = true)
    User findUserByGoogleId(@Param("googleId") String googleId);

    // Insert a new manual user (no Google Sign-In)
    @Transactional
    @Modifying
    @Query(value = "insert into users(fullname, email, password, authType) values(:fullname, :email, :password, :authType)", nativeQuery = true)
    int registerNewUser (@Param("fullname") String fullname,
                         @Param("email") String email,
                         @Param("password") String password,
                         @Param("authType") String authType);

    // Insert a new Google user
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO users(fullname, email, googleId, authType) VALUES(:fullname, :email, :googleId, :authType)", nativeQuery = true)
    int registerGoogleUser(@Param("fullname") String fullname,
                           @Param("email") String email,
                           @Param("googleId") String googleId,
                           @Param("authType") String authType);
}
