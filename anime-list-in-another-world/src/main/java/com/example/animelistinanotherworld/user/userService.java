package com.example.animelistinanotherworld.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Blob;

@Service
public class userService {

    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;

    public userService(UserRepository userRepository, UserDataRepository userDataRepository) {
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
    }

    //set the user info into login table but also populate the userdata table to default values
    @Transactional
    public boolean registerUser(String username, String password, String email) {
        // Check if email already exists in the database
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            return false; // Registration failed due to duplicate email
        }

        // Create a new user object
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Assuming you use passwordEncoder for hashing
        user.setEmail(email);

        // Save the new user to the database
        userRepository.save(user);

        userData userData = new userData();
        //userData.setProfileImage("../images/defaultProfileImage.png");
        userData.setTotalCount(0);
        userData.setTotalPlanning(0);
        userData.setTotalWatched(0);
        userData.setTotalWatching(0);

        userData.setUser(user);

        userDataRepository.save(userData);


        return true; // Registration successful
    }

    public void updateProfileImage(Integer userId, Blob profileImage) {
        userData userData = userDataRepository.findByUser_Id(userId);
        if (userData != null) {
            userDataRepository.setProfileImageByUserId(profileImage,userId);
            userDataRepository.save(userData);
        }
    }


}
