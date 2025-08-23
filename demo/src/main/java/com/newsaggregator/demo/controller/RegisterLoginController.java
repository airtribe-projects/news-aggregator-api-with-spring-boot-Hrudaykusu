package com.newsaggregator.demo.controller;

import com.newsaggregator.demo.Exceptions.InvalidCredentialsException;
import com.newsaggregator.demo.entity.NewsAPI;
import com.newsaggregator.demo.entity.UserDetails;
import com.newsaggregator.demo.entity.UserDto;
import com.newsaggregator.demo.service.NewsService;
import com.newsaggregator.demo.service.RegisterLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RegisterLoginController {

    @Autowired
    private RegisterLoginService registerLoginService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private WebClient webClient;


    @PostMapping("/register")
    public UserDetails registerUser(@RequestBody UserDto userDto) {
        if(userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        return registerLoginService.registerUser(userDto);

    }

    @GetMapping("/signin")
    public String loginUser(@RequestBody UserDto userDto) {
       if(userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
           throw new IllegalArgumentException("Username cannot be empty");
       }

       String token=registerLoginService.loginUser(userDto);

        if(token == null) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        return token;
    }

    //GET /api/preferences: Retrieve the news preferences for the logged-in user.
   @GetMapping("/preferences")
    public ResponseEntity<List<NewsAPI>> getUserPreferences(Authentication authentication) {

          String username = authentication.getName();
          if(username == null || username.isEmpty()) {
              return ResponseEntity.status(401).build();
          }

          List<NewsAPI> res=newsService.getNews(username);
          return ResponseEntity.ok(res);
    }

    @PutMapping("/preferences")
    public String updateUserPreferences(Authentication authentication, @RequestBody UserDto userDto) {
        String username = authentication.getName();
        if(username == null || username.isEmpty()) {
            return "Unauthorized"; // Unauthorized
        }

        newsService.updatePreferences(username, userDto);

        return "Preferences updated successfully";
    }

    @GetMapping("/news")
    public ResponseEntity<NewsAPI> getNews() {
        String url = "https://newsapi.org/v2/everything?q=apple&from=2025-08-22&to=2025-08-22&sortBy=popularity&apiKey=50055cf7b4984ad28a2aac0aac584465";
        NewsAPI response = webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(NewsAPI.class)
            .block(); // Only for debugging, not recommended in production

        return ResponseEntity.ok(response);
    }


}
