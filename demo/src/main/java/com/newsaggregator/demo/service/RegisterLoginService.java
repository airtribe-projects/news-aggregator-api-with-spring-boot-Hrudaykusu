package com.newsaggregator.demo.service;

import com.newsaggregator.demo.entity.UserDetails;
import com.newsaggregator.demo.entity.UserDto;
import com.newsaggregator.demo.repository.RegisterLoginRepository;
import com.newsaggregator.demo.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterLoginService {

    @Autowired
    private RegisterLoginRepository registerLoginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public  String loginUser(UserDto userDto) {
       UserDetails userDetails= registerLoginRepository.findByUsername(userDto.getUsername());
        if(userDetails != null && passwordEncoder.matches(userDto.getPassword(), userDetails.getPassword())) {
            // Logic for successful login
            return JwtUtil.generateToken(userDetails.getUsername());
        }

        return "Invalid credentials for user: " + userDto.getUsername();

    }


    public UserDetails registerUser(UserDto userDto) {
        // Logic to register a user
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(userDto.getUsername());
        String pwd=passwordEncoder.encode(userDto.getPassword());
        userDetails.setPassword(pwd);
        userDetails.setPreferences(userDto.getPreferences());

        return registerLoginRepository.save(userDetails);

    }
}
