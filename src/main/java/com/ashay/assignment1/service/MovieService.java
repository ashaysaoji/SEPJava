package com.ashay.assignment1.service;

import com.ashay.assignment1.dto.MovieResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Service
public class MovieService {

    private final WebClient webClient;

    public MovieService(WebClient webClient) {
        this.webClient = webClient;
    }

    private Mono<MovieResponse> fetchPage(int page, String title, Integer year) {
        Mono<MovieResponse> movieResponse = webClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .queryParam("page", page);
                    if (title != null) builder = builder.queryParam("Title", title);
                    if (year != null) builder = builder.queryParam("Year", year);
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(MovieResponse.class);

        return movieResponse;
    }

    private List<Object> fetchAllPages(String title, Integer year) {
        MovieResponse firstPage = fetchPage(1,  title, year).block();
        int totalPages = firstPage.getTotalPages();
        List<Object> allMovies = new ArrayList<>();

        List<Mono<MovieResponse>> request = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            final int page = i;
            request.add(fetchPage(page, title, year));
        }

        Flux.merge(request)
                .collectList()
                .block()
                .forEach(response -> allMovies.addAll(response.getData()));
        return allMovies;
    }


    public List<Object> getAllMovies(){
        return fetchAllPages(null, null);
    }

    public List<Object> getMoviesByParams(String title, Integer year) {
        return fetchAllPages(title, year);
    }
}
