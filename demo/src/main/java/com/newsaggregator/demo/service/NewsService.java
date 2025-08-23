package com.newsaggregator.demo.service;

import com.newsaggregator.demo.entity.NewsAPI;
import com.newsaggregator.demo.entity.UserDetails;
import com.newsaggregator.demo.entity.UserDto;
import com.newsaggregator.demo.repository.RegisterLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private RegisterLoginRepository registerLoginRepository;

    public List<NewsAPI> getNews(String username) {
        // In a real application, this method would fetch news from a database or an external API.
       // String url = "https://newsapi.org/v2/everything?q=apple&from=2025-08-22&to=2025-08-22&sortBy=popularity&apiKey=50055cf7b4984ad28a2aac0aac584465";
        UserDetails userDetails=registerLoginRepository.findByUsername(username);

        List<NewsAPI> newsList = new ArrayList<>();

        for(int i=0;i<userDetails.getPreferences().size();i++) {
            String qValue=userDetails.getPreferences().get(i);
            String url = "https://newsapi.org/v2/everything?q="+qValue+"&from=2025-08-22&to=2025-08-22&sortBy=popularity&apiKey=50055cf7b4984ad28a2aac0aac584465";
            NewsAPI response = webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(NewsAPI.class)
            .block();
            if(response!=null){
                newsList.add(response);
            }
        }


        return newsList;
    }

    public void updatePreferences(String username, UserDto userDto) {
        UserDetails userDetails = registerLoginRepository.findByUsername(username);
        if (userDetails != null) {
            userDetails.setPreferences(userDto.getPreferences());
            registerLoginRepository.save(userDetails);
        }
    }
}
