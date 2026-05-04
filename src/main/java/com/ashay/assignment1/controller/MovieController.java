package com.ashay.assignment1.controller;


import com.ashay.assignment1.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }


    @GetMapping
    public List<Object> getAllMovies() {
        return  movieService.getAllMovies();
    }

    @GetMapping(params = {"title"})
    public List<Object> getMoviesByParams(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) Integer year) {
    return movieService.getMoviesByParams(title, year);

    }
}

